package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
	private int port;
	private ServerMonitor monitor;

	public ServerThread(ServerMonitor monitor, int port) {
		this.port = port;
		this.monitor = monitor;
	}

	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			monitor.setInput(in);
			monitor.setOutput(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
