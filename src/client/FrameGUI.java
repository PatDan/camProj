package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class FrameGUI extends JFrame {
	private String[] syncBoxes = { "synchronized", "asynchronized" };
	private String[] movieBoxes = { "idle", "movie" };
	

	JPanel jui = new JPanel(new GridLayout(1, 2));
	JPanel vui = new JPanel(new GridLayout(1, 2));
	private JCheckBox syncCheck[];
	private JCheckBox movieCheck[];
	private JCheckBox autoCheck;
	
	private ButtonGroup bgSync = new ButtonGroup();
	private ButtonGroup bgMovie = new ButtonGroup();
	private VideoPanel videoFrame1 = new VideoPanel();
	private VideoPanel videoFrame2 = new VideoPanel();
	private boolean isMode;
	private ClientMonitor cm;
	/**
	 * This constructor draws up a combination of checkboxes and VideoPanels, and needs to recieve a clientmonitor as input
	 * in order to create the necessary listeners for the checkboxes.
	 * @param cm (ClientMonitor
	 */
	public FrameGUI(ClientMonitor cm) {
		this.cm = cm;
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
		jui.setPreferredSize(new Dimension(1280, 50));
		jui.setLayout(new GridLayout(1, 7));
		jui.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setLayout(new BorderLayout(0, 2));
		syncCheck = new JCheckBox[syncBoxes.length];
		movieCheck = new JCheckBox[movieBoxes.length];
		jui.setBackground(Color.gray);
		vui.add(videoFrame1);
		vui.add(videoFrame2);
		vui.setPreferredSize(new Dimension(1280, 540));

		jui.add(new JLabel("Sync mode"));

		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 12));
		
		autoCheck = new JCheckBox("auto");
		autoCheck.setBorder(new EmptyBorder(0, 0, 0, 0));
		autoCheck.setBackground(Color.gray);
		autoCheck.setSelected(true);
		autoCheck.addItemListener(new AutoListener(cm));
		jui.add(autoCheck);
		
		
		isMode = true;
		for (int i = 0; i < syncBoxes.length; i++) {
			createCheckBoxes(i, syncBoxes, syncCheck, bgSync);
		}

		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
		jui.add(new JLabel("Movie mode"));

		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 12));
		isMode = false;
		for (int i = 0; i < movieBoxes.length; i++) {
			createCheckBoxes(i, movieBoxes, movieCheck, bgMovie);
		}

		add(vui, BorderLayout.NORTH);

		add(jui, BorderLayout.SOUTH);

		setTitle("Camera Viewer");
		pack();
		setSize(1280, 620);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	/**
	 * Method to create different fonts in the GUI. The desired font is the input for this method.
	 * @param f 
	 */
	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}
	/**
	 * A private method used when creating the checkboxes and placing them in corresponding button groups. Recieves the amount of
	 * boxes, the names of the boxes in the order they need to be created, the corresponding box vector that the are to be placed in, and lastly
	 * the corresponding buttongroup that they adhere to.
	 * @param i
	 * @param names
	 * @param boxes
	 * @param bg
	 */
	private void createCheckBoxes(int i, String[] names, JCheckBox boxes[], ButtonGroup bg) {
		boxes[i] = new JCheckBox(names[i]);
		boxes[i].setBorder(new EmptyBorder(0, 0, 0, 0));
		boxes[i].setBackground(Color.gray);
		boxes[i].setSelected(i == 0 ? true : false);
		boxes[i].addItemListener(isMode ? new SyncListener(cm) : new ModeListener(cm));
		bg.add(boxes[i]);
		jui.add(boxes[i]);
	}
	/**
	 * This is a method used by the monitor to update the mode, thus changing which of the checkboxes is highlighted. Input is the
	 * selected checkbox to highlight.
	 * @param mode
	 */
	public void updateMode(int mode) {
		movieCheck[mode - 1].setSelected(true);
	}
	/**
	 * A method to indicate which camera caused the recent change in modes. Input is the panel that is specified to blink.
	 * @param panel
	 */
	public void activeCamera(int panel) {
		switch (panel) {
		case (1):
			videoFrame1.setTriggered();
			break;
		case (2):
			videoFrame2.setTriggered();
			break;
		}
	}
	/**
	 * A method to send images to the corresponding VideoPanel and update the gui. The inputs are the picture that is to be displayed and 
	 * the frame it is to be displayed on.
	 * @param p
	 * @param frame
	 */
	public void sendImage(Picture p, int frame) {
		switch (frame) {
		case 1:
			videoFrame1.refresh(p);
			break;
		case 2:
			videoFrame2.refresh(p);
			break;
		}
	}
	public void changeSyncMode(int mode) {
		syncCheck[mode - 1].setSelected(true);	
	}
}
