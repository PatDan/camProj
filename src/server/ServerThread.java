package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	private int port;
	private ServerMonitor monitor;
	private ServerSocket serverSocket;

	/**
	 * Sets up server sockets
	 * 
	 * @param monitor
	 *            - the server monitor corresponding to the server sockets
	 * @param port
	 *            - the port for the server to run on
	 */
	public ServerThread(ServerMonitor monitor, int port) {
		this.port = port;
		this.monitor = monitor;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Starts the server and accepts one client
	 */
	public void run() {
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected");
				OutputStream out = clientSocket.getOutputStream();
				InputStream in = clientSocket.getInputStream();
				monitor.connect(in, out);
				System.out.println("Client disconnected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
