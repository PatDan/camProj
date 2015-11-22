package server;

import java.io.InputStream;
import java.io.OutputStream;
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
		System.out.println("Starting server thread");
		new ServerThread(this, 8080).start();
	}

	synchronized void connect(InputStream in, OutputStream out) {
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

				while (lastImage + 5000 > (t1 = System.currentTimeMillis()) && !(motionDetected = cam.motionDetected()))
					wait(lastImage + 5000 - t1);
			} catch (InterruptedException e) {
				System.out.println("Server interrupted while waiting for image");
			}
		}

		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		int pos = cam.getJPEG(jpeg, 0);
		byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		cam.getTime(time, 0);
		byte motion = (byte) (motionDetected ? 1 : 0);
		jpeg = trim(jpeg, pos);
		System.out.println("ServerMonitor: jpeg.length: " + jpeg.length);
		int length = jpeg.length + time.length+1;
		byte[] blength = intToByteArray(length);
		byte[] msg = new byte[blength.length+length];
		for(int i = 0; i < blength.length; i++) {
			msg[i] = blength[i];
		}
		int offset = blength.length;
		msg[offset] = motion;
		offset++;
		for(int i = 0; i < time.length; i++) {
			msg[i+offset] = time[i];
		}
		offset += time.length;
		for(int i = 0; i < jpeg.length; i++) {
			msg[i+offset] = jpeg[i];
		}
		
		lastImage = System.currentTimeMillis();
		
		System.out.println("ServerMonitor: msg size: " + msg.length);

		return msg;
	}

	private byte[] trim(byte[] jpeg, int pos) {
		byte[] image = new byte[pos + 1];
		for (int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		return image;
	}

	public static void main(String[] args) {

		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		AxisM3006V cam = new AxisM3006V();
		cam.init();
		cam.connect();
		int length = cam.getJPEG(jpeg, 0);
		int pos = jpeg.length - 1;
		while (jpeg[pos] == 0 && pos >= 0) {
			pos--;
		}

		byte[] image = new byte[pos + 1];
		byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		cam.getTime(time, 0);
//		System.out.println(Arrays.toString(time));
//		byte[] b = intToByteArray(128);
//		System.out.println(Arrays.toString(b));
//		int a = byteToIntArray(b);
//		System.out.println(a);

		// System.out.println(jpeg.length + " " + pos + " " + image.length);

		for (int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		
		System.out.println(length);
		System.out.println(image.length);

		// System.out.println(image.length);

		// System.out.println(Arrays.toString(image));
	}

	private byte[] intToByteArray(int data) {

		byte[] result = new byte[4];

		result[0] = (byte) ((data & 0xFF000000) >> 24);
		result[1] = (byte) ((data & 0x00FF0000) >> 16);
		result[2] = (byte) ((data & 0x0000FF00) >> 8);
		result[3] = (byte) ((data & 0x000000FF) >> 0);

		return result;
	}
	
	private int byteToInt(byte[] data) {
		int i= (data[0]<<24)&0xff000000|
			       (data[1]<<16)&0x00ff0000|
			       (data[2]<< 8)&0x0000ff00|
			       (data[3]<< 0)&0x000000ff;
		return i;
	}
}
