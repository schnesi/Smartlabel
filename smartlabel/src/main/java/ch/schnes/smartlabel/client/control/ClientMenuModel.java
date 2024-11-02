package ch.schnes.smartlabel.client.control;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Get the menu buttons to chose the main content.
 */
public class ClientMenuModel extends JPanel {
	private JButton bGetOrder, bEditMaterial, bEditDelivery, bRemoveMaterial, bEditSmartlabel;
	private JPanel panel;
	
	public ClientMenuModel(ClientController controller) {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		bGetOrder = new JButton("Get orders");
		bGetOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
		bGetOrder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		bGetOrder.addActionListener(e -> {
			controller.getOrder();
		});
		panel.add(bGetOrder);
		
		bEditMaterial = new JButton("Material");
		bEditMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
		bEditMaterial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(bEditMaterial);
		
		bEditDelivery = new JButton("Delivery");
		bEditDelivery.setAlignmentX(Component.CENTER_ALIGNMENT);
		bEditDelivery.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(bEditDelivery);
		
		bRemoveMaterial = new JButton("Remove Stock");
		bRemoveMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
		bRemoveMaterial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(bRemoveMaterial);
		
		bEditSmartlabel = new JButton("Edit Smartlabel");
		bEditSmartlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		bEditSmartlabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.add(bEditSmartlabel);
	}
	
	public JPanel getPanel() {
		return panel;
	}
}