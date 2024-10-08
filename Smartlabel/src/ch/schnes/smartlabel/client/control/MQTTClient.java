package ch.schnes.smartlabel.client.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MqttClient {
	private String brokerURL;
	private String topic;
	private String clientId;
	private MqttClient mqttClient;
	
	public MqttClient() throws IOException {
		loadConfig();
		// connect();
	}
	
	private void loadConfig() throws IOException {
		Properties properties = new Properties();
		FileInputStream input = new FileInputStream("");
		properties.load(input);
		brokerURL = properties.getProperty("mqttClient.brokerURL");
		System.out.println(brokerURL);
	}
}
