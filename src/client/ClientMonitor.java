package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Util.Util;

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

	synchronized void readImage(InputStream in) {
		byte[] l = new byte[4];
		try {
			l[0] = (byte) in.read();
			l[1] = (byte) in.read();
			l[2] = (byte) in.read();
			l[3] = (byte) in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int size = Util.byteToInt(l);

		System.out.println("size " + size);
		byte[] msg = new byte[size];
		int read = 0;
		try {
			while (read != size) {
				int n = in.read(msg, read, size - read);
				if(n == -1) throw new IOException();
				read += n;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Kom ihåg att hämta ut tiden! kanske ska överväga image-klass ändå
		byte[] image = new byte[msg.length - 9];
		for (int i = 9; i < msg.length; i++) {
			image[i - 9] = msg[i];
		}

		putImage(image);

		System.out.println("ServerReader: msg.length: " + msg.length);
	}

	private void putImage(byte[] image) {
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
