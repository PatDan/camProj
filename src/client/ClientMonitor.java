package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientMonitor {
	public static int PORT_NUMBER = 8080;
	public static final int IDLE = 1;
	public static final int MOVIE = 2;
	public static final int DISCONNECT = -1;
	public static final int SYNCHRONIZED = 1;
	public static final int ASYNCHRONIZED = 2;

	private int movieMode = IDLE;
	private int syncMode = SYNCHRONIZED;
	
	private volatile Picture[] imageBuffer1;
	private volatile Picture[] imageBuffer2;
	private long[] delayTime = { -1, -1 };
	private FrameGUI gui;
	private int nextCam;
	private boolean autoSync;
	private Socket socket;
	private int[] ports;
	private String[] cameras;

	/**
	 * Monitor for the client. Handles buffers for the cameras, mode for
	 * synchronization and mode for sending images.
	 */
	public ClientMonitor(int[] ports, String[] cameras) {
		gui = new FrameGUI(this);
		imageBuffer1 = new Picture[0];
		imageBuffer2 = new Picture[0];
		nextCam = 1;
		autoSync = true;
		this.ports = ports;
		this.cameras = cameras;
		new ClientThread(this, ports, cameras).start();
		new SyncThread(this).start();
	}

	/**
	 * Called when a connection to a camera server is established
	 * 
	 * @param in
	 *            - inputstream from camera server
	 * @param out
	 *            - outputstream to camera server
	 */
	synchronized void connect(InputStream in, OutputStream out, Socket socket) {
		this.socket = socket;
		int nextCam = this.nextCam;
		this.nextCam++;
		new ServerReaderThread(this, in, nextCam).start();
		new ClientWriterThread(this, out).start();
		new ScreenThread(this, nextCam).start();
	}

	synchronized void close() {
		movieMode = DISCONNECT;
		notifyAll();
	}

	/**
	 * Called to update a video panel
	 * 
	 * @param panel
	 *            - the number of the panel to be updated
	 */
	synchronized void updateScreen(int panel) {
		Picture p = getPicture(panel);
		gui.sendImage(p, panel);

	}

	/**
	 * Change the video mode of the client
	 * 
	 * @param videoMode
	 *            - IDLE or MOVIE
	 */
	synchronized void changeVideoMode(int movieMode) {
		if (movieMode == IDLE) {
			forceSync(SYNCHRONIZED);
		}
		this.movieMode = movieMode;
		notifyAll();
	}

	/**
	 * Force the synchronization mode of the client
	 * 
	 * @param syncMode
	 *            - SYNCHRONIZED or ASYNCHRONIZED
	 */
	synchronized void forceSync(int syncMode) {
		if (movieMode == IDLE && syncMode == ASYNCHRONIZED) {
			gui.changeSyncMode(SYNCHRONIZED);
		} else {
			this.syncMode = syncMode;
			gui.changeSyncMode(syncMode);
			notifyAll();
		}
	}

	/**
	 * Change the synchronization mode of the client if mode is auto
	 * 
	 * @param sync
	 *            - SYNCHRONIZED or ASYNCHRONIZED
	 */
	synchronized void changeSync(int sync) {
		if (autoSync && movieMode == MOVIE) {
			forceSync(sync);
		}
	}

	/**
	 * Adds an image to the buffer of the camera's buffer
	 * 
	 * @param image
	 *            - The image to be added
	 * @param cam
	 *            - The number of the camera adding an image
	 */
	synchronized void putImage(Picture image, int cam) {
		Picture[] imageBuffer = null;
		switch (cam) {
		case 1:
			imageBuffer = imageBuffer1;
			break;
		case 2:
			imageBuffer = imageBuffer2;
			break;
		}

		if (imageBuffer == null) {
			System.err.println("Oh no...." + cam);
		}
		Picture[] temp = new Picture[imageBuffer.length + 1];
		for (int i = 0; i < imageBuffer.length; i++) {
			temp[i] = imageBuffer[i];
		}

		temp[temp.length - 1] = image;

		switch (cam) {
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
		delayTime[cam - 1] = System.currentTimeMillis() - image.getTime();

		notifyAll();
	}

	/**
	 * Gets the first image from the camera's buffer
	 * 
	 * @param cam
	 *            - number of the camera
	 * @return the image
	 */
	private Picture getPicture(int cam) {
		int threshold = 0;
		switch (syncMode) {
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
			switch (cam) {
			case 1:
				while (imageBuffer1.length == 0) {
					wait();
				}
				while ((t0 = System.currentTimeMillis()) < imageBuffer1[0].getTime() + threshold)
					wait(imageBuffer1[0].getTime() + threshold - t0);
				imageBuffer = imageBuffer1;
				break;
			case 2:
				while (imageBuffer2.length == 0) {
					wait();
				}
				while ((t0 = System.currentTimeMillis()) < imageBuffer2[0].getTime() + threshold)
					wait(imageBuffer2[0].getTime() + threshold - t0);
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

		switch (cam) {
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

	/**
	 * Checks if the movie mode is changed
	 * 
	 * @param lastMode
	 *            - the last mode returned
	 * @return the mode
	 */
	synchronized int updateMode(int lastMode) {
		try {
			while (movieMode == lastMode) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return movieMode;
	}

	/**
	 * Checks the number of images in the camera's buffer within the thershold
	 * 
	 * @param cam
	 *            - The number of the camera
	 * @param t0
	 *            - current time
	 * @return
	 */
	synchronized int getSyncPicture(int cam, long t0) {
		Picture[] imageBuffer = null;
		switch (cam) {
		case 1:
			imageBuffer = imageBuffer1;
			break;
		case 2:
			imageBuffer = imageBuffer2;
			break;
		}

		int i = 0;

		while (i < imageBuffer.length && (t0 - imageBuffer[i].getTime() < 200)) {
			i++;
		}

		return i;
	}

	/**
	 * @return the synchronization mode
	 */
	synchronized int getSyncMode() {
		return syncMode;
	}

	/**
	 * Controlls if synchronization mode is auto
	 * 
	 * @param autoSync
	 *            - true if auto else false
	 */
	synchronized void setAuto(boolean autoSync) {
		this.autoSync = autoSync;

	}

	/**
	 * @param cam
	 *            - the number of the camera to check the delay time for
	 * @return - the delay time for the camera cam
	 */
	synchronized long getDelay(int cam) {
		return delayTime[cam - 1];
	}
}
