package client;

public class ScreenThread extends Thread{
	private ClientMonitor cm;
	private int panel;
	
	public ScreenThread(ClientMonitor cm, int panel) {
		this.cm = cm;
		this.panel = panel;
	}
	
	public void run() {
		while(true) {
			cm.updateScreen(panel);
		}
	}
}
