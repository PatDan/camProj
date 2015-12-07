package client;

import java.io.IOException;
import java.io.InputStream;

import Util.Util;

public class ServerReaderThread extends Thread {
	private ClientMonitor clientMonitor;
	private InputStream in;
	private int cam;

	/**
	 * Handles reading images from the server
	 * 
	 * @param clientMonitor
	 *            - the client monitor containing the image buffers to add
	 *            images to
	 * @param in
	 *            - the inputstream from the server
	 * @param cam
	 *            - the number of the image buffer to put the image
	 */
	public ServerReaderThread(ClientMonitor clientMonitor, InputStream in, int cam) {
		this.clientMonitor = clientMonitor;
		this.in = in;
		this.cam = cam;
	}

	/**
	 * Reads from inputstream and adds the image to the image buffer
	 */
	public void run() {
		while (true) {

//			System.out.println("my mother said yes: ");
			byte[] l = new byte[4];
			try {
				l[0] = (byte) in.read();
				l[1] = (byte) in.read();
				l[2] = (byte) in.read();
				l[3] = (byte) in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}

			int size = Util.byteToInt(l);
			if(size < 0){
				System.err.println("Server has unexpectedly shutdown");
				System.exit(1);
			}
			byte[] msg = new byte[size];
			int read = 0;
			try {
				while (read != size) {
					int n = in.read(msg, read, size - read);
					if (n == -1)
						throw new IOException();
					read += n;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte motion = msg[0];

			byte[] time = new byte[8];

			for (int i = 1; i < 9; i++) {
				time[i - 1] = msg[i];
			}

			byte[] image = new byte[msg.length - 9];
			for (int i = 9; i < msg.length; i++) {
				image[i - 9] = msg[i];
			}

			Picture p = new Picture(image, motion, Util.byteToLong(time));
			clientMonitor.putImage(p, cam);
		}
	}
}
