package gs;

import java.util.ArrayList;

//algorithme de recherche locale
public class RechercheLocale {
	
	private String addressGraph;
	
	//nombre de chemins
	private int nbrPaths;
	
	private double proba;
	
	//solution trouvée
	private Soluce soluce;
	
	public RechercheLocale(String addressG, int nbrP, double p) {	
		addressGraph = addressG;
		nbrPaths = nbrP;		
		proba = p;
	}
	
	//fonction récursive qui rappelle la recherche tant que la solution est améliorée
	//la recherche se fait en appelant la fonction calcBornSup2 de Graph
	//avec une liste de numéros correspondant à l'ordre des chemins à placer
	public double Recherche(double valObj, ArrayList<Integer> listPath) {
		//printList(listPath);
		//on crée une liste contenant les voisins de listPath
		ArrayList<ArrayList<Integer>> listVoisins  = calcVoisins(listPath);
		
		ArrayList<Integer> listBest = clone(listPath);
		double bestValObj = valObj;
		Graph g;
		
		for(ArrayList<Integer> voisin: listVoisins) {
			g = new ReadDataGraph(addressGraph).getGraph();
			//on calcule la valeur objectif pour chaques voisins
			double newValObj = g.calcBornSup2(voisin, proba);
			//System.out.print("    "+newValObj+" pour ");
			//printList(voisin);
			//on enregistre tout si la valeur a été améliorée
			if(newValObj<bestValObj) {
				bestValObj = newValObj;
				soluce = g.getSoluce();
				listBest = clone(voisin);
			}
		}
		//on a atteint un optimum local, plus d'améliorations
		if(bestValObj == valObj) {
			//System.out.print("Optimum local trouvé : ");
			//printList(listBest);
			//System.out.println("valObj = "+bestValObj);
			return bestValObj;
		}
		//sinon, on a encore amélioré la solution, donc on continue la recherche
		else {
			//System.out.print("amelioration : valObj = "+bestValObj+ " pour ");
			//printList(listBest);
			return Recherche(bestValObj, listBest);
		}
	}
	
	//on a une liste de l'ordre des chemins à ajouter -> [0,1,2,3,...]
	//on défini un voisin comme une liste dont 2 valeurs cotes à cotes on été interverties
	//chaque liste de taille n possède donc n voisins
	//cette fonction donne la liste des n voisins possibles d'une liste
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
	
	//copie une liste
	private ArrayList<Integer> clone(ArrayList<Integer> l){
		ArrayList<Integer> copy = new ArrayList<Integer>();	
		for(int i=0; i< l.size(); i++){
			copy.add(i);
		}
		return copy;
	}
	
	//affiche une liste dans la console
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
