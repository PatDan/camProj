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
			case "auto":
				cm.changeMode(ClientMonitor.AUTO);
				break;
			case "idle":
				cm.changeMode(ClientMonitor.IDLE);
				break;
			case "movie":
				cm.changeMode(ClientMonitor.MOVIE);
				break;
			}
		}

	}

}
