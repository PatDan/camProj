package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientWriterThread extends Thread {
	private ClientMonitor clientMonitor;
	private OutputStream out;
	private int cam;
	public ClientWriterThread(ClientMonitor clientMonitor, OutputStream out, int cam) {
		this.clientMonitor = clientMonitor;
		this.out = out;
		this.cam = cam;
	}
	public void run() {
		String fromClient = null;
	

		
	}

}


