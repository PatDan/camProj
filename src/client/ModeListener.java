package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
/**
 * Listener for the Movie and idle buttons in the GUI. This changes a mode in the monitor
 */
public class ModeListener implements ItemListener {
	private ClientMonitor cm;
	/**
	 * Constructor for the Modelistener where it needs a ClientMonitor as input to be able to affect the
	 * modes in the monitor
	 * @param cm
	 *       - ClientMonitor from FrameGUI
	 */
	public ModeListener(ClientMonitor cm) {
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
			switch (((JCheckBox) e.getItem()).getText()) {
			case "idle":
				cm.changeVideoMode(ClientMonitor.IDLE);
				break;
			case "movie":
				cm.changeVideoMode(ClientMonitor.MOVIE);
				break;
			}
		}

	}

}
