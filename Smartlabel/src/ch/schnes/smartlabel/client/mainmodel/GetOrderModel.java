package ch.schnes.smartlabel.client.mainmodel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ch.schnes.smartlabel.client.control.ClientController;

/**
 * Shows the and save the changed order data.
 */
public class GetOrderModel implements ClientMainModel {
	private JPanel panel;
	private JTable table;
	private ClientController controller;
	
	/**
	 * Create the panel
	 * @param data
	 * @param controller
	 */
	public GetOrderModel(Object[][] data, ClientController controller) {
		this.controller = controller;
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
				data, new String[] {
						"Key", "Value"
				}
		));
		panel.add(table, BorderLayout.CENTER);
		panel.setVisible(true);
		
		JButton bSave = new JButton("Save Data");
		bSave.addActionListener(e -> {
			Object[][] tableData = getTableData();
			controller.sendOrderData(tableData);
		});
		panel.add(bSave, "South");
	}
	
	/**
	 * Save and send the table data to the controller.
	 * @return
	 */
	private Object[][] getTableData() {
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		
		Object[][] tableData = new Object[rowCount][columnCount];
		for (int row=0; row<rowCount; row++) {
			for (int col=0; col<columnCount; col++) {
				tableData[row][col] = table.getValueAt(row, col);
			}
		}
		return tableData;
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}

}
