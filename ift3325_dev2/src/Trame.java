import java.io.UnsupportedEncodingException;


public class Trame {

	private final String  FLAG = "01111110";
	private String type = "";
	private int num = 0;
	private String data = "";
	private String crc = "";
	
	public Trame(){
		
	}

	//Getters et Setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}
	public String getFlag(){
		return this.FLAG;
	}
}
