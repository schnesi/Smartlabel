package ch.schnes.smartlabel.client.mainmodel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shows a loading message.
 */
public class LoadingModel implements ClientMainModel {
	private JLabel label;
	private JPanel panel;
	
	public LoadingModel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		label = new JLabel("Loading.. Please wait");
		panel.add(label, BorderLayout.NORTH);
	}
	
	/**
	 * Return the Panel.
	 */
	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return panel;
	}

}
