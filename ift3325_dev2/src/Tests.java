import java.util.ArrayList;
import java.util.Random;


public class Tests {

	/**
	 * inverse un bit aleatoirement d'une liste de trames donnees
	 * @param trameList 
	 * @return
	 */
	public static ArrayList<String> flipBit(ArrayList<String> trameList){
		ArrayList<String> trames = trameList;
		
		//nous allons creer un erreur si flip = 0
		int flip = new Random().nextInt(2);
		if(flip == 0){
			//generer aleatoirement le bit a inverser
			int trameNum = new Random().nextInt(trameList.size());	//trame qui est erronnee
			int charNum = new Random().nextInt(trames.get(trameNum).length());	//charactere qui est errone
			
			//inverser le bit
			String newS = trames.get(trameNum);
			newS = newS.substring(0, charNum)
					+flipBit(trames.get(trameNum).charAt(charNum))
					+newS.substring(charNum+1);
		}
		
		return trames;
	}
	
	private static char flipBit(char bit){
		if(bit == 1){
			return 0;
		}
		else{
			return 1;
		}
	}
}
