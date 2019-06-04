package gs;

import java.util.ArrayList;

public class RechercheLocale {
	
	private String addressGraph;
	
	private int nbrPaths;
	
	private double proba;
	
	private Soluce soluce;
	
	public RechercheLocale(String addressG, int nbrP, double p) {	
		addressGraph = addressG;
		nbrPaths = nbrP;		
		proba = p;
	}
	
	public double Recherche(double valObj, ArrayList<Integer> listPath) {
		//printList(listPath);
		ArrayList<ArrayList<Integer>> listVoisins  = calcVoisins(listPath);
		
		ArrayList<Integer> listBest = clone(listPath);
		double bestValObj = valObj;
		Graph g;
		
		for(ArrayList<Integer> voisin: listVoisins) {
			g = new ReadDataGraph(addressGraph).getGraph();
			double newValObj = g.calcBornSup2(voisin, proba);
			System.out.print("    "+newValObj+" pour ");
			printList(voisin);
			if(newValObj<bestValObj) {
				bestValObj = newValObj;
				soluce = g.getSoluce();
				listBest = clone(voisin);
			}
		}
		//on a atteint un optimum local
		if(bestValObj == valObj) {
			//System.out.print("Optimum local trouvé : ");
			
			//printList(listBest);
			//System.out.println("valObj = "+bestValObj);
			return bestValObj;
		}
		else {
			//System.out.print("amelioration : valObj = "+bestValObj+ " pour ");
			//printList(listBest);
			return Recherche(bestValObj, listBest);
		}
		
	}
	
	private ArrayList<ArrayList<Integer>> calcVoisins(ArrayList<Integer> list) {
		ArrayList<ArrayList<Integer>> listeVoisins = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<nbrPaths-1;i++) {
			ArrayList<Integer> lvoisine = clone(list);
			int temp = lvoisine.get(i);
			lvoisine.set(i, lvoisine.get(i+1));
			lvoisine.set(i+1,temp);
			listeVoisins.add(lvoisine);
		}
		ArrayList<Integer> lvoisine = clone(list);
		int temp = lvoisine.get(nbrPaths-1);
		lvoisine.set(nbrPaths-1,lvoisine.get(0));
		lvoisine.set(0,temp);
		listeVoisins.add(lvoisine);
		return listeVoisins;
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
