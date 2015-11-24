package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Util.Util;

public class ClientMonitor {
	public static final int PORT_NUMBER = 8080;
	public static final int AUTO = 0;
	public static final int IDLE = 1;
	public static final int MOVIE = 2;
	public static final int SYNCHRONIZED = 1;
	public static final int ASYNCHRONIZED = 2;

	private int mode = AUTO;
	private int sync = AUTO;
	private Picture[] imageBuffer;
	private FrameGUI gui;

	public ClientMonitor() {
//		System.out.println("Starting client thread");
		gui = new FrameGUI(this);
		imageBuffer = new Picture[0];
		new ScreenThread(this, 1).start();
		new ClientThread(this, PORT_NUMBER, "Hello").start();
	}

	synchronized void connect(InputStream in, OutputStream out) {
		new ServerReaderThread(this, in).start();
		new ClientWriterThread(this, out).start();
	}

	synchronized void updateScreen(int panel) {
		Picture p = getPicture();
		gui.sendImage(p.getImage(), panel);
	}

	synchronized void changeMode(int mode) {
		this.mode = mode;
		notifyAll();
	}

	synchronized void changeSync(int sync) {
		this.sync = sync;
		notifyAll();
	}

//	synchronized void readImage(InputStream in) {
//		byte[] l = new byte[4];
//		try {
//			l[0] = (byte) in.read();
//			l[1] = (byte) in.read();
//			l[2] = (byte) in.read();
//			l[3] = (byte) in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		int size = Util.byteToInt(l);
//
//		// System.out.println("size " + size);
//		byte[] msg = new byte[size];
//		int read = 0;
//		try {
//			while (read != size) {
//				int n = in.read(msg, read, size - read);
//				if (n == -1)
//					throw new IOException();
//				read += n;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		byte motion = msg[0];
//
//		byte[] time = new byte[8];
//
//		for (int i = 1; i < 9; i++) {
//			time[i - 1] = msg[i];
//		}
//
//		// System.out.println("ClientMonitor MotionDetection: " + motion);
//		// System.out.println("ClientMonitor timestamp: " +
//		// Util.byteToLong(time));
//
//		// Kom ih�g att h�mta ut tiden! kanske ska �verv�ga image-klass �nd�
//		byte[] image = new byte[msg.length - 9];
//		for (int i = 9; i < msg.length; i++) {
//			image[i - 9] = msg[i];
//		}
//
//		Picture p = new Picture(image, motion, Util.byteToLong(time));
//
//		putImage(p);
//		notifyAll();
//
//		// System.out.println("ServerReader: msg.length: " + msg.length);
//	}

	synchronized void putImage(Picture image) {
		Picture[] temp = new Picture[imageBuffer.length + 1];
		for (int i = 0; i < imageBuffer.length; i++) {
			temp[i] = imageBuffer[i];
		}

		temp[temp.length - 1] = image;

		imageBuffer = temp;
		notifyAll();

//		System.out.println("ClientMonitor image.length: " + image.getImage().length);
//		System.out.println("ClientMonitor imageBuffer.length: " + imageBuffer.length);
	}

	private Picture getPicture() {
		try {
			while (imageBuffer.length == 0) {
//				System.out.println("ClientMonitor getPicture: imageBuffer.length: " + imageBuffer.length);
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
			this.imageBuffer = temp;
		}

//		System.out.println("ClientMonitor Returning Image");
		notifyAll();
		return ret;
	}
}
