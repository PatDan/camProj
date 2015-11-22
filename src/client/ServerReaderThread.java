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
			byte[] msg = new byte[4096*8];
			int pos = 0;
			try {
				pos = in.read(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("pos " + pos);
			System.out.println("msg.length: " + msg.length);
			msg = trim(msg, pos);
			System.out.println("msg.length: " + msg.length);
			
			System.out.println(Arrays.toString(msg));
//		}
	}
	
	private byte[] trim(byte[] array, int pos) {
		byte[] trimedArray = new byte[pos];
		
		for (int i = 0; i < pos; i++) {
			trimedArray[i] = array[i];
		}
		return trimedArray;
	}

}
