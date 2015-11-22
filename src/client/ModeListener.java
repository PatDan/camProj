package client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ModeListener implements ItemListener{
	private ClientMonitor cm;
	public ModeListener(ClientMonitor cm){
		this.cm = cm;
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		switch (e.getItem().toString()){
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
