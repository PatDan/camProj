package server;

import java.io.InputStream;
import java.io.OutputStream;

import Util.Util;
//import se.lth.cs.eda040.proxycamera.AxisM3006V; //This is for proxy camera
import se.lth.cs.eda040.fakecamera.AxisM3006V; //This is for fake cameraM30

public class ServerMonitor {
	public static final int IDLE_MODE = 1;
	public static final int MOVIE_MODE = 2;
	private int movieMode;
	private long lastSentImage;
	private static int camNbr = 0;
	private int thisCamNbr;
	private boolean motionDetected;
	private volatile byte[] imageBuffer;

	public ServerMonitor() {
		movieMode = IDLE_MODE;
		lastSentImage = System.currentTimeMillis() - 5000;
		new ServerThread(this, 8080 + camNbr).start();
		thisCamNbr = camNbr++;
		motionDetected = false;
		imageBuffer = null;
	}

	synchronized void connect(InputStream in, OutputStream out) {
		new ClientReaderThread(this, in).start();
		new ServerWriterThread(this, out, thisCamNbr).start();
	}

	synchronized int mode() {
		return movieMode;
	}

	synchronized void updateMode(int mode) {
		this.movieMode = mode;
		notifyAll();
	}

	synchronized void cameraConnect(AxisM3006V cam) {
		new ReadImageThread(this, cam).start();
	}

	synchronized void readImage(byte[] imageBuffer, boolean motionDetected) {
		try {
			while (movieMode == MOVIE_MODE && this.imageBuffer != null)
				wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.imageBuffer = imageBuffer;
		if (motionDetected) {
			movieMode = MOVIE_MODE;
		}
		notifyAll();
	}

	synchronized byte[] image() {
		System.out.println("Trying to send image...");
		try {
			long t1;
			while (movieMode == IDLE_MODE && lastSentImage + 5000 > (t1 = System.currentTimeMillis())
					&& !(motionDetected))
				wait(lastSentImage + 5000 - t1);
			while (imageBuffer == null) {
				wait();
			}
		} catch (InterruptedException e) {
			System.out.println("Server interrupted while waiting for image");
		}
		lastSentImage = System.currentTimeMillis();
		System.out.println("Sending image");
		byte[] ret = imageBuffer;
		imageBuffer = null;
		notifyAll();
		return ret;
	}
}
