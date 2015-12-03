package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
/**
 * Listener for the Autobutton in the GUI. This changes a mode in the monitor
 */
public class AutoListener implements ItemListener {
	private ClientMonitor cm;
	
	
	/**
	 * Constructor for the Autolistener where it needs a clientmonitor as input to be able to affect the
	 * modes in the monitor
	 * @param cm
	 *       - ClientMonitor from FrameGUI
	 */
	public AutoListener(ClientMonitor cm) {
		this.cm = cm;
	}

	/**
	 * This is a method that describes what happens when the button is pressed.
	 * @param e
	 * 		- the item event from FrameGUI
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			cm.setAuto(true);
		}else{
			cm.setAuto(false);
		}
	}

}
