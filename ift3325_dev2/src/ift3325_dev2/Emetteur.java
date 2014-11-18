package ift3325_dev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class Emetteur {

	public static void main(String[] args) throws UnsupportedEncodingException{
		if(args.length != 3){
			System.out.println("Erreur: Les paramètres sont <Nom_machine> <Num_Port> <Nom_Fichier>");
		}
		else{
			String nomMachine = args[0];
			int numPort = Integer.parseInt(args[1]);
			Reader reader = new Reader(args[2]);

			ArrayList<String> trameList = createTrames(reader);
			send(nomMachine, numPort, trameList);
		}
	}

	/**
	 * envoie toutes les trames
	 * @param trameList
	 */
	private static void send(String nomMachine, int numPort, ArrayList<String> trameList) throws UnsupportedEncodingException{
		try {
			Socket socket = new Socket(nomMachine, numPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			//Reception
			String line = in.readLine();
			System.out.println(line);
			
			//Emission
			for(int i=0; i<trameList.size();i++){
				//t1.sleep(1000);
				out.println(trameList.get(i));
				out.flush();
				if(i%3 == 0){
					line = in.readLine();
					System.out.println("Emetteur recoit: " + line);
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
	 * Genere les trames a partir du fichier
	 * @param Reader
	 * @return ArrayList<String>
	 */
	private static ArrayList<String> createTrames(Reader reader){
		ArrayList<String> trameList = new ArrayList<String>();
		try{
			ArrayList<String> dataList = reader.read();

			for(int i=0; i<dataList.size();i++){
				Trame t = new Trame();
				t.setType(generateType());
				t.setNum(i % 8);
				t.setData(dataList.get(i));
				t.setCrc(computeCRC(t));
				
				String trameBin = t.getFlag() 
						+ bitStuffing(t.getTypeBin())
						+ bitStuffing(t.getNumBin())
						+ bitStuffing(t.getDataBin())
						+ bitStuffing(t.getCRCBin())
						+ t.getFlag();
				
				trameList.add(trameBin);
			}
		}catch(Exception e){

		}
		return trameList;
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
		
		//TODO
		
		return res;
	}

	/**
	 * Genere le type de la trame aleatoirement
	 * @return String
	 */
	private static String generateType() {
		String res = "";
		int rand = new Random().nextInt(4);
		if(rand == 0){
			res="i";
		}
		else if(rand == 1){
			res="c";
		}
		else if(rand == 2){
			res="f";
		}
		else if(rand == 3){
			res="p";
		}
		return res;
	}
}
