package gs;

import java.util.ArrayList;
import java.util.Collections;

//classe appelée par le main pour effectuer la méthode de diversification
public class Diversification {
	
	//meilleur solution trouvée par l'algo
	private Soluce soluce;
	
	//on donne l'adresse du fichier de données pour reconstruire le graphe
	//le nombres de chemins
	//le nombre de points de départ pour la diversification
	//proba est une valeur entre 0 et 1
	//cela indique la tendence de l'algo a choisir de résoudre les conflits par des décalages de temps de départ des taches
	//ou en changeant les taux des taches
	//proba = 1 -> decalages uniquement; proba = 0 -> changement des taux uniquement ; 0.5 -> 50% de chaque
	public Diversification(String addressG, int nbrPaths, int nbrStartPoints, double proba) {
		//creation de la liste qui correspond à l'ordre que l'on va utiliser pour ajouter les taches des chemins dans le graphe
		//cet ordre va determiner l'apparition des conflits de capacité lors de la construction de la solution
		ArrayList<Integer> listPath = new ArrayList<Integer>();
		for(int i =0; i< nbrPaths; i++) {
			listPath.add(i, i);
		}
		//ordre qui a amené à la meilleur solution (initialisation)
		ArrayList<Integer> listBest = clone(listPath);
		//valeur objectif de la solution qu'on essaie de diminuer le plus possible
		double bestValObj = Double.MAX_VALUE;
		//chacuns des points de départ de la diversification correspond en fait à une liste d'ordre des chemins
		//cette liste est un mélange aléatoire de la liste [0,1,2,3,4,5,...]
		for(int i = 0; i< nbrStartPoints; i++) {
			//creation de la liste
			ArrayList<Integer> copie = clone(listPath);
			//mélangeage de la liste
			Collections.shuffle(copie);
			//on applique la recherche locale sur cette liste
			RechercheLocale r = new RechercheLocale(addressG, nbrPaths, proba);
			//on calcule la valeur objective de cette recherche
			double valObj = r.Recherche(Double.MAX_VALUE, copie);
			//System.out.print("    "+valObj+" en partant de ");
			//printList(copie);
			//si la valeur est meilleur que celle qu'on a en mémoire, on la remplace 
			//et on retient la liste qui y a mené
			if(valObj < bestValObj) {
				bestValObj = valObj;
				listBest = copie;
				//on maj la soluce enregistrée
				soluce = r.getSoluce();
			}

		}
		//fin, on affiche la soluce et on la print en txt
		//System.out.print("Solution : "+bestValObj+" pour ");
		//printList(listBest);
		soluce.printToTxt();
		
	}
	
	//clone la liste pour eviter des bugs de duplication
	private ArrayList<Integer> clone(ArrayList<Integer> l){
		ArrayList<Integer> copy = new ArrayList<Integer>();	
		for(int i=0; i< l.size(); i++){
			copy.add(i);
		}
		return copy;
	}
	
	//fonction pour afficher la liste dans la console
	private void printList(ArrayList<Integer> list) {
		for(int i:list) {
			System.out.print(i+" ");
		}
		System.out.println("");
	}
	
	public Soluce getSoluce() {
		return soluce;
	}
	
	public double getValObj() {
		return soluce.getValObj();
	}

}
