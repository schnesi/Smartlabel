package ch.schnes.smartlabel.server;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ServerMain {
	private final static Logger logger = Logger.getInstance();
	private final static String INSTANCE = "ServerMain";
	private static Thread dataService;
	
	public static void main(String[] args) {
		logger.logInfo(INSTANCE, "Start Server");
		try {
			dataService = new Thread(new DataService());
		} catch (IOException e) {
			System.out.println("ServerMain: Failed read the config.properties");
			String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Failed read the config.properties");
			logger.logError(INSTANCE, msg);
		} catch (MqttException e) {
			System.out.println("ServerMain: Failed to initialize the MqttHandler");
			String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "ServerMain: Failed to initialize the MqttHandler");
			logger.logError(INSTANCE, msg);
		}
		
		dataService.start();
	}
}