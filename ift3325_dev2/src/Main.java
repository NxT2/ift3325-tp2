import java.io.UnsupportedEncodingException;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException {
		//Recepteur
		if(args.length==1){
			Recepteur recepteur = new Recepteur(args);
			recepteur.run();
		}
		//Emetteur
		else if(args.length==3){
			Emetteur emetteur = new Emetteur(args);
			emetteur.run();	
		}
		else{
			System.out.println("Erreur de syntaxe:\n"
								+"Pour utiliser Receveur: <Nom_fichier>\n"
								+"Pour utiliser Emetteur: <Nom_machine> <Num_port> <Nom_fichier>");
		}
	}

}
