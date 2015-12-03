package server;

import java.io.IOException;
import java.io.OutputStream;


import se.lth.cs.eda040.proxycamera.AxisM3006V; //This is for proxy camera
//import se.lth.cs.eda040.fakecamera.AxisM3006V; //This is for fake cameraM3006V;

public class ServerWriterThread extends Thread{
	private ServerMonitor monitor;
	private OutputStream out;
	private int camNbr;
	
	public ServerWriterThread(ServerMonitor serverMonitor, OutputStream out, int camNbr) {
		this.monitor = serverMonitor;
		this.out = out;
	}

	public void run() {
		while(true) {
			try {
				byte[] msg = monitor.image();
				out.write(msg[0]);
				out.write(msg[1]);
				out.write(msg[2]);
				out.write(msg[3]);
				out.write(msg,4,msg.length-4);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
