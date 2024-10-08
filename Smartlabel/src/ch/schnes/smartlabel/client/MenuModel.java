package ch.schnes.smartlabel.client;

import ch.schnes.smartlabel.client.control.ClientController;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuModel extends JPanel {
	private JButton bGetOrder, bEditMaterial, bEditDelivery, bRemoveMaterial;

	MenuModel(ClientController controller) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		bGetOrder = new JButton("Get orders");
		bGetOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
		bGetOrder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		bGetOrder.addActionListener(e -> {
			controller.getOrder();
		});
		
		add(bGetOrder);
		
		bEditMaterial = new JButton("Get orders");
		bEditMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
		bEditMaterial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		add(bEditMaterial);
		
		bEditDelivery = new JButton("Get orders");
		bEditDelivery.setAlignmentX(Component.CENTER_ALIGNMENT);
		bEditDelivery.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		add(bEditDelivery);
		
		bRemoveMaterial = new JButton("Get orders");
		bRemoveMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
		bRemoveMaterial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		add(bRemoveMaterial);
	}
}
