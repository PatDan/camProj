package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VideoPanel extends JPanel{
	 private ImageIcon icon;
	 private JLabel text;
	 private boolean blinked;

	    public VideoPanel() {
	      super();
	      blinked = false;
	      icon = new ImageIcon();
	      JLabel label = new JLabel(icon);
	      add(label, BorderLayout.CENTER);
	      text = new JLabel();
	      add(text, BorderLayout.SOUTH);
	      setPreferredSize(new Dimension(640,520));
	      setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
	    }

	    public void refresh(Picture p) {
	      Image image = getToolkit().createImage(p.getImage());
	      getToolkit().prepareImage(image,-1,-1,null);
	      icon.setImage(image);
	      icon.paintIcon(this, this.getGraphics(), 0, 0);
	      text.setFont(new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 15));
	      text.setText(p.getDate());
	      setBackground(Color.lightGray);
	    }
	    public void borderBlink() {
	    	setBorder(blinked ? BorderFactory.createLineBorder(Color.BLACK) : BorderFactory.createLineBorder(Color.RED));
	    	blinked = !blinked;
	    }
}
