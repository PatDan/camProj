package client;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerReaderThread extends Thread {
	private ClientMonitor clientMonitor;
	private BufferedReader in;

	public ServerReaderThread(ClientMonitor clientMonitor, BufferedReader in) {
		this.clientMonitor = clientMonitor;
		this.in = in;
	}

	public void run() {
		String fromServer = null;

		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye."))
					break;
			}
		} catch (IOException e) {
		}
	}

}
