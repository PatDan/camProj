package client;

import java.io.BufferedReader;
import java.io.PrintWriter;



public class ClientMonitor {
	public static final int PORT_NUMBER = 0;
	
	synchronized void setInput(BufferedReader in) {
		new ServerReaderThread(this, in).start();
	}

	synchronized void setOutput(PrintWriter out) {
		new ClientWriterThread(this, out).start();
	}
}
