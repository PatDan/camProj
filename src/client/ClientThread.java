package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
	private int port;
	private ClientMonitor monitor;
	private String hostName;
	private Socket clientSocket;

	/**
	 * Handles connecting to camera servers
	 * 
	 * @param monitor
	 * @param port
	 * @param hostName
	 */
	public ClientThread(ClientMonitor monitor, int port, String hostName) {
		this.port = port;
		this.monitor = monitor;
		this.hostName = hostName;

		System.out.println("Connecting to server");

	}

	/**
	 * Attempts connection with two cameras at port 8080 and 8081
	 */
	public void run() {
		while (port < 8082) {
			try {
				clientSocket = new Socket("localhost", port);
				OutputStream out = clientSocket.getOutputStream();
				InputStream in = clientSocket.getInputStream();
				monitor.connect(in, out);
				port++;
			} catch (IOException e) {
				System.err.println("No new camera");
			}
		}

	}
}
