package ch.schnes.smartlabel.server.database;

public class DbQueryBuilder {
	public static String selectSmartlabelData(String serial) {
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
	
	public static String selectSmartlabelInit(String mat) {
		return "SELECT Smartlabel.SmartlabelSN, Smartlabel.MaterialNo, Material.MaterialName, \r\n"
				+ "	Smartlabel.StorageProduction, Smartlabel.Quantity, Smartlabel.Active, \r\n"
				+ "	Smartlabel.Sensor, Smartlabel.MeasuermentPeriode\r\n"
				+ "FROM smartlabel\r\n"
				+ "LEFT OUTER JOIN Material ON Material.MaterialNo = Smartlabel.MaterialNo\r\n"
				+ "WHERE Smartlabel.SmartlabelSN = '" + mat + "'\r\n"
				+ "ORDER BY Smartlabel.Date DESC LIMIT 1;";
	}
}
