package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ServerMonitor {
	public static final int MOVIE_MODE = 1;
	public static final int IDLE_MODE = 2;
	private int mode;
	private long lastImage;
	private AxisM3006V cam;

	public ServerMonitor() {
		mode = IDLE_MODE;
		lastImage = System.currentTimeMillis() - 5000;
		cam = new AxisM3006V();
		cam.init();
		cam.connect();
	}

	synchronized void setInput(BufferedReader in) {
		new ClientReaderThread(this, in).start();
	}

	synchronized void setOutput(PrintWriter out) {
		new ServerWriterThread(this, out).start();
	}

	synchronized int mode() {
		return mode;
	}

	synchronized byte[] image() {
		if (mode == IDLE_MODE) {
			try {
				long t1;

				while (lastImage + 5000 < (t1 = System.currentTimeMillis()))
					wait(lastImage + 5000 - t1);
			} catch (InterruptedException e) {
				System.out.println("Server interrupted while waiting for image");
			}
		}
		
		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		cam.getJPEG(jpeg, 0);
		byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		cam.getTime(time, 0);
		byte motion = 0;
		Integer a = jpeg.length + time.length + 1 + Integer.BYTES;
		byte length = a.byteValue(); 
		
		lastImage = System.currentTimeMillis();
		
		return new byte[0];
	}
}
