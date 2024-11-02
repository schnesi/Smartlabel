package ch.schnes.smartlabel.client.mainmodel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ch.schnes.smartlabel.client.control.ClientController;

public class OrderTestPanel implements ClientMainModel {
	ClientController controller;
	private JPanel panel;
	
	public OrderTestPanel(Map<String, Object> data, ClientController controller) {
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
						System.out.println("Table data for order " + entry.getKey() + ": " + tableData);
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
		Map<String, Object> dataMap = new HashMap<>();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int row = 0; row < model.getRowCount(); row++) {
			String key = model.getValueAt(row, 0).toString();
			Object value = model.getValueAt(row, 1);
			dataMap.put(key, value);
		}
		return dataMap;
	}
}
