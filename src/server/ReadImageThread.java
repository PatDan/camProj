package server;

import Util.Util;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ReadImageThread extends Thread{
	private ServerMonitor sm;
	private AxisM3006V cam;
	
	public ReadImageThread (ServerMonitor sm, AxisM3006V cam) {
		this.sm = sm;
		this.cam = cam;
	}
	public void run() {
		while(true) {
			System.out.println("Reading image");
			byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
			int pos = cam.getJPEG(jpeg, 0);
			boolean motionDetected = cam.motionDetected();
			byte[] time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
			cam.getTime(time, 0);
			byte motion = (byte) (motionDetected ? 1 : 0);
			jpeg = trim(jpeg, pos);
			int length = jpeg.length + time.length + 1;
			byte[] blength = Util.intToByteArray(length);
			byte[] imageBuffer = new byte[blength.length + length];
			for (int i = 0; i < blength.length; i++) {
				imageBuffer[i] = blength[i];
			}
			int offset = blength.length;
			imageBuffer[offset] = motion;
			offset++;
			for (int i = 0; i < time.length; i++) {
				imageBuffer[i + offset] = time[i];
			}
			offset += time.length;
			for (int i = 0; i < jpeg.length; i++) {
				imageBuffer[i + offset] = jpeg[i];
			}
			sm.readImage(imageBuffer, motionDetected);
		}
	}

	private byte[] trim(byte[] jpeg, int pos) {
		byte[] image = new byte[pos + 1];
		for (int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		return image;
	}
}
