package server;

import Util.Util;
//import se.lth.cs.eda040.fakecamera.AxisM3006V;
import se.lth.cs.eda040.proxycamera.AxisM3006V;

public class ReadImageThread extends Thread {
	private ServerMonitor sm;
	private int camNbr;
	private int port;

	/**
	 * Reads images from the camera
	 * 
	 * @param sm
	 *            - the server monitor to add the images
	 * @param camNbr
	 *            - the number of the camera to be used. Using
	 *            argus-camNbr.student.lth.se
	 * @param port
	 *            - the port used on the camera
	 */
	public ReadImageThread(ServerMonitor sm, int camNbr, int port) {
		this.sm = sm;
		this.camNbr = camNbr;
		this.port = port;
	}

	/**
	 * Connects to a camera and fetches images until interruption. Detects
	 * motion.
	 */
	public void run() {
		AxisM3006V cam = new AxisM3006V();
		cam = new AxisM3006V();
		cam.init();
		String hostname = "argus-" + camNbr + ".student.lth.se";
		cam.setProxy(hostname, port); // This is for proxy camera
		cam.connect();
		while (sm.isConnected()) {
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
		
		cam.close();
		cam.destroy();
	}

	/**
	 * A method to extract the image from the package.
	 * 
	 * @param jpeg
	 * @param pos
	 * @return image
	 */
	private byte[] trim(byte[] jpeg, int pos) {
		byte[] image = new byte[pos + 1];
		for (int i = 0; i <= pos; i++) {
			image[i] = jpeg[i];
		}
		return image;
	}
}
