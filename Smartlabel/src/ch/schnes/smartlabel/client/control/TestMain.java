package ch.schnes.smartlabel.client.control;

import java.io.IOException;

public class TestMain {

	public static void main(String[] args) {
		try {
			MqttClient client = new MqttClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
