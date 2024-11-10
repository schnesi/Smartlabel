package ch.schnes.smartlabel.server.database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.schnes.smartlabel.server.DataService;
import ch.schnes.smartlabel.server.Logger;

public class DataRepository implements Runnable {
	private DataService dataService;
	private DatabaseManager dbManager = null;
	private final String INSTANCE = "DataRepository";
	private final Logger logger = Logger.getInstance();
	private final Map<String, Object> jsonMap;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public DataRepository(Map<String, Object> json, DataService dataService) throws IOException {
		this.dataService = dataService;
		dbManager = new DatabaseManager();
		this.jsonMap = objectMapper.readValue(objectMapper.writeValueAsString(json), Map.class);
	}
	
	@Override
	public void run() {
		Map<String, Object> body = null;
		String header = (String) jsonMap.get("header");
		Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");
		String headerResponse = null;
		String topic = null;
		
		// Handle requests
		if ("request".equals(header)) {
			String item = (String) data.get("item");
			System.out.println("DataRepository: header: " + header + ", item: " + item);
			
			// Handle the order request from the client
			if ("order".equals(item)) {
				System.out.println("Order Data requested");
				logger.logInfo(INSTANCE, "Order data requested");
				body = requestOrder();
				body.put("clientId", data.get("clientId"));
				headerResponse = "responseOrder";
				topic = "/smartlabel/client/" + data.get("clientId");
				logger.logInfo(INSTANCE, "Request order data successful");
			}
			
			// Handle the dataSmartlabel request from the smartlabel
			if ("dataSmartlabel".equals(item)) {
				System.out.println("Data smartlabel requested");
				logger.logInfo(INSTANCE, "Data smartlabel requested");
				body = requestDataSmartlabel(data.get("smartlabelSN"));
				body.put("smartlabelSN", data.get("smartlabelSN"));
				headerResponse = "response";
				topic = "/smartlabel/smartlabel/" + data.get("smartlabelSN");
				logger.logInfo(INSTANCE, "Request data smartlabel successful");
			}

			System.out.print("DataRepository: Execute query successful: ");
			System.out.println(body);
		}
		
		// Handle order data changes
		if ("dbData".equals(header)) {
			System.out.println(INSTANCE + ": Order data change received");
			logger.logInfo(INSTANCE, "Order data change received");
			try {
				Map<String, Object> ref = selectData(DbQueryBuilder.selectOrderRef(data.get("orderid")));
				ref = (Map<String, Object>) ref.get("0");
				System.out.println(ref);
				if (data.get("qty").toString().equals(ref.get("quantity").toString())) {
					System.out.println(INSTANCE + ": Delete order");
					logger.logInfo(INSTANCE, "Delete order");
					System.out.println("Löschen einfügen");
				}
				else {
					System.out.println(INSTANCE + ": Change order");
					logger.logInfo(INSTANCE, "Change order");
					int refValue = Integer.parseInt(ref.get("quantity").toString());
					int dataValue = Integer.parseInt(data.get("qty").toString());
					int newValue = refValue - dataValue;
					changeData(DbQueryBuilder.updateOrder(data.get("orderid"), newValue));
					ref = selectData(DbQueryBuilder.selectOrderStorageRef(data.get("storage")));
					ref = (Map<String, Object>) ref.get("0");
					refValue = Integer.parseInt(ref.get("quantity").toString());
					newValue = refValue - dataValue;
					changeData(DbQueryBuilder.updateStorage(data.get("storage"), newValue));
					System.out.println(INSTANCE + ": Change order successful");
					logger.logInfo(INSTANCE, "Change order successful");
				}
			} catch (SQLException e) {
				sqlException(e);
			}
		}
		
		// Handle the orderSmartlabel state. If no order exist then a new one will created.
		if ("orderSmartlabel".equals(header)) {
			System.out.println("New order");
			logger.logInfo(INSTANCE, "New order from Smartlabel: " + data.get("smartlabelSN"));
			try {
				Map<String, Object> ref = selectData(DbQueryBuilder.selectOrderSmartlabel(data.get("smartlabelSN")));
				ref = (Map<String, Object>) ref.get("0");
				System.out.println(ref);
				logger.logInfo(INSTANCE, "Check order data: " + ref);
				
				if (ref.get("order") == null) {
					logger.logInfo(INSTANCE, "Insert new order");
					changeData(DbQueryBuilder.insertNewOrder(
						ref.get("materialno"), ref.get("smartlabelid"), ref.get("deliveryid"), ref.get("orderqty")
					));
					System.out.println(INSTANCE + ": New order successfully inserted");
					logger.logInfo(INSTANCE, "New order successfully inserted");
				}
				
				body = requestDataSmartlabel(data.get("smartlabelSN"));
				body.put("smartlabelSN", data.get("smartlabelSN"));
				headerResponse = "response";
				topic = "/smartlabel/smartlabel/" + data.get("smartlabelSN");
				logger.logInfo(INSTANCE, "Response data smartlabel successful");				
			} catch (SQLException e) {
				sqlException(e);
			}
		}
		
		Map<String, Object> answer = new HashMap<>();
		answer.put("header", headerResponse);
		answer.put("data", body);
		dataService.publish(topic, answer);
	}
	
	private Map<String, Object> requestOrder() {
		Map<String, Object> body = null;
		try {
			body = selectData(DbQueryBuilder.selectOrder());
			logger.logInfo(INSTANCE, "Select Data from DB successful");
		} catch (SQLException e) {
			sqlException(e);
		}
		return body;
	}
	
	private Map<String, Object> requestDataSmartlabel(Object smartlabelSN) {
		Map<String, Object> body = null;
		try {
			body = selectData(DbQueryBuilder.selectSmartlabelData(smartlabelSN));
			logger.logInfo(INSTANCE, "Select Data from DB successful");
		} catch (SQLException e) {
			sqlException(e);
		}
		return body;
	}
	
	private Map<String, Object> selectData(String query) throws SQLException {
		ResultSet resultSet = null;
		try {
			dbManager.connect();
			resultSet = dbManager.executeQuery(query);
		} catch (SQLException e) {
			System.out.println(INSTANCE + ": Execute query failed");
			logger.logError(INSTANCE, "Execute query failed");
			throw e;
		} finally {
			if (dbManager != null) dbManager.close();
		}
		
		Map<String, Object> body = resultSetToMap(resultSet);
		return body;
	}
	
	private void changeData(String query) throws SQLException {
		int check;
		try {
			System.out.println("changeData started");
			dbManager.connect();
			check = dbManager.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(INSTANCE + ": Execute query failed");
			logger.logError(INSTANCE, "Execute query failed");
			throw e;
		} finally {
			if (dbManager != null) dbManager.close();
		}
	}
	
	private Map<String, Object> resultSetToMap(ResultSet resultSet) throws SQLException {
		Map<String, Object> bodyMap = new HashMap<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        int rowNumber = 0;

        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                rowMap.put(columnName, columnValue);
            }
            bodyMap.put(Integer.toString(rowNumber++), rowMap);
        }
        return bodyMap;
    }
	
	private void sqlException(SQLException e) {
		System.out.println(INSTANCE + ": Select data from DB failed");
		String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
		logger.logError(INSTANCE, "Select data from DB failed");
		logger.logError(INSTANCE, msg);
	}
}
