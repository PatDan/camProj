package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class ClientWriterThread extends Thread {
	private ClientMonitor clientMonitor;
	private PrintWriter out;
	public ClientWriterThread(ClientMonitor clientMonitor, PrintWriter out) {
		this.clientMonitor = clientMonitor;
		this.out = out;
	}
	public void run() {
		String fromClient = null;
		
		// Warning temporary solution //
		BufferedReader in = null;
		// 							  //

		try {
			while ((fromClient = in.readLine()) != null) {
				System.out.println("FromClient: " + fromClient);
				out.println(fromClient);
			}
		} catch (IOException e) {
		}
	}

}


