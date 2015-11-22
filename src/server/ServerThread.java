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

	public ServerThread(ServerMonitor monitor, int port) {
		this.port = port;
		this.monitor = monitor;
		System.out.println("Creating server socket");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("Server on port " + port);
			Socket clientSocket = serverSocket.accept();
			System.out.println("Accepted client");
			OutputStream out = clientSocket.getOutputStream();
			InputStream in = clientSocket.getInputStream();
			monitor.connect(in, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
