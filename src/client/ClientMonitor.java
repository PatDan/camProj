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

	private int mode = IDLE;
	private int sync = SYNCHRONIZED;
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
		this.mode = mode;
		System.out.println("The Mode is:" + mode);
		notifyAll();
	}

	synchronized void changeSync(int sync) {
		this.sync = sync;
		System.out.println("The Sync is:" + sync);

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
		
		if (image.motion()) {
			mode = MOVIE;
			gui.updateMode(mode);
		}
		
		notifyAll();
	}

	private Picture getPicture(int cam) {
		Picture[] imageBuffer = null;
		try {
			switch(cam) {
			case 1:
				while (imageBuffer1.length == 0) {
					wait();
				}
				imageBuffer = imageBuffer1;
				break;
			case 2:
				while (imageBuffer2.length == 0) {
					wait();
				}
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
			while(mode == lastMode) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return mode;
	}
}
