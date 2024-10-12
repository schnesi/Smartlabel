package ch.schnes.smartlabel.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;

import ch.schnes.smartlabel.client.control.ClientController;

public class ClientView extends JFrame {
	private ClientController controller = new ClientController(this);
	private JScrollPane spMenu, spMainView;
	
	private static final long serialVersionUID = 1L;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView frame = new ClientView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public ClientView() {
		setTitle("User Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 854, 480);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		
		JPanel menuPanel = controller.getMenu();
		spMenu = new JScrollPane(menuPanel);
		spMainView = new JScrollPane();
		spMainView.setLayout(new ScrollPaneLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spMenu, spMainView);
		splitPane.setDividerLocation(0.2);
		splitPane.setResizeWeight(0.2);
		getContentPane().add(splitPane);
	}
	
	/**
	 * Set the mainPanel.
	 * @param mainPanel
	 */
	public void setMainView(JPanel mainPanel) {
		SwingUtilities.invokeLater(() -> {
			spMainView.setViewportView(mainPanel);
			spMainView.revalidate();
		});
	}
}