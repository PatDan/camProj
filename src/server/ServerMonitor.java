package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ServerMonitor {
	public static final int MOVIE_MODE = 1;
	public static final int IDLE_MODE = 2;
	private int mode;
	private long lastImage;
	private AxisM3006V cam;
	private boolean connected;

	public ServerMonitor() {
		mode = IDLE_MODE;
		lastImage = System.currentTimeMillis() - 5000;
		connected = false;
		cam = new AxisM3006V();
		cam.init();
		cam.connect();
		new ServerThread(this, 8080).start();
	}

	synchronized void connect(BufferedReader in, PrintWriter out) {
		connected = true;
		new ClientReaderThread(this, in).start();
		new ServerWriterThread(this, out).start();
	}

	synchronized int mode() {
		return mode;
	}

	synchronized byte[] image() {
		boolean motionDetected = false;
		if (mode == IDLE_MODE) {
			try {
				long t1;

				while (lastImage + 5000 < (t1 = System.currentTimeMillis()) && !(motionDetected = cam.motionDetected()))
					wait(lastImage + 5000 - t1);
			} catch (InterruptedException e) {
				System.out.println("Server interrupted while waiting for image");
			}
		}
		
		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		cam.getJPEG(jpeg, 0);
		byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		cam.getTime(time, 0);
		byte motion = (byte) (motionDetected?1:0);
		jpeg = trim(jpeg);
		int length = jpeg.length + time.length;
		System.out.println(jpeg);
		
		lastImage = System.currentTimeMillis();
		
		return new byte[0];
	}
	
	private byte[] trim(byte[] jpeg) {
		int pos = jpeg.length - 1;
		while(jpeg[pos] == 0 && pos >= 0) {
			pos--;
		}
		byte[] image = new byte[pos + 1];
		for(int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		return image;
	}
	
	public static void main(String[] args) {
		
		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		AxisM3006V cam = new AxisM3006V();
		cam.init();
		cam.connect();
		cam.getJPEG(jpeg, 0);
		int pos = jpeg.length - 1;
		while(jpeg[pos] == 0 && pos >= 0) {
			pos--;
		}
		
		byte[] image = new byte[pos + 1];

		System.out.println(jpeg.length + " " + pos + " " + image.length);
		
		for(int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		
		System.out.println(image.length);
		
		System.out.println(Arrays.toString(image));
	}
}
