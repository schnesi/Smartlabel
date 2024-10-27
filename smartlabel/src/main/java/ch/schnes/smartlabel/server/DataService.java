package ch.schnes.smartlabel.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.MqttException;
import com.fasterxml.jackson.core.JsonProcessingException;

import ch.schnes.smartlabel.JsonHandler;
import ch.schnes.smartlabel.MqttHandler;
import ch.schnes.smartlabel.Observer;
import ch.schnes.smartlabel.server.database.DataRepository;

public class DataService implements Observer, DataServiceInterface {
	private final Logger logger = Logger.getInstance();
	private final String INSTANCE = "DataService";
	private final ExecutorService executorService;
	private boolean running;
	MqttHandler client;
	Properties properties;
	
	public DataService() throws IOException, MqttException {
		this.executorService = Executors.newCachedThreadPool();
		FileInputStream input = new FileInputStream("src/main/resources/config.properties");
		properties = new Properties();
		properties.load(input);
		String brokerUrl = properties.getProperty("DataService.mqtt.brokerUrl");
		String clientId = properties.getProperty("DataService.mqtt.clientId");
		String topicSub = properties.getProperty("DataService.mqtt.topic.receive");
		client = new MqttHandler(brokerUrl, clientId, topicSub);
		client.addObserver(this);
		logger.logInfo(INSTANCE, "DataService and MQTT-Client initialized");
	}


	public void publish(String topic, Map<String, Object> msg) {
		String message;
		try {
			message = JsonHandler.serialize(msg);
			client.publish(message, topic, 2);
		} catch (JsonProcessingException e) {
			System.out.println("DataService: Failed to serialize the map");
			String errorMsg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Failed to serialize the map");
			logger.logError(INSTANCE, errorMsg);
		} catch (MqttException e) {
			System.out.println("DataService: Failed to send the MQTT-message");
			String errorMsg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Failed to send the MQTT-message");
			logger.logError(INSTANCE, errorMsg);
		}
	}



	@Override
	public void update(String topic, String message) {
		try {
			Map<String, Object> json = JsonHandler.deserialize(message);
			DataRepository dataRepository = new DataRepository(json, this);
			executorService.submit(dataRepository);
		} catch (JsonProcessingException e) {
			System.out.println("DataService: Failed to deserialize the MQTT-message");
			String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Failed to deserialize the MQTT-message");
			logger.logError(INSTANCE, msg);
		} catch (IOException e) {
			System.out.println("DataService: Failed to initialize the DataRepository");
			String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
			logger.logError(INSTANCE, "Failed to initialize the DataRepository");
			logger.logError(INSTANCE, msg);
		}	
	}

	public void stopThread() {
		running = false;
		System.out.println("DataService: Stopped");
		logger.logInfo(INSTANCE, "Stopped");
	}

	@Override
	public void update(String message) {
				
	}

	@Override
	public void run() {
		running = true;
		while (!Thread.currentThread().isInterrupted() && running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("DataService: Is interrupted");
				String msg = "msg: " + e.getMessage() + ", loc: " + e.getLocalizedMessage() + ", cause: " + e.getCause() + ", excep: " + e;
				logger.logError(INSTANCE, "Is interrupted");
				logger.logError(INSTANCE, msg);
			}
		}
		
	}
}
