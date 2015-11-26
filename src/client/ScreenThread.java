package client;

public class ScreenThread extends Thread{
	private ClientMonitor cm;
	private int cam;
	
	public ScreenThread(ClientMonitor cm, int cam) {
		this.cm = cm;
		this.cam = cam;
	}
	
	public void run() {
		while(true) {
			System.out.println("Screen thread running");
			cm.updateScreen(cam);
		}
	}
}
