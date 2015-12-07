package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
	private int[] port;
	private ClientMonitor monitor;
	private String[] hostName;
	private Socket clientSocket;

	/**
	 * Handles connecting to camera servers
	 * 
	 * @param monitor - the client monitor
	 * @param port - ports to be used
	 * @param hostName - host names to be used
	 */
	public ClientThread(ClientMonitor monitor, int[] port, String[] hostName) {
		this.port = port;
		this.monitor = monitor;
		this.hostName = hostName;

		System.out.println("Connecting to server");

	}

	/**
	 * Attempts connection with two cameras at port the given ports
	 */
	public void run() {
		int i = 0;
		while (i < 2) {
			try {
				clientSocket = new Socket(hostName[i], port[i]);
				OutputStream out = clientSocket.getOutputStream();
				InputStream in = clientSocket.getInputStream();
				monitor.connect(in, out, clientSocket);
				i++;
			} catch (IOException e) {
//				System.err.println("No new camera");
			}
		}

	}
}
