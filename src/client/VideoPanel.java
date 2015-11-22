package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VideoPanel extends JPanel{
	 ImageIcon icon;

	    public VideoPanel() {
	      super();
	      icon = new ImageIcon();
	      JLabel label = new JLabel(icon);
	      add(label, BorderLayout.CENTER);
	      setPreferredSize(new Dimension(640,480));
	      setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
	    }

	    public void refresh(byte[] jpeg) {
	      Image image = getToolkit().createImage(jpeg);
	      getToolkit().prepareImage(image,-1,-1,null);
	      icon.setImage(image);
	      icon.paintIcon(this, this.getGraphics(), 0, 0);
	    }
}
