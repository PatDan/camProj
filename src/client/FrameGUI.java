package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class FrameGUI extends JFrame {
	private String[] syncBoxes = { "auto", "synchronized", "asynchronized" };
	private String[] movieBoxes = { "auto", "movie", "idle" };

	JPanel jui = new JPanel(new GridLayout(2,1));
	JPanel vui = new JPanel(new GridLayout(1,2));
	private JCheckBox syncCheck[];
	private JCheckBox movieCheck[];	
	private ButtonGroup bgSync = new ButtonGroup();
	private ButtonGroup bgMovie = new ButtonGroup();
	private VideoPanel videoFrame1 = new VideoPanel();
	private VideoPanel videoFrame2 = new VideoPanel();

	public FrameGUI() {
		jui.setPreferredSize(new Dimension(20,40));
		jui.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setLayout(new GridBagLayout());
		add(vui);
		add(jui);
	
		syncCheck = new JCheckBox[syncBoxes.length];
		movieCheck = new JCheckBox[movieBoxes.length];

		vui.add(videoFrame1);
		vui.add(videoFrame2);
		
		for (int i = 0; i < syncBoxes.length; i++) {
            createCheckBoxes(i, syncBoxes, syncCheck, bgSync);
        }
		for (int i = 0; i < movieBoxes.length; i++) {
            createCheckBoxes(i, movieBoxes, movieCheck, bgMovie);
        }

		
		setTitle("Camera Viewer");
		pack();
		setSize(1300, 1000);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
	public void createCheckBoxes(int i, String [] names, JCheckBox boxes[], ButtonGroup bg){
	  boxes[i] = new JCheckBox(names[i]);
      boxes[i].setBorder(new EmptyBorder(0,i*30,0,0));
      bg.add(boxes[i]);
      jui.add(boxes[i]);
	}
	 public static void main(String[] args) {
	        Runnable r = new Runnable() {
	            public void run() {
	                JFrame frame = new FrameGUI();
	            }
	        };
	        SwingUtilities.invokeLater(r);
	    }

}
