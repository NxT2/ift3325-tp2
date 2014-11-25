import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Recepteur implements Runnable{

	public static ServerSocket serverSocket;
	public static Socket socket;
	public static int numPort;
	private static BufferedReader in;
	private static PrintWriter out;
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("R> Erreur de syntaxe: <Num_Port>");
		}
		else{
			numPort = Integer.parseInt(args[0]);
			connect();
			
		}
	}
	
	/**
	 * etablie la connexion
	 */
	public static void connect(){
		try{
			serverSocket = new ServerSocket(numPort);
			socket = serverSocket.accept();
			out = new PrintWriter(socket.getOutputStream());
		}catch(IOException e){
			
		}
	}

	/**
	 * fais l'action de recevoir les trames
	 * @throws IOException
	 */
	public static void receive() throws IOException{
		String msg = "";
		int count = 0;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		do{
			try{
				msg = in.readLine();
				if(checkEnd(msg)){
					System.out.println("R> Fin de Transmission");
					break;
				}
				else{	//ce n'est pas la fin
					if(isError(msg)){
						System.out.println("R> Recu: Trame erronnée. REJ");
						sendRej(msg);
					}
					else{	//pas d'erreur
						System.out.println("R> Recu: " + convertBin(msg));
						count++;
						if(count%3==0){
							sendAck(msg);
						}
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}while(true);
	}
	
	/**
	 * verifie si c'est la fin du message
	 * @param msg
	 * @return
	 */
	public static boolean checkEnd(String msg){
		boolean end = false;
		String type = msg.substring(8, 17);
		if(convertBin(type).equals("f")){
			end = true;
		}
		return end;
	}
	
	/**
	 * indique s'il y a une erreur dans le message
	 * @param msg
	 * @return
	 */
	public static boolean isError(String msg){
		
		
		
		return false;
	}
	
	/**
	 * enleve le bit stuffing
	 * @return
	 */
	public static String unBitStuff(String s){
		String res = "";
		int count = 0;
		
		for(int i=0;i<s.length();i++){
			if(s.charAt(i) == '1'){ 
				count++;
				res = res +s.charAt(i); }
			else{
				res = res + s.charAt(i);
				count = 0; 
			}
			if(count == 5 ){
				s = removeCharAt(s,i+1);
				count = 0; 
			}
		}
		return res;
	}
	
	/**
	 * enleve un caractere d'une string
	 * @param s
	 * @param pos
	 * @return
	 */
	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}
	
	/**
	 * converti une suite binaire en String
	 * @param msg
	 * @return
	 */
	private static String convertBin(String msg){
		String res = "";
		String temp = msg.substring(8, msg.length()-8);
		String temp1 = unBitStuff(temp);
		String temp3 = unBitStuff (temp1.substring(16, temp1.length()-16));
		
		for (int i = 0 ; i<temp3.length(); i+=8){
			res += (char) Integer.parseInt(temp3.substring(i,i+8),2);
		}
		return res;
	}
	
	private static void sendRej(String msg){
		String rej = msg.substring(0,8)+ "Rej" +msg.substring(16,24) +msg.substring(msg.length()-24, msg.length());
		out.println(rej);
		out.flush();
	}
	
	private static void sendAck(String msg){
		String rr = msg.substring(0,8)+ "RR" +msg.substring(16,24) +msg.substring(msg.length()-24, msg.length());
		out.println(rr);
		out.flush();
	}
	
	public void run() {
		
	}

}
