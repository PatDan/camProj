package client;

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
}
