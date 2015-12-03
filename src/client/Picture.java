package client;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Picture {
	private byte[] image;
	private boolean motion;
	private long time;

	/**
	 * Contains information about the image
	 * 
	 * @param image
	 *            - the image in a byte array
	 * @param motion
	 *            - the flag for motion. 1 for motion detected, else 0
	 * @param time
	 *            - the time the image was fetched from the camera
	 */
	public Picture(byte[] image, byte motion, long time) {
		this.image = image;
		this.motion = (motion == 1);
		this.time = time;
	}

	/**
	 * @return - the image as a byte array
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @return - true if motion, else false
	 */
	public boolean motion() {
		return motion;
	}

	/**
	 * @return - the time the image was fetched from the camera
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return - the time the image was fetched from the camera in format
	 * 				YYYY MM DD HH:MM:SS
	 */
	public String getDate() {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		return format.format(date);
	}
}
