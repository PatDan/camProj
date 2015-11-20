package server;

import java.io.BufferedReader;

public class ClientReaderThread extends Thread {
	private ServerMonitor monitor;
	private BufferedReader in;
	
	public ClientReaderThread(ServerMonitor serverMonitor, BufferedReader in) {
		this.monitor = serverMonitor;
		this.in = in;
	}

	public void run() {
		
	}
}
