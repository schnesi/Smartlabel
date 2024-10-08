package ch.schnes.smartlabel.client.control;

import ch.schnes.smartlabel.client.*;
import ch.schnes.smartlabel.client.mainmodel.ClientMainModel;
import ch.schnes.smartlabel.client.mainmodel.GetOrderModel;

import javax.swing.JPanel;

/**
 * The ClientController handle the communication with the MQTT broker and set the models to the view
 */
public class ClientController {
	private ClientMenuModel menu;
	private ClientView view;
	
	public ClientController(ClientView view) {
		this.view = view;
		menu = new ClientMenuModel(this);
	}
	
	public JPanel getMenu() {
		return menu.getPanel();
	}
	
	/**
	 * Get the GetOrderModel.
	 * 
	 * Noch pseudo Daten!!
	 */
	public void getOrder() {
		Object[][] data = new Object[][] {
			{"ClientID", "ClientID"},
			{"OrderID", 1},
			{"MaterialNO", "MaterialNO"},
			{"MaterialName", "MaterialName"},
			{"Storage", "Storage"},
			{"qty", 1}
		};
		ClientMainModel mainModel = new GetOrderModel(data, this);
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
}
