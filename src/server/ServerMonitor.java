package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

public class ServerMonitor {
	public static final int IDLE_MODE = 1;
	public static final int MOVIE_MODE = 2;
	public static final int DISCONNECT = -1;
	private int movieMode;
	private long lastSentImage;
	private int camNbr;
	private int port;
	private boolean motionDetected;
	private volatile byte[] imageBuffer;
	private static int clientPort = 8080;
	private ClientReaderThread crt;
	private ReadImageThread rit;
	private ServerWriterThread swt;
	private boolean connected = false;

	/**
	 * The server monitor handling movie mode and sending images
	 * 
	 * @param camNbr
	 *            - the number of the camera to be used.
	 *            argus-camNbr.student.lth.se
	 * 
	 * @param port
	 *            - the port used by the camera
	 */
	public ServerMonitor(int camNbr, int port) {
		movieMode = DISCONNECT;
		motionDetected = false;
		imageBuffer = null;
		this.camNbr = camNbr;
		this.port = port;
		lastSentImage = System.currentTimeMillis() - 5000;
		String hostname = "argus-" + camNbr + ".student.lth.se";
		System.out.println("Running on: " + hostname + ":" + port);
		new ServerThread(this, clientPort++).start();
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
		movieMode = IDLE_MODE;
		connected = true;
		(crt = new ClientReaderThread(this, in, this)).start();
		(rit = new ReadImageThread(this, camNbr, port)).start();
		(swt = new ServerWriterThread(this, out)).start();
		try {
			while(movieMode != DISCONNECT) wait();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used when client disconnects
	 */
	synchronized void disconnect() {
		connected = false;
		notifyAll();
	}

	/**
	 * Checks if a client is connected
	 * @return
	 */
	synchronized boolean isConnected() {
		return connected;
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
		if(movieMode == DISCONNECT){
			connected = false;
		}
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
	 * Returns the image in the image buffer. If in idle mode, returns image
	 * every five second
	 * 
	 * @return - the image from the image buffer
	 */
	synchronized byte[] image() {
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
		byte[] ret = imageBuffer;
		imageBuffer = null;
		notifyAll();
		return ret;
	}
}
