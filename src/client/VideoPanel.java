package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VideoPanel extends JPanel {
	private ImageIcon icon;
	private JLabel text;
	private JLabel text1;
	private long triggerdTime;
	private boolean triggered;

	public VideoPanel() {
		super();
		icon = new ImageIcon();
		JLabel label = new JLabel(icon);
		text1 = new JLabel();
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2,1));
		add(label, BorderLayout.CENTER);
		text = new JLabel();
		textPanel.add(text1);
		textPanel.add(text);
		textPanel.setBackground(Color.lightGray);
		add(textPanel, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(640, 520));
//		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		triggered = false;
	}

	public void refresh(Picture p) {
		Image image = getToolkit().createImage(p.getImage());
		getToolkit().prepareImage(image, -1, -1, null);
		icon.setImage(image);
		icon.paintIcon(this, this.getGraphics(), 0, 0);
		text1.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
		text.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
		text.setText(p.getDate() + "\t \t" + "Delay: " + (System.currentTimeMillis() - p.getTime()) + "ms");
		if(triggered && System.currentTimeMillis() - triggerdTime > 5000) {
			triggered = false;
			text1.setText("");
		}
		setBackground(Color.lightGray);
	}

	public void setTriggered() {
		triggered = true;
		text1.setText("Triggered");
		triggerdTime = System.currentTimeMillis();
	}

	
	
}
