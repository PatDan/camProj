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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import server.ServerMonitor;

public class FrameGUI extends JFrame {
	private String[] syncBoxes = { "synchronized", "asynchronized" };
	private String[] movieBoxes = { "idle", "movie" };

	JPanel jui = new JPanel(new GridLayout(1, 2));
	JPanel vui = new JPanel(new GridLayout(1, 2));
	private JCheckBox syncCheck[];
	private JCheckBox movieCheck[];
	private ButtonGroup bgSync = new ButtonGroup();
	private ButtonGroup bgMovie = new ButtonGroup();
	private VideoPanel videoFrame1 = new VideoPanel();
	private VideoPanel videoFrame2 = new VideoPanel();
	private boolean isMode;
	private ClientMonitor cm;

	public FrameGUI(ClientMonitor cm) {
		this.cm = cm;
		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
		jui.setPreferredSize(new Dimension(1280, 50));
		jui.setLayout(new GridLayout(1, 6));
		jui.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setLayout(new BorderLayout(0, 2));
		syncCheck = new JCheckBox[syncBoxes.length];
		movieCheck = new JCheckBox[movieBoxes.length];
		jui.setBackground(Color.gray);
		vui.add(videoFrame1);
		vui.add(videoFrame2);
		vui.setPreferredSize(new Dimension(1280, 520));

		jui.add(new JLabel("Sync mode"));

		setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 12));
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
		setSize(1280, 600);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	private void createCheckBoxes(int i, String[] names, JCheckBox boxes[], ButtonGroup bg) {
		boxes[i] = new JCheckBox(names[i]);
		boxes[i].setBorder(new EmptyBorder(0, 0, 0, 0));
		boxes[i].setBackground(Color.gray);
		boxes[i].addItemListener(isMode ? new SyncListener(cm) : new ModeListener(cm));
		boxes[i].setSelected(i == 0 ? true : false);
		bg.add(boxes[i]);
		jui.add(boxes[i]);
	}

	public void updateMode(int mode) {
		System.out.println("Updating GUI mode" + mode);
		movieCheck[mode - 1].setSelected(true);
	}

	public void activeCamera(int panel) {
		switch (panel) {
		case (1):
			videoFrame1.borderBlink();
			break;
		case (2):
			videoFrame2.borderBlink();
			break;
		}
	}

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

	public static void main(String[] args) {

		Runnable r = new Runnable() {
			public void run() {
				ServerMonitor sm = new ServerMonitor();
				ServerMonitor sm2 = new ServerMonitor();
				ClientMonitor am = new ClientMonitor();

			}
		};
		SwingUtilities.invokeLater(r);
	}

}
