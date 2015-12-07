package server;

import java.io.IOException;
import java.io.OutputStream;

public class ServerWriterThread extends Thread{
	private ServerMonitor monitor;
	private OutputStream out;
	
	/**
	 * Sends images to the client
	 * @param serverMonitor - the server monitor to fetch images from
	 * @param out - the outputstream to the client
	 */
	public ServerWriterThread(ServerMonitor serverMonitor, OutputStream out) {
		this.monitor = serverMonitor;
		this.out = out;
	}

	/**
	 * Sends an image to the client every time an image is available
	 */
	public void run() {
		while(monitor.isConnected()) {
			try {
				byte[] msg = monitor.image();
				out.write(msg[0]);
				out.write(msg[1]);
				out.write(msg[2]);
				out.write(msg[3]);
				out.write(msg,4,msg.length-4);
				
			} catch (IOException e) {
				if(!monitor.isConnected()) {
					System.out.println("Client disconnected");
				}
			}
		}
	}
}
