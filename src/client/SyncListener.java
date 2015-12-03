package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
/**
 * Listener for the synchronize and asynchronize buttons in the GUI. This changes a mode in the monitor
 */
public class SyncListener implements ItemListener {
	private ClientMonitor cm;
	/**
	 * Constructor for the Synclistener where it needs a ClientMonitor as input to be able to affect the
	 * modes in the monitor
	 * @param cm
	 *       - ClientMonitor from FrameGUI
	 */
	public SyncListener(ClientMonitor cm) {
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
			case "synchronized":
				cm.forceSync(ClientMonitor.SYNCHRONIZED);
				break;
			case "asynchronized":
				cm.forceSync(ClientMonitor.ASYNCHRONIZED);
				break;
			}
		}
	}
}