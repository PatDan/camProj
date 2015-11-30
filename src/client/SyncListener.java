package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class SyncListener implements ItemListener {
	private ClientMonitor cm;

	public SyncListener(ClientMonitor cm) {
		this.cm = cm;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			switch (((JCheckBox) e.getItem()).getText()) {
			case "synchronized":
				System.out.println("Nico luktar skit");
				cm.changeSync(ClientMonitor.SYNCHRONIZED);
				break;
			case "asynchronized":
				cm.changeSync(ClientMonitor.ASYNCHRONIZED);
				break;
			}
		}
	}
}