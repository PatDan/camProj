package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Util.Util;

public class ClientMonitor {
	public static int PORT_NUMBER = 8080;
	public static final int IDLE = 1;
	public static final int MOVIE = 2;
	public static final int SYNCHRONIZED = 1;
	public static final int ASYNCHRONIZED = 2;

	private int movieMode = IDLE;
	private int syncMode = SYNCHRONIZED;
	private Picture[] imageBuffer1;
	private Picture[] imageBuffer2;
	private FrameGUI gui;
	private int nextCam;

	public ClientMonitor() {
//		System.out.println("Starting client thread");
		gui = new FrameGUI(this);
		imageBuffer1 = new Picture[0];
		imageBuffer2 = new Picture[0];
		new ClientThread(this, PORT_NUMBER, "localhost").start();
		new SyncThread(this).start();
		nextCam = 1;
	}

	synchronized void connect(InputStream in, OutputStream out) {
		new ServerReaderThread(this, in, nextCam).start();
		new ClientWriterThread(this, out, nextCam).start();
		new ScreenThread(this, nextCam).start();
		nextCam++;
	}

	synchronized void updateScreen(int panel) {
		Picture p = getPicture(panel);
		gui.sendImage(p, panel);
		
	}

	synchronized void changeMode(int mode) {
		this.movieMode = mode;
		System.out.println("The Mode is:" + mode);
		notifyAll();
	}

	synchronized void changeSync(int sync) {
		this.syncMode = sync;
		gui.changeSyncMode(syncMode);
		notifyAll();
	}

	synchronized void putImage(Picture image, int cam) {
		Picture[] imageBuffer = null;
		switch(cam) {
		case 1:
			imageBuffer = imageBuffer1;
			break;
		case 2:
			imageBuffer = imageBuffer2;
			break;
		}
		
		Picture[] temp = new Picture[imageBuffer.length + 1];
		for (int i = 0; i < imageBuffer.length; i++) {
			temp[i] = imageBuffer[i];
		}

		temp[temp.length - 1] = image;

		switch(cam) {
		case 1:
			imageBuffer1 = temp;
			break;
		case 2:
			imageBuffer2 = temp;
			break;
		}
		
		if (image.motion() && movieMode != MOVIE) {
			movieMode = MOVIE;
			gui.updateMode(movieMode);
			gui.activeCamera(cam);
		}
		
		notifyAll();
	}

	private Picture getPicture(int cam) {
		int threshold = 0;
		switch(syncMode) {
		case SYNCHRONIZED:
			threshold = 200;
			break;
		case ASYNCHRONIZED:
			threshold = 0;
			break;
		}
		
		Picture[] imageBuffer = null;
		try {
			long t0;
			switch(cam) {
			case 1:
				while (imageBuffer1.length == 0) {
					wait();
				}
				while((t0 = System.currentTimeMillis()) < imageBuffer1[0].getTime() + threshold) wait(imageBuffer1[0].getTime() + threshold - t0);
				imageBuffer = imageBuffer1;
				break;
			case 2:
				while (imageBuffer2.length == 0) {
					wait();
				}
				while((t0 = System.currentTimeMillis()) < imageBuffer2[0].getTime() + threshold) wait(imageBuffer2[0].getTime() + threshold - t0);
				imageBuffer = imageBuffer2;
				break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Picture ret = imageBuffer[0];
		if (imageBuffer.length == 1) {
			imageBuffer = new Picture[0];
		} else {
			Picture[] temp = new Picture[imageBuffer.length - 1];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = imageBuffer[i + 1];
			}
			imageBuffer = temp;
		}
		
		switch(cam) {
		case 1:
			imageBuffer1 = imageBuffer;
			break;
		case 2:
			imageBuffer2 = imageBuffer;
			break;
		}

		notifyAll();
		return ret;
	}
	
	synchronized int updateMode(int lastMode) {
		try {
			while(movieMode == lastMode) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return movieMode;
	}

	synchronized int getSyncPicture(int cam, long t0) {
		Picture[] imageBuffer = null;
		switch(cam) {
		case 1:
			imageBuffer = imageBuffer1;
			break;
		case 2:
			imageBuffer = imageBuffer2;
			break;
		}
		
		int i = 0;
		
		while(i < imageBuffer.length && (t0 - imageBuffer[i].getTime() < 200)) {
			i++;
		}
		
		return i;
	}

	synchronized int getSyncMode() {
		return syncMode;
	}
}
