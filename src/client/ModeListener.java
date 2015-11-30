package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class ModeListener implements ItemListener {
	private ClientMonitor cm;

	public ModeListener(ClientMonitor cm) {
		this.cm = cm;
	}

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
