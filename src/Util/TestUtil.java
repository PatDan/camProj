package Util;

public class TestUtil {

	public static void main(String[] args) {
		int a = 10;
		byte[] ab = Util.intToByteArray(a);
		int a2 = Util.byteToInt(ab);
		System.out.println(a);
		System.out.println(a2);
		
		long b = System.currentTimeMillis();
		System.out.println(b);
		byte[] bb = Util.longToByteArray(b);
		long b2 = Util.byteTolong(bb);
		System.out.println(b2);
		
	}

}
