package ch.schnes.smartlabel.client.control;

import ch.schnes.smartlabel.MQTTClient;
import ch.schnes.smartlabel.Observer;
import ch.schnes.smartlabel.client.*;
import ch.schnes.smartlabel.client.mainmodel.ClientMainModel;
import ch.schnes.smartlabel.client.mainmodel.OrderModel;

import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JPanel;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * The ClientController handle the communication with the MQTT broker and set the models to the view
 */
public class ClientController implements Observer {
	private ClientMenuModel menu;
	private ClientView view;
	private String brokerUrl, clientId, topicSubscribe;
	private Properties properties;
	private MQTTClient client;
	
	public ClientController(ClientView view) {
		try {
			this.view = view;
			menu = new ClientMenuModel(this);
			FileInputStream input = new FileInputStream("src/ch/schnes/smartlabel/client/config.properties");
			properties = new Properties();
			properties.load(input);
			brokerUrl = properties.getProperty("ClientController.brokerUrl");
			clientId = properties.getProperty("ClientController.clientId");
			topicSubscribe = properties.getProperty("ClientController.topic.receive");
			client = new MQTTClient(brokerUrl, clientId, topicSubscribe);
			client.addObserver(this);
		} catch (MqttException me) {
			System.out.println("reason "+me.getReasonCode());
	        System.out.println("msg "+me.getMessage());
	        System.out.println("loc "+me.getLocalizedMessage());
	        System.out.println("cause "+me.getCause());
	        System.out.println("excep "+me);
		} catch (Exception e) {
			System.out.println("Initializing of the Controller failed.");
	        System.out.println("msg "+e.getMessage());
	        System.out.println("loc "+e.getLocalizedMessage());
	        System.out.println("cause "+e.getCause());
	        System.out.println("excep "+e);
		}
	}
	
	public JPanel getMenu() {
		return menu.getPanel();
	}
	
	
	/**
	 * Get the GetOrder.
	 * 
	 * Versuchsaufbau
	 */
	public void getOrder() {
		try {
			String message = "Message";
			String topic = properties.getProperty("ClientController.topic.publish"); 
			client.publish(message, topic, 2);
		} catch (MqttException me) {
			System.out.println("reason "+me.getReasonCode());
	        System.out.println("msg "+me.getMessage());
	        System.out.println("loc "+me.getLocalizedMessage());
	        System.out.println("cause "+me.getCause());
	        System.out.println("excep "+me);
		} catch (Exception e) {
			System.out.println("Initializing of the Controller failed.");
	        System.out.println("msg "+e.getMessage());
	        System.out.println("loc "+e.getLocalizedMessage());
	        System.out.println("cause "+e.getCause());
	        System.out.println("excep "+e);
		}
	}
	
	private void showOrder() {
		Object[][] data = new Object[][] {
			{"ClientID", "ClientID"},
			{"OrderID", 1},
			{"MaterialNO", "MaterialNO"},
			{"MaterialName", "MaterialName"},
			{"Storage", "Storage"},
			{"qty", 1}
		};
		ClientMainModel mainModel = new OrderModel(data, this);
		view.setMainView(mainModel.getPanel());
	}
	
	/**
	 * Send the order data to the MQTT broker.
	 * @param data
	 * 
	 * Noch nicht implementiert
	 */
	public void sendOrderData(Object[][] data) {
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
	}

	@Override
	public void update(String topic, Object message) {
		// TODO Auto-generated method stub
		
	}
}
