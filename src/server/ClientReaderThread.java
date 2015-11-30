package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import Util.Util;
import client.Picture;

public class ClientReaderThread extends Thread {
	private ServerMonitor monitor;
	private InputStream in;

	public ClientReaderThread(ServerMonitor serverMonitor, InputStream in) {
		this.monitor = serverMonitor;
		this.in = in;
	}

	public void run() {
		while (true) {
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

			// System.out.println("size " + size);
			byte[] mode = new byte[4];
			byte[] time = new byte[8];
			int read = 0;
			try {
				while (read != 4) {
					int n = in.read(mode, read, 4 - read);
					if (n == -1)
						throw new IOException();
					read += n;
				}
				System.out.println("Read = " + read);
				read = 0;
				while (read != 8) {
					int n = in.read(time, read, 8 - read);
					if (n == -1) {
						throw new IOException();
					}
					read += n;
				}
				System.out.println("Read2 = " + read);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Mode: " + Util.byteToInt(mode));
			monitor.updateMode(Util.byteToInt(mode));
		}
	}
}
