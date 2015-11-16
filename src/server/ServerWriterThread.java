package server;

import java.io.PrintWriter;

public class ServerWriterThread extends Thread{
	private ServerMonitor monitor;
	private PrintWriter out;
	
	public ServerWriterThread(ServerMonitor serverMonitor, PrintWriter out) {
		this.monitor = serverMonitor;
		this.out = out;
	}

	public void run() {
		while(true) {
			
		}
	}
}
