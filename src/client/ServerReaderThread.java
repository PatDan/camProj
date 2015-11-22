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
			byte[] l = new byte[4];
			try {
				l[0] = (byte)in.read();
				l[1] = (byte)in.read();
				l[2] = (byte)in.read();
				l[3] = (byte)in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int size = byteToInt(l);
			
			System.out.println("size " + size);
			byte[] msg = new byte[size];
			try {
				in.read(msg, 0, size);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("ServerReader: msg.length: " + msg.length);
//		}
	}
	
	private byte[] trim(byte[] array, int pos) {
		byte[] trimedArray = new byte[pos];
		
		for (int i = 0; i < pos; i++) {
			trimedArray[i] = array[i];
		}
		return trimedArray;
	}
	
	private int byteToInt(byte[] data) {
		int i= (data[0]<<24)&0xff000000|
			       (data[1]<<16)&0x00ff0000|
			       (data[2]<< 8)&0x0000ff00|
			       (data[3]<< 0)&0x000000ff;
		return i;
	}

}
