package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AutoListener implements ItemListener {
	private ClientMonitor cm;
	
	public AutoListener(ClientMonitor cm) {
		this.cm = cm;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			cm.setAuto(true);
		}else{
			cm.setAuto(false);
		}
	}

}
