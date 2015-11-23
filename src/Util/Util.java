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
	
	public static byte[] longToByteArray(long data) {
		byte[] array = new byte[8];
		int index = 0;
		
		array[index++] = (byte) ((data & 0xff00000000000000L)>>56);
		array[index++] = (byte) ((data & 0x00ff000000000000L)>>48);
		array[index++] = (byte) ((data & 0x0000ff0000000000L)>>40);
		array[index++] = (byte) ((data & 0x000000ff00000000L)>>32);
		array[index++] = (byte) ((data & 0x00000000ff000000L)>>24);
		array[index++] = (byte) ((data & 0x0000000000ff0000L)>>16);
		array[index++] = (byte) ((data & 0x000000000000ff00L)>>8);
		array[index++] = (byte) ((data & 0x00000000000000ffL));

		return array;
	}
	
	public static long byteToLong(byte[] data) {
		long value = 0;
		for (int i = 0; i < data.length; i++)
		{
		   value = (value << 8) + (data[i] & 0xff);
		}
		return value;
	}
}
