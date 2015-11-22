package Util;

public class Util {
	public static byte[] intToByteArray(int data) {

		byte[] result = new byte[4];

		result[0] = (byte) ((data & 0xFF000000) >> 24);
		result[1] = (byte) ((data & 0x00FF0000) >> 16);
		result[2] = (byte) ((data & 0x0000FF00) >> 8);
		result[3] = (byte) ((data & 0x000000FF) >> 0);

		return result;
	}
	
	public static int byteToInt(byte[] data) {
		int i= (data[0]<<24)&0xff000000|
			       (data[1]<<16)&0x00ff0000|
			       (data[2]<< 8)&0x0000ff00|
			       (data[3]<< 0)&0x000000ff;
		return i;
	}
}
