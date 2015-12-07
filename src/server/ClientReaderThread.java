package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.SocketException;

import Util.Util;

public class ClientReaderThread extends Thread {
	private ServerMonitor monitor;
	private InputStream in;
	private ServerMonitor sm;

	/**
	 * Reading messages from the client
	 * 
	 * @param serverMonitor
	 *            - the server monitor handling movie mode
	 * @param in
	 *            - the inputstream from the client
	 */
	public ClientReaderThread(ServerMonitor serverMonitor, InputStream in, ServerMonitor sm) {
		this.monitor = serverMonitor;
		this.in = in;
		this.sm = sm;
	}

	/**
	 * Reads messages from the client containing state changes. Changes the
	 * state of the monitor accordingly
	 */
	public void run() {
		while (sm.isConnected()) {
			byte[] l = new byte[4];
			try {
				l[0] = (byte) in.read();
				l[1] = (byte) in.read();
				l[2] = (byte) in.read();
				l[3] = (byte) in.read();
			} catch (IOException e) {
				interrupt();
			}

			// System.out.println("size " + size);
			byte[] mode = new byte[4];
			byte[] time = new byte[8];
			int read = 0;
			try {
				while (read != 4) {
					int n = in.read(mode, read, 4 - read);
					if (n == -1) {
						sm.disconnect();
						continue;
					}
					read += n;
				}
				System.out.println("Read = " + read);
				read = 0;
				while (read != 8) {
					int n = in.read(time, read, 8 - read);
					if (n == -1) {
						sm.disconnect();
						continue;
					}
					read += n;
				}
				System.out.println("Read2 = " + read);

			} catch (IOException e) {
				if(!sm.isConnected()) {
					System.out.println("Client disconnected");
				}
			}

			monitor.updateMode(Util.byteToInt(mode));
		}
		
	}
}
