package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import Util.Util;

public class ClientWriterThread extends Thread {
	private ClientMonitor clientMonitor;
	private OutputStream out;
	private int cam;
	private int lastMode;
	
	public ClientWriterThread(ClientMonitor clientMonitor, OutputStream out, int cam) {
		this.clientMonitor = clientMonitor;
		this.out = out;
		this.cam = cam;
		lastMode = ClientMonitor.IDLE;
	}
	
	public void run() {
		while(true) {
			lastMode = clientMonitor.updateMode(lastMode);
			byte[] mode = Util.intToByteArray(lastMode);
			byte[] time = Util.longToByteArray(System.currentTimeMillis());
			int l = mode.length + time.length;
			byte[] length = Util.intToByteArray(l);
			
			byte[] msg = new byte[mode.length + time.length + length.length];
			
			int offset = 0;
			for(int i = 0; i < length.length; i++) {
				msg[i] = length[i];
			}
			offset += length.length;
			for(int i = 0; i < mode.length; i++) {
				msg[i + offset] = mode[i];
			}
			offset += mode.length;
			for(int i = 0; i < time.length; i++) {
				msg[i + offset] = time[i];
			}
			try {
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


