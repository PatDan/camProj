package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;



public class ClientMonitor {
	public static final int PORT_NUMBER = 8080;
	
	public ClientMonitor() {
		System.out.println("Starting client thread");
		new ClientThread(this, PORT_NUMBER, "Hello").start();
	}
	
	synchronized void connect(InputStream in, OutputStream out) {
		new ServerReaderThread(this, in).start();
		new ClientWriterThread(this, out).start();
	}
}
