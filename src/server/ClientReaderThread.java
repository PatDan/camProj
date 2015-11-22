package server;

import java.io.BufferedReader;
import java.io.InputStream;

public class ClientReaderThread extends Thread {
	private ServerMonitor monitor;
	private InputStream in;
	
	public ClientReaderThread(ServerMonitor serverMonitor, InputStream in) {
		this.monitor = serverMonitor;
		this.in = in;
	}

	public void run() {
		
	}
}
