
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Emetteur {

	private static String nomMachine;
	private static int numPort;
	public static int[] polynome = {1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1};
	private static ArrayList<String> dataList;

	//mettre testFlipBit a true pour simuler les insertions d'erreurs
	private static boolean testFlipBit = false;
	//mettre testLost a true pour simuler les trames perdues
	private static boolean testLost = false;
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException{
		if(args.length != 3){
			System.out.println("E> Erreur de syntaxe: <Nom_Machine> <Num_Port> <Nom_Fichier>");
		}
		else{
			dataList = Reader.read(args[2]);
			nomMachine = args[0];
			numPort = Integer.parseInt(args[1]);
			ArrayList<Trame> trameList = new ArrayList<Trame>();
			ArrayList<String> binTrameList = new ArrayList<String>();
			trameList = createTrames();
			binTrameList = binTrame(trameList);
			send(binTrameList);
		}
	}

	/**
	 * envoie toutes les trames de la liste de trame
	 * @param trameList
	 */
	private static void send(ArrayList<String> trames) throws UnsupportedEncodingException{
		try {
			ArrayList<String> trameList = trames;
			Socket socket = new Socket(nomMachine, numPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());

			//Reception
			String line = in.readLine();
			System.out.println(line);
			
			//Simulation d'erreurs
			if(testFlipBit){
				ArrayList<String> trameListErr = new ArrayList<String>();
				trameListErr = Tests.flipBit(trames);
				trameList = trameListErr;
			}
			if(testLost){
				ArrayList<String> trameListErr = new ArrayList<String>();
				trameListErr = Tests.lostTrame(trames);
				trameList = trameListErr;
			}
			
			//Emission
			for(int i=0; i<trameList.size();i++){
				//t1.sleep(1000);
				out.println(trameList.get(i));
				out.flush();
				if(i%3 == 0){
					line = in.readLine();
					System.out.println("E> Emetteur envoie: " + line);
				}
				out.println("fin");
				out.flush();
				socket.close();
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Genere les trames de type Information a partir du fichier
	 * @param Reader
	 * @return ArrayList<String>
	 */
	private static ArrayList<Trame> createTrames(){

		ArrayList<Trame> trameList = new ArrayList<Trame>();
		try{

			for(int i=0; i<dataList.size();i++){
				Trame t = new Trame();
				t.setType("i");	//les trames avec les donnees sont toutes de type information
				t.setNum(i % 8);
				t.setData(dataList.get(i));
				t.setCrc(computeCRC(t));

				trameList.add(t);
			}
		}catch(Exception e){

		}
		
		//Creer la trame de fin
		Trame end = new Trame("f","00","00");
		trameList.add(end);
		
		return trameList;
	}
	
	/**
	 * transforme toutes les trames en binaire
	 * @param trameList
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static ArrayList<String> binTrame(ArrayList<Trame> trameList) throws UnsupportedEncodingException{
		ArrayList<String> binTrameList = new ArrayList<String>();
		
		for(int i=0; i<trameList.size();i++){
			Trame t = trameList.get(i);
		String trameBin = t.getFlag() 
				+ bitStuffing(BinConverter.convertToBin(t.getType()))
				+ bitStuffing(BinConverter.convertToBin(t.getNum()))
				+ bitStuffing(BinConverter.convertToBin(t.getData()))
				+ bitStuffing(BinConverter.convertToBin(t.getCrc()))
				+ t.getFlag();
		
		binTrameList.add(trameBin);
		}
		return binTrameList;
	}

	/**
	 * Ajoute le bit stuffing a la trame
	 * @param trame
	 * @return String
	 */
	private static String bitStuffing(String t){
		String res = "";
		int count = 0;

		for(int i=0; i<t.length(); i++){
			if(t.charAt(i) == '1'){
				count++;
			}
			else{
				count = 0;
			}

			res = res + t.charAt(i);

			if(count == 5){
				res = res + '0';
				count = 0;
			}
		}

		return res;
	}

	/**
	 * calcule le CRC de la trame
	 * @param t
	 * @return String
	 */
	private static String computeCRC(Trame t) {
		String res = "";
		String msgStr = t.computeString();
		int i=0;
		int[] msg = new int[msgStr.length()+16];
		int[] div = new int[polynome.length];
		int[] remainder = new int[polynome.length];
		
		for(i=0; i<msgStr.length();i++){
			msg[i]=Integer.parseInt(msgStr.substring(i,i+1));
		}
		
		for(;i<msg.length;i++){
			msg[i]=0;
		}
		
		//dividende initial
		for(i=0;i<div.length;i++){
			div[i] = msg[i];
		}
		i++;
		int j=0;
		int k=0;
		do{
			remainder = divideXOR(div);
			
			//trouver le reste commence par combien de zeros
			k = 0;
			while(remainder[k] == 0 && k < remainder.length){
				k++;
			}
			j=0;
			//mettre le reste dans la dividende
			for(;k<remainder.length;k++){
				div[j]=remainder[k];
				j++;
			}
			//remplir la dividende
			for(;j<div.length;j++){
				if(i<msg.length){
					div[j]=msg[i];
					i++;
				}
				else{
					break;
				}
			}
		}while(i<msg.length);
		
		//mettre le reste en string
		for(j = 0; j<remainder.length;j++){
			res = res + String.valueOf(remainder[i]);
		}
		
		return res;
	}
	
	public static int[] divideXOR(int[] div){
		int reste[] = new int[polynome.length];
		
		for(int i=0; i<reste.length;i++){
			if(div[i] == polynome[i]){
				reste[i] = 0;
			}
			else{
				reste[i] = 1;
			}
		}
		
		return reste;
	}
}
