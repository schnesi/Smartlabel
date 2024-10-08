package ch.schnes.smartlabel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MQTTClient implements MqttCallback {
	private MqttClient client;
	private List<Observer> observers;
	
	/**
	 * Constructor connect to the broker and subscribe for the Topic for the Client.
	 * @param brokerUrl
	 * @param cliendId
	 * @param topic
	 * @throws IOException
	 * @throws MqttException
	 */
	public MQTTClient(String brokerUrl, String cliendId, String topic) throws IOException, MqttException {
		observers = new ArrayList<>();
		connect(brokerUrl, cliendId , topic);
	}
	
	/**
	 * Add an observer.
	 * @param observer
	 */
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	/**
	 * Remove an observer.
	 * @param observer
	 */
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	/**
	 * Notify the observers
	 * @param topic
	 * @param message
	 */
	private void notifyObservers(String topic, Object message) {
		for (Observer observer : observers) {
			observer.update(topic, message);
		}
	}
	
	/**
	 * Connect with the broker.
	 * @param brokerUrl
	 * @param clientId
	 * @param topic
	 * @throws IOException
	 * @throws MqttException
	 */
	private void connect(String brokerUrl, String clientId, String topic) throws IOException, MqttException {
		client = new MqttClient(brokerUrl, clientId);
		client.setCallback(this);
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		client.connect(options);
		System.out.println("Connected to broker: " + brokerUrl);
		client.subscribe(topic);
		System.out.println("Subscrebed topic: " + topic);
	}
		
	/**
	 * Publish a message.
	 * @param message
	 * @param topic
	 * @param qos		QoS from Level 0 to 2
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void publish(String message, String topic, int qos) throws MqttPersistenceException, MqttException {
		MqttMessage msg = new MqttMessage(message.getBytes());
		msg.setQos(qos);
		client.publish(topic, msg);
		System.out.println("Message published: " + msg);
	}
	
	/**
	 * Disconnect from broker.
	 * @throws MqttException
	 */
	public void disconnect() throws MqttException {
		client.disconnect();
		System.out.println("Disconnected");
		notifyObservers("disconnected", null);
	}
		
	/**
	 * Notify observer if connection lost.
	 */
	@Override
	public void connectionLost(Throwable arg0) {
		notifyObservers("connectionLost", null);
	}

	/**
	 * Notify observer if delivery complete.
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg) {
		notifyObservers("delivery complete", arg);
		
	}

	/**
	 * Send the observers the message.
	 */
	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		String message = new String(msg.getPayload());
		System.out.println("Message arrived: " + message);
		notifyObservers(topic, message);
	}
}
