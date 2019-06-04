package gs;

import java.util.ArrayList;

//fonction qui lit un fichier de données pour en extraire la solution associée
public class ReadDataSoluce {

	private String fileName;
	private Soluce s;
	
	public ReadDataSoluce(String fileName) {
		this.fileName = fileName;
		read();
	}
	
	private void read() {

		//récupère le contenu du fichier sous forme de liste
		ArrayList<String> liste = new GestionFichier().readFile(Main.address+fileName+".txt");
		s = new Soluce(liste.get(0), Integer.parseInt(liste.get(1)));
		int numLigne = 2;
		while(numLigne < 2+Integer.parseInt(liste.get(1))) {
			Node n = new Node(liste.get(numLigne));
			s.addNode(n);
			numLigne++;
			
		}
		boolean natureSoluce = false;
		if(liste.get(numLigne).equals("valid")){
			natureSoluce = true;
		}
		int valObj = Integer.parseInt(liste.get(numLigne+1));
		long calcTime = Long.parseLong(liste.get(numLigne+2));
		String method = liste.get(numLigne+3);
		String freeField = liste.get(numLigne+4);
		
		s.addParams(natureSoluce, valObj, calcTime, method, freeField);			
	}
	
	
	public Soluce getSoluce() {
		return s;
	}

}
