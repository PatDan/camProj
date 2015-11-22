package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import server.ServerMonitor;

public class ClientThread {
	private int port;
	private ClientMonitor monitor;
	private String hostName;
	
	public ClientThread(ClientMonitor monitor, int port, String hostName) {
		this.port = port;
		this.monitor = monitor;
		this.hostName = hostName;
	}

	public void run() {
		try {
			Socket kkSocket = new Socket(hostName, port);
			PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(kkSocket.getInputStream()));
		    monitor.setInput(in);
		    monitor.setOutput(out);
		} catch (IOException e) {
			
		}
		
	}
}
