import java.io.UnsupportedEncodingException;

public class BinConverter {

	public static String convertToBin(String s) throws UnsupportedEncodingException{
		byte[] b = s.getBytes("UTF-8");
		return Integer.toBinaryString(b[0]);
	}
	public static String convertToBin(int i){
		return Integer.toBinaryString(i);
	}
}
