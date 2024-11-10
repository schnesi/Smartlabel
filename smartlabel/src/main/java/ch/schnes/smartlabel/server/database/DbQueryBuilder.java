package ch.schnes.smartlabel.server.database;

public class DbQueryBuilder {
	public static String selectSmartlabelData(Object serial) {
		return "SELECT SmartlabelSN, material.MaterialNo, material.materialname, \r\n"
				+ "	storageProduction, Sensor, MeasuermentPeriode, orders.orderstate, Delivery.DeliveryDate\r\n"
				+ "FROM Smartlabel \r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "LEFT OUTER JOIN Orders ON Orders.MaterialNo = Material.MaterialNo\r\n"
				+ "LEFT OUTER JOIN delivery ON Delivery.DeliveryID = Orders.DeliveryID\r\n"
				+ "WHERE Smartlabel.SmartlabelSN = '" + serial + "';";
	}
	
	public static String selectOrder() {
		return "SELECT OrderID, Orders.MaterialNo, Material.MaterialName, Storage.Storage, \r\n"
				+ "	Smartlabel.storageProduction, Orders.Quantity \r\n"
				+ "FROM Orders\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Orders.MaterialNo\r\n"
				+ "LEFT OUTER JOIN Storage ON Storage.MaterialNo = Material.MaterialNo\r\n"
				+ "LEFT OUTER JOIN Smartlabel ON Smartlabel.SmartlabelID = Orders.SmartlabelID;";
	}
	
	public static String selectDataMaterial_Mat(String mat) {
		return "SELECT Material.MaterialNo, Material.MaterialName, Material.Weight, Storage.Storage, Storage.quantity\r\n"
				+ "FROM Material\r\n"
				+ "LEFT OUTER JOIN Storage ON Storage.MaterialNo = Material.MaterialNo\r\n"
				+ "WHERE Material.MaterialNo = '" + mat + "'"
				+ "ORDER BY Storage.Date DESC LIMIT 1;";
	}
	
	public static String selectDataMaterial_Stor(String storage) {
		return "SELECT Material.MaterialNo, Material.MaterialName, Material.Weight, Storage.Storage, Storage.quantity\r\n"
				+ "FROM Material\r\n"
				+ "LEFT OUTER JOIN Storage ON Storage.MaterialNo = Material.MaterialNo\r\n"
				+ "WHERE Storage.Storage = '" + storage + "'"
				+ "ORDER BY Storage.Date DESC LIMIT 1";
	}
	
	public static String selectAllDelivery() {
		return "SELECT Delivery.MaterialNo, Material.MaterialName, Delivery.Quantity,\r\n"
				+ "	Delivery.DeliveryDate, Delivery.DeliveryState\r\n"
				+ "FROM delivery\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Delivery.MaterialNo;";
	}
	
	public static String selectDelivery(String mat) {
		return "SELECT Delivery.MaterialNo, Material.MaterialName, Delivery.Quantity,\r\n"
				+ "	Delivery.DeliveryDate, Delivery.DeliveryState\r\n"
				+ "FROM delivery\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Delivery.MaterialNo\r\n"
				+ "WHERE Delivery.MaterialNo = '" + mat + "';";
	}
	
	public static String selectStock_Mat(String mat) {
		return "SELECT Smartlabel.SmartlabelSN, Smartlabel.MaterialNo, Material.MaterialName,\r\n"
				+ "	Smartlabel.StorageProduction, Smartlabel.Quantity\r\n"
				+ "FROM Smartlabel\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "WHERE Smartlabel.MaterialNo = '" + mat + "'\r\n"
				+ "ORDER BY Smartlabel.Date DESC LIMIT 1;";
	}
	
	public static String selectStock_Stor(String storage) {
		return "-- SELECT query for stock correction request\r\n"
				+ "SELECT Smartlabel.SmartlabelSN, Smartlabel.MaterialNo, Material.MaterialName,\r\n"
				+ "	Smartlabel.StorageProduction, Smartlabel.Quantity\r\n"
				+ "FROM Smartlabel\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "WHERE Smartlabel.StorageProduction = '" + storage + "'\r\n"
				+ "ORDER BY Smartlabel.Date DESC LIMIT 1;";
	}
	
	public static String selectSmartlabelInit(Object sn) {
		return "SELECT Smartlabel.SmartlabelSN, Smartlabel.MaterialNo, Material.MaterialName, \r\n"
				+ "	Smartlabel.StorageProduction, Smartlabel.Quantity, Smartlabel.Active, \r\n"
				+ "	Smartlabel.Sensor, Smartlabel.MeasuermentPeriode\r\n"
				+ "FROM smartlabel\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "WHERE Smartlabel.SmartlabelSN = '" + sn + "'\r\n"
				+ "ORDER BY Smartlabel.Date DESC LIMIT 1;";
	}
	
	public static String selectOrderSmartlabel(Object id) {
		return "SELECT Orders.SmartlabelID AS order, Smartlabel.SmartlabelID, Smartlabel.OrderQty,\r\n"
				+ "	Smartlabel.MaterialNo, Delivery.DeliveryId\r\n"
				+ "FROM Smartlabel\r\n"
				+ "LEFT OUTER JOIN Orders ON Orders.SmartlabelID = Smartlabel.SmartlabelID\r\n"
				+ "LEFT OUTER JOIN Delivery ON Delivery.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "WHERE Smartlabel.SmartlabelSN = '" + id + "';";
	}
	
	public static String selectOrderRef(Object id) {
		return "SELECT quantity FROM Orders\r\n"
				+ "WHERE orderid = " + id + ";";
	}
	
	public static String selectOrderStorageRef(Object storage) {
		return "SELECT quantity FROM Storage\r\n"
				+ "WHERE storage = '" + storage + "';";
	}
	
	public static String selectOrderSlRef(Object stroage) {
		return "";
	}
	
	public static String updateOrder(Object id, Object qty) {
		return "Update orders\r\n"
				+ "SET quantity = " + qty + "\r\n"
				+ "WHERE orderid = " + id + ";";
	}
	
	public static String updateStorage(Object id, Object qty) {
		return "Update storage\r\n"
				+ "SET quantity = " + qty + "\r\n"
				+ "WHERE storage = " + id + ";";
	}
	
	public static String insertNewOrder(Object materialNo, Object smartlabelId, Object deliveryId, Object quantity) {
		return "INSERT INTO Orders (MaterialNo, SmartlabelID, DeliveryId, Quantity, Date, OrderState) VALUES\r\n"
				+ "('" + materialNo + "', "+ smartlabelId + ", " + deliveryId + ", " + quantity + ", CURRENT_DATE,\r\n"
				+ "	CASE\r\n"
				+ "		WHEN " + deliveryId + " IS NULL THEN 'scheduled'\r\n"
				+ "		ELSE 'backlog'\r\n"
				+ "	END\r\n"
				+ ");";
	}
}
