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
		if ("request".equals(header)) {
			String item = (String) data.get("item");
			System.out.println("DataRepository: header: " + header + ", item: " + item);
			logger.logInfo(INSTANCE, "orderData requestetd");
			if ("order".equals(item)) {
				logger.logInfo(INSTANCE, "Order data requested");
				body = requestOrder();
				logger.logInfo(INSTANCE, "Request order data successful");
			}
			
			String topic = "/smartlabel/client/" + data.get("clientID");
			body.put("clientID", data.get("clientID"));
			System.out.print("DataRepository: Execute query successful: ");
			System.out.println(body);
			Map<String, Object> answer = new HashMap<>();
			answer.put("header", "responseOrder");
			answer.put("data", body);
			dataService.publish(topic, answer);
		}
	}
	
	private Map<String, Object> requestOrder() {
		Map<String, Object> body = null;
		try {
			body = selectData(DbQueryBuilder.selectOrder());
			logger.logInfo(INSTANCE, "Select Data from DB successful");
		} catch (SQLException e) {
			System.out.println(INSTANCE + ": Select data from DB failed");
			String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Select data from DB failed");
			logger.logError(INSTANCE, msg);
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
	
	private void insertData() {
		
	}
	
	private void deleteData() {
		
	}
	
	private void updateData() {
		
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
}
