package server;

import java.io.InputStream;
import java.io.OutputStream;

import Util.Util;
//import se.lth.cs.eda040.proxycamera.AxisM3006V; //This is for proxy camera
import se.lth.cs.eda040.fakecamera.AxisM3006V; //This is for fake camera

public class ServerMonitor {
	public static final int IDLE_MODE = 1;
	public static final int MOVIE_MODE = 2;
	private int mode;
	private long lastImage;
	private AxisM3006V cam;
	private boolean connected;
	private static int camNbr = 0;

	public ServerMonitor() {
		mode = IDLE_MODE;
		lastImage = System.currentTimeMillis() - 5000;
		connected = false;
		cam = new AxisM3006V();
		cam.init();
//		cam.setProxy("argus-" + (camNbr + 1) + ".student.lth.se", 8080 + camNbr); //This is for proxy camera
		cam.connect();
		System.out.println("Starting server thread");
		new ServerThread(this, 8080 + camNbr).start();
		System.out.println("Starting server: " + camNbr);
		camNbr++;
	}

	synchronized void connect(InputStream in, OutputStream out) {
		connected = true;
		new ClientReaderThread(this, in).start();
		new ServerWriterThread(this, out).start();
	}

	synchronized int mode() {
		return mode;
	}

	synchronized void updateMode(int mode) {
		this.mode = mode;
		notifyAll();
	}

	synchronized byte[] image() {
		boolean motionDetected = false;
		try {
			long t1;

			while (mode == IDLE_MODE && lastImage + 5000 > (t1 = System.currentTimeMillis())
					&& !(motionDetected = cam.motionDetected()))
				wait(lastImage + 5000 - t1);
		} catch (InterruptedException e) {
			System.out.println("Server interrupted while waiting for image");
		}

		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		int pos = cam.getJPEG(jpeg, 0);
		byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		cam.getTime(time, 0);
		byte motion = (byte) (motionDetected ? 1 : 0);
		jpeg = trim(jpeg, pos);
		int length = jpeg.length + time.length + 1;
		byte[] blength = Util.intToByteArray(length);
		byte[] msg = new byte[blength.length + length];
		for (int i = 0; i < blength.length; i++) {
			msg[i] = blength[i];
		}
		int offset = blength.length;
		msg[offset] = motion;
		offset++;
		for (int i = 0; i < time.length; i++) {
			msg[i + offset] = time[i];
		}
		offset += time.length;
		for (int i = 0; i < jpeg.length; i++) {
			msg[i + offset] = jpeg[i];
		}

		lastImage = System.currentTimeMillis();

		return msg;
	}

	private byte[] trim(byte[] jpeg, int pos) {
		byte[] image = new byte[pos + 1];
		for (int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		return image;
	}

}
