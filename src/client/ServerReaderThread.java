package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ServerReaderThread extends Thread {
	private ClientMonitor clientMonitor;
	private InputStream in;

	public ServerReaderThread(ClientMonitor clientMonitor, InputStream in) {
		this.clientMonitor = clientMonitor;
		this.in = in;
	}

	public void run() {
//		while(true) {
			clientMonitor.readImage(in);
//		}
	}
}
