import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Recepteur implements Runnable{

	public static ServerSocket serverSocket;
	public static Socket socket;
	public static int numPort;
	private static BufferedReader in;
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("R> Erreur de syntaxe: <Num_Port>");
		}
		else{
			numPort = Integer.parseInt(args[0]);
			connect();
			
		}
	}
	
	public static void connect(){
		try{
			serverSocket = new ServerSocket(numPort);
			socket = serverSocket.accept();
		}catch(IOException e){
			
		}
	}

	public static void receive() throws IOException{
		String msg = "";
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		do{
			try{
				msg = in.readLine();
				if(checkEnd(msg)){
					
				}
				else{	//ce n'est pas la fin
					if(checkError(msg)){
						
					}
					else{	//pas d'erreur
						
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}while(true);
	}
	
	public static boolean checkEnd(String msg){
		
		return false;
	}
	
	public static boolean checkError(String msg){
		
		return false;
	}
	
	public void run() {
		
	}

}
