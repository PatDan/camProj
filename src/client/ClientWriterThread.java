package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientWriterThread extends Thread {
	private ClientMonitor clientMonitor;
	private OutputStream out;
	public ClientWriterThread(ClientMonitor clientMonitor, OutputStream out) {
		this.clientMonitor = clientMonitor;
		this.out = out;
	}
	public void run() {
		String fromClient = null;
	

		
	}

}


