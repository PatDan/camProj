package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import Util.Util;

public class ServerReaderThread extends Thread {
	private ClientMonitor clientMonitor;
	private InputStream in;

	public ServerReaderThread(ClientMonitor clientMonitor, InputStream in) {
		this.clientMonitor = clientMonitor;
		this.in = in;
	}

	public void run() {
		while(true) {
//			clientMonitor.readImage(in);
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

			// System.out.println("size " + size);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte motion = msg[0];

			byte[] time = new byte[8];

			for (int i = 1; i < 9; i++) {
				time[i - 1] = msg[i];
			}

			// System.out.println("ClientMonitor MotionDetection: " + motion);
			// System.out.println("ClientMonitor timestamp: " +
			// Util.byteToLong(time));

			// Kom ih�g att h�mta ut tiden! kanske ska �verv�ga image-klass �nd�
			byte[] image = new byte[msg.length - 9];
			for (int i = 9; i < msg.length; i++) {
				image[i - 9] = msg[i];
			}

			Picture p = new Picture(image, motion, Util.byteToLong(time));
			clientMonitor.putImage(p);
		}
	}
}
