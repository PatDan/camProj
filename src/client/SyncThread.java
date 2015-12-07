package client;

public class SyncThread extends Thread {
	private ClientMonitor cm;

	/**
	 * Handles the synchronization mode of the screen
	 * 
	 * @param cm
	 *            - the client monitor handling the synchronization mode
	 */
	public SyncThread(ClientMonitor cm) {
		this.cm = cm;
	}

	/**
	 * Checks the number of images in buffer within the specified threshold of
	 * 200 milliseconds and the delay of images. Changes to synchronization mode
	 * accordingly
	 */
	public void run() {
		while (true) {
			if (cm.getSyncMode() == ClientMonitor.SYNCHRONIZED) {
				long t0 = System.currentTimeMillis();
				int n1 = cm.getSyncPicture(1, t0);
				int n2 = cm.getSyncPicture(2, t0);

				if (n1 == 0 || n2 == 0) {
					try {
						sleep(1000);
						t0 = System.currentTimeMillis();
						n1 = cm.getSyncPicture(1, t0);
						n2 = cm.getSyncPicture(2, t0);
						if (n1 == 0 || n2 == 0) {
							cm.changeSync(ClientMonitor.ASYNCHRONIZED);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			} else {
				long n1 = cm.getDelay(1);
				long n2 = cm.getDelay(2);

				if (n1 < 200 && n2 < 200) {
					try {
						sleep(1000);
						n1 = cm.getDelay(1);
						n2 = cm.getDelay(2);
						if (n1 < 200 && n2 < 200) {
							cm.changeSync(ClientMonitor.SYNCHRONIZED);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
