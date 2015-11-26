package client;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Picture {
	private byte[] image;
	private boolean motion;
	private long time;
	
	public Picture (byte[] image, byte motion, long time) {
		this.image = image;
		this.motion = (motion == 1);
		this.time = time;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public boolean motion() {
		return motion;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getDate(){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
	    return format.format(date);
	}
}
