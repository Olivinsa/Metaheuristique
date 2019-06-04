package gs;


public class Main {
	
	//adresse du dossier des instances
	public static String address = "/home/boulle/Bureau/Documents/Metaheuristique/Instances/";

	public static void main(String[] args) {
		//g.printGraph();
		//Soluce s = new ReadDataSoluce("graphe-TD-sans-DL-sol").getSoluce();
		//s.printSoluce();
		//g.addSoluce(s);
		Soluce s = new Diversification("sparse_10_30_3_10_I", 10,10,0.5).getSoluce();
		Graph g = new ReadDataGraph("sparse_10_30_3_10_I").getGraph();
		g.addSoluce(s);
		g.isValid();
		//System.out.println("validated");
		//g.printSoluce();
		//System.out.println(g.bornInf());
	}
}
