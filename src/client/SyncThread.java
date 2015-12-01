package client;

public class SyncThread extends Thread {
	private ClientMonitor cm;

	public SyncThread(ClientMonitor cm) {
		this.cm = cm;
	}

	public void run() {
		while (true) {
			if (cm.getSyncMode() == ClientMonitor.SYNCHRONIZED) {
//				System.out.println("MC Synchronized");
				long t0 = System.currentTimeMillis();
				int n1 = cm.getSyncPicture(1, t0);
				int n2 = cm.getSyncPicture(2, t0);
//				System.out.println("n1=" + n1 + " n2=" + n2);

				if (n1 == 0 || n2 == 0) {
					try {
						sleep(500);
						t0 = System.currentTimeMillis();
						n1 = cm.getSyncPicture(1, t0);
						n2 = cm.getSyncPicture(2, t0);
//						System.out.println("n1=" + n1 + " n2=" + n2);
						if (n1 == 0 || n2 == 0) {
							cm.changeSync(ClientMonitor.ASYNCHRONIZED);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			} else {
//				System.out.println("I'm A-sync");
				long t0 = System.currentTimeMillis();
				int n1 = cm.getSyncPicture(1, t0);
				int n2 = cm.getSyncPicture(2, t0);
//				System.out.println("n1=" + n1 + " n2=" + n2);
				
				if(n1 != 0 && n2 != 0) {
					cm.changeSync(ClientMonitor.SYNCHRONIZED);
				}
			}
		}
	}
}
