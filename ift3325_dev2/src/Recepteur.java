import java.io.IOException;
import java.net.ServerSocket;


public class Recepteur {

	public static ServerSocket socket;
	public static int numPort;
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Erreur de syntaxe: <Num_Port>");
		}
		else{
			numPort = Integer.parseInt(args[0]);
			connect();
			
		}
	}
	
	public static void connect(){
		try{
			socket = new ServerSocket(numPort);
			socket.accept();
		}catch(IOException e){
			
		}
	}

}
