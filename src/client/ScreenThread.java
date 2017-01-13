package client;

public class ScreenThread extends Thread {
	private ClientMonitor cm;
	private int cam;

	/**
	 * Updates the screen in the GUI
	 * 
	 * @param cm
	 *            - the client monitor containing the image buffers
	 * @param cam
	 *            - the number of the video panel to be updated, also the number
	 *            of the buffer to fetch images from
	 */
	public ScreenThread(ClientMonitor cm, int cam) {
		this.cm = cm;
		this.cam = cam;
	}

	/**
	 * Uses the client monitors method updateScreen(cam) to update the screen
	 */
	public void run() {
		while (true) {
			cm.updateScreen(cam);
		}
	}
}
