package gs;

import java.util.ArrayList;
import java.util.Collections;

public class Diversification {
	
	private Soluce soluce;
	
	public Diversification(String addressG, int nbrPaths, int nbrStartPoints, double proba) {
		//proba = 1 -> decalages uniquement
		//proba = 0 -> changement des taux
		ArrayList<Integer> listPath = new ArrayList<Integer>();
		for(int i =0; i< nbrPaths; i++) {
			listPath.add(i, i);
		}
		ArrayList<Integer> listBest = clone(listPath);
		double bestValObj = Double.MAX_VALUE;
		
		for(int i = 0; i< nbrStartPoints; i++) {
			ArrayList<Integer> copie = clone(listPath);
			Collections.shuffle(copie);
			RechercheLocale r = new RechercheLocale(addressG, nbrPaths, proba);
			double valObj = r.Recherche(Double.MAX_VALUE, copie);
			//System.out.print("    "+valObj+" en partant de ");
			printList(copie);
			if(valObj < bestValObj) {
				bestValObj = valObj;
				listBest = copie;
				soluce = r.getSoluce();
			}

		}
		
		System.out.print("Solution : "+bestValObj+" pour ");
		printList(listBest);
		soluce.printToTxt();
		
	}
	
	private ArrayList<Integer> clone(ArrayList<Integer> l){
		ArrayList<Integer> copy = new ArrayList<Integer>();	
		for(int i=0; i< l.size(); i++){
			copy.add(i);
		}
		return copy;
	}
	
	private void printList(ArrayList<Integer> list) {
		for(int i:list) {
			System.out.print(i+" ");
		}
		System.out.println("");
	}
	
	public Soluce getSoluce() {
		return soluce;
	}

}
