package gs;

import java.util.ArrayList;

public class ReadDataGraph {
	
	private String fileName;
	private Graph g;
	
	public ReadDataGraph(String fileName) {
		this.fileName = fileName;
		read();
	}
	
	private void read() {
		g = new Graph(fileName);
		
		//récupère le contenu du fichier sous forme de liste
		ArrayList<String> liste = new GestionFichier().readFile(Main.address+fileName+".full");
		int numEvacNodes = Integer.parseInt(liste.get(1).split(" ")[0]);
		String idSafeNode = liste.get(1).split(" ")[1];
		int numLigne = 2;
		int numPath = 1;
		while(!liste.get(numLigne).matches("(.*)[graph](.*)")) {
			//System.out.println("debug : "+liste.get(numLigne));
			g.addPath(numPath, liste.get(numLigne));
			numLigne++;
			numPath++;
		}
		//on est a la ligne c [graph] ...
		numLigne++;
		int numNodes = Integer.parseInt(liste.get(numLigne).split(" ")[0]);
		int numEdges = Integer.parseInt(liste.get(numLigne).split(" ")[1]);
		numLigne++;
		while(numLigne < liste.size()) {
			g.addEdge(liste.get(numLigne));
			numLigne++;
		}
		g.addParams(numNodes, numEdges, numEvacNodes, idSafeNode);
		g.addListEdgePath();
	}
	
	public Graph getGraph() {
		return g;
	}
}
