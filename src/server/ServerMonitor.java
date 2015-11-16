package server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ServerMonitor {

	public ServerMonitor() {

	}
	
	synchronized void setInput(BufferedReader in) {
		new ClientReaderThread(this, in).start();
	}
	
	synchronized void setOutput(PrintWriter out)  {
		new ServerWriterThread(this, out).start();
	}
}
