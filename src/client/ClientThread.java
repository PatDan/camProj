package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread{
	private int port;
	private ClientMonitor monitor;
	private String hostName;
	private Socket clientSocket;
	
	public ClientThread(ClientMonitor monitor, int port, String hostName) {
		this.port = port;
		this.monitor = monitor;
		this.hostName = hostName;

		System.out.println("Connecting to server");
		try {
			clientSocket = new Socket("localhost", port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			OutputStream out = clientSocket.getOutputStream();
		    InputStream in = clientSocket.getInputStream();
		    monitor.connect(in, out);
		} catch (IOException e) {
			
		}
		
	}
}
