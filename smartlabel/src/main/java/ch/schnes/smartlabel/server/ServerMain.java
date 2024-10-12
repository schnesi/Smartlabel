package ch.schnes.smartlabel.server;

import java.io.IOException;

import ch.schnes.smartlabel.server.database.DatabaseManager;

public class ServerMain {

	public static void main(String[] args) {
		try {
			DatabaseManager db = new DatabaseManager();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}