package server;

import java.io.InputStream;
import java.io.OutputStream;

public class ServerMonitor {
	public static final int IDLE_MODE = 1;
	public static final int MOVIE_MODE = 2;
	private int movieMode;
	private long lastSentImage;
	private static int camNbr = 0;
	private int thisCamNbr;
	private boolean motionDetected;
	private volatile byte[] imageBuffer;

	/**
	 * The server monitor handling movie mode and sending images
	 */
	public ServerMonitor() {
		movieMode = IDLE_MODE;
		lastSentImage = System.currentTimeMillis() - 5000;
		new ServerThread(this, 8080 + camNbr).start();
		thisCamNbr = camNbr++;
		motionDetected = false;
		imageBuffer = null;
	}

	/**
	 * Called when a client connects to the server. Starts threads handling
	 * communication with client.
	 * 
	 * @param in
	 *            - the inputstream from the client
	 * @param out
	 *            - the outputstream to the client
	 */
	synchronized void connect(InputStream in, OutputStream out) {
		new ClientReaderThread(this, in).start();
		new ReadImageThread(this, thisCamNbr).start();
		new ServerWriterThread(this, out).start();
	}

	/**
	 * Returns the movie mode of the monitor
	 * 
	 * @return
	 */
	synchronized int mode() {
		return movieMode;
	}

	/**
	 * Changes the movie mode of the monitor
	 * 
	 * @param mode
	 *            - the mode to change to
	 */
	synchronized void updateMode(int mode) {
		this.movieMode = mode;
		notifyAll();
	}

	/**
	 * Adds an image to the image buffer and detects motion
	 * 
	 * @param image
	 *            - the image fetched from the camera
	 * @param motionDetected
	 *            - true if motion detected, else false
	 */
	synchronized void readImage(byte[] image, boolean motionDetected) {
		try {
			while (movieMode == MOVIE_MODE && this.imageBuffer != null)
				wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.imageBuffer = image;
		if (motionDetected) {
			movieMode = MOVIE_MODE;
		}
		notifyAll();
	}

	/**
	 * Returns the image in the image buffer. If in idle mode, returns image every
	 * five second
	 * 
	 * @return - the image from the image buffer
	 */
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
