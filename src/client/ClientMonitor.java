package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientMonitor {
	public static final int PORT_NUMBER = 8080;
	public static final int AUTO = 0;
	public static final int IDLE = 1;
	public static final int MOVIE = 2;
	public static final int SYNCHRONIZED = 1;
	public static final int ASYNCHRONIZED = 2;

	private int mode = AUTO;
	private int sync = AUTO;
	private byte[] image;

	public ClientMonitor() {
		System.out.println("Starting client thread");
		new ClientThread(this, PORT_NUMBER, "Hello").start();
		image = new byte[0];
	}

	synchronized void connect(InputStream in, OutputStream out) {
		new ServerReaderThread(this, in).start();
		new ClientWriterThread(this, out).start();
	}

	synchronized void changeMode(int mode) {
		this.mode = mode;
	}

	synchronized void changeSync(int sync) {
		this.sync = sync;
	}

	synchronized void putImage(byte[] image) {
		this.image = image;
		System.out.println("ClientMonitor image.length: " + image.length);
	}

	synchronized byte[] getImage() {
		try {
			while (image.length == 0)
				wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Returning image");
		byte[] ret = image;
		image = new byte[0];
		return ret;
	}
}
