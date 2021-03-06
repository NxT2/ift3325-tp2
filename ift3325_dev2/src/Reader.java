
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class Reader {

	private static BufferedReader in;
	
	public Reader(){

	}
	
	/**
	 * Lit le fichier et garde chaque ligne dans un ArrayList
	 * @return ArrayList<String>
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> read(String fileName) throws FileNotFoundException{
		System.out.println("Reading:" + fileName);
		
		ArrayList<String> lineList = new ArrayList<String>();
		String line = "";
		
		in = new BufferedReader(new FileReader(fileName));
		try{
			while((line = in.readLine()) != null){
				lineList.add(line.trim());
			}
			in.close();
		}catch(IOException e){
			System.out.println ("Erreur lors de la lecture : " + e.getMessage());
		}
		return lineList;
	}
}
