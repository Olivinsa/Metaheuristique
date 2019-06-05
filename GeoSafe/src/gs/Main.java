package gs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	
	//adresse du dossier des instances
	public static String address = "/home/boulle/Bureau/Documents/Metaheuristique/Instances/";

	public static void main(String[] args) {
		
		Graph g = new ReadDataGraph("dense_10_30_3_5_I").getGraph();
		//g.printGraph();
		//Soluce s = new ReadDataSoluce("graphe-TD-sans-DL-sol").getSoluce();
		//s.printSoluce();
		//g.addSoluce(s);
		
		//Soluce s = new Diversification("sparse_10_30_3_10_I", 10,10,0.5).getSoluce();
		
		//g.addSoluce(s);
		//g.isValid();
		//g.printSoluce();
		//System.out.println(g.bornInf());
		//testMethodes("dense_10_30_3_5_I");
		//calcAverageTime();
	}
	
	//fonction utilisé pour tester les 3 méthodes sur un meme graphe
	private static void testMethodes(String fileName) {
		int nbrChemins = 10;
		double proba = 0.5;
		int nbrStartPoints = 10;
		Graph g = new ReadDataGraph(fileName).getGraph();
		ArrayList<Integer> listPath = new ArrayList<Integer>();
		for(int i =0; i< nbrChemins; i++) {
			listPath.add(i, i);
		}
		Long time = System.currentTimeMillis();
		double valObj1 = g.calcBornSup2(listPath, proba);
		Long t1 = System.currentTimeMillis()-time;
		time = System.currentTimeMillis();
		RechercheLocale rL = new RechercheLocale(fileName, nbrChemins, proba);
		double valObj2 = rL.Recherche(valObj1, listPath);
		Long t2 = System.currentTimeMillis()-time;
		time = System.currentTimeMillis();
		double valObj3 = new Diversification(fileName, nbrChemins,nbrStartPoints ,proba).getValObj();
		Long t3 = System.currentTimeMillis()-time;
		
		System.out.println("Fichier : "+fileName);
		System.out.println(" BornSup : "+valObj1+" en "+t1+" ms");
		System.out.println(" RechercheLocale : "+valObj2+" en "+t2+" ms");
		System.out.println(" Diversification : "+valObj3+" en "+t3+" ms");
	}
	
	//calcule la moyenne des temps de calculs des 3 méthodes sur tous les sparse, medium et dense
	private static void calcAverageTime() {
		int nbrChemins = 10;
		double proba = 0.5;
		int nbrStartPoints = 10;
		//temps des méthodes
		Long t1 = (long) 0;
		Long t2 = (long) 0;
		Long t3 = (long) 0;
		List<String> typeGraphs = Arrays.asList("sparse","medium","dense");
		for(String type : typeGraphs) {
			for(int i=1;i<10;i++) {
				String fileName = type+"_10_30_3_"+i+"_I";
				System.out.println(fileName);
				Graph g = new ReadDataGraph(fileName).getGraph();
				ArrayList<Integer> listPath = new ArrayList<Integer>();
				for(int j =0; j< nbrChemins; j++) {
					listPath.add(j, j);
				}
				
				Long time = System.currentTimeMillis();
				double valObj1 = g.calcBornSup2(listPath, proba);
				t1 += System.currentTimeMillis()-time;
				time = System.currentTimeMillis();
				RechercheLocale rL = new RechercheLocale(fileName, nbrChemins, proba);
				double valObj2 = rL.Recherche(valObj1, listPath);
				t2 += System.currentTimeMillis()-time;
				time = System.currentTimeMillis();
				double valObj3 = new Diversification(fileName, nbrChemins,nbrStartPoints ,proba).getValObj();
				t3 += System.currentTimeMillis()-time;
				
			}
			System.out.println(type+" : " );
			System.out.println(" BornSup en "+t1/10.0+" ms");
			System.out.println(" RechercheLocale  en "+t2/10.0+" ms");
			System.out.println(" Diversification en "+t3/10.0+" ms");
			t1 = (long) 0;
			t2 = (long) 0;
			t3 = (long) 0;
			
		}
		
		
	}
}
