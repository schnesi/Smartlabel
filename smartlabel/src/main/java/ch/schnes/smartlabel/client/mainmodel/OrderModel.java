package ch.schnes.smartlabel.client.mainmodel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ch.schnes.smartlabel.client.control.ClientController;

/**
 * Shows the and save the changed order data.
 */
public class OrderModel implements ClientMainModel {
	private ClientController controller;
	private JPanel panel;
	
	public OrderModel(Map<String, Object> data, ClientController controller) {
		this.controller = controller;
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// Durchlaufen der nummerierten Einträgen
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			try {
				Integer.parseInt(entry.getKey());	// Nur numerische Schlüssel verarbeiten
				Map<String, Object> entryMap = (Map<String, Object>) entry.getValue();
				
				// Panel für jeden Eintrag mit Table und Button
				JPanel entryPanel = new JPanel(new BorderLayout());
				
				// Tabelle für den aktuellen Eintrag erstellen
                DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Key", "Value"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        // Nur die "Value"-Zelle in der "delivered"-Zeile ist bearbeitbar
                        return getValueAt(row, 0).equals("delivered") && column == 1;
                    }
                };
				
				JTable table = new JTable(tableModel);
				
				for (Map.Entry<String, Object> innerEntry : entryMap.entrySet()) {
					tableModel.addRow(new Object[] {innerEntry.getKey(), innerEntry.getValue()});
				}
				
				// Zusätzlice Zeile für "delivered" einfügen
				tableModel.addRow(new Object[] {"delivered", ""});
				entryPanel.add(table, BorderLayout.CENTER);
				
				
				
				
				// JButton hinzufügen (Logik noch nicht vorhanden)
				JButton button = new JButton("Save order " + entry.getKey());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {						
						Map<String, Object> tableData = extractTableData(table);
						controller.sendOrderData(tableData);
					}
					
				});
				entryPanel.add(button, BorderLayout.SOUTH);
				
				panel.add(entryPanel);
			} catch (NumberFormatException e) {
				// Ignoriere, falls der Schlüssel keine Zahl ist
			}
		}
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	private Map<String, Object> extractTableData(JTable table) {
		Map<String, Object> extractedData = new HashMap<>();
        int rowCount = table.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            String key = table.getValueAt(row, 0).toString();
            Object value = table.getValueAt(row, 1);

            if ("orderid".equals(key)) {
                extractedData.put("orderid", value);
            } else if ("delivered".equals(key)) {
                extractedData.put("qty", value);
            } else if ("storage".equals(key)) {
            	extractedData.put("storage", value);
            } else if ("storageproduction".equals(key)) {
            	extractedData.put("storageproduction", value);
            }
        }
        return extractedData;
	}
}