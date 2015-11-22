package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ServerWriterThread extends Thread{
	private ServerMonitor monitor;
	private OutputStream out;
	
	public ServerWriterThread(ServerMonitor serverMonitor, OutputStream out) {
		this.monitor = serverMonitor;
		this.out = out;
	}

	public void run() {
//		while(true) {
			try {
				out.write(monitor.image());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
	}
}
