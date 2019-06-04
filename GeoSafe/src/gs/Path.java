package gs;

import java.util.ArrayList;

//classe représentant un chemin d'évacuation du graphe
public class Path {
	
	private int numPath;
	private String idStartNode;
	private int population;
	private int maxRate;
	private int length;	
	//liste des ids des noeuds de ce chemin
	private ArrayList<String> tabNodes = new ArrayList<String>();
	//liste des arcs de ce chemin
	private ArrayList<Edge> tabEdges = new ArrayList<Edge>();
	
	//on donne un numéro d'identification à chaque chemins
	//construction a partir d'une ligne du fichier d'un graphe
	public Path(int numP, String ligne) {
		numPath = numP;
		String[] liste = ligne.split(" ");
		idStartNode = liste[0];
		population = Integer.parseInt(liste[1]);
		maxRate = Integer.parseInt(liste[2]);
		length = Integer.parseInt(liste[3]);
		int numParam = 4;
		tabNodes.add(idStartNode);
		while(numParam < liste.length) {
			tabNodes.add(liste[numParam]);
			numParam++;
		}
	}
	
	public ArrayList<String> getNodes(){
		return tabNodes;
	}
	
	public ArrayList<Edge> getEdges(){
		return tabEdges;
	}
	
	public void addTabEdges(ArrayList<Edge> tabE) {
		tabEdges = tabE;
	}
	
	//recupere le temps necessaire pour évacuer ce chemin
	public double getTime() {
		double time = 0;
		for(Edge e: tabEdges) {
			time+=e.getLength();
		}
		return time+(double)population/(double)maxRate;
	}

	public String getFirstNode() {
		return idStartNode;
	}
	
	//calcule les taches du chemin
	//met à jour la liste des taches des arcs de ce chemin
	public void calcTasks(int evacRate, double evacStart) {
		Edge firstEdge = tabEdges.get(0);
		firstEdge.addTask(numPath, population, evacStart, evacRate);
		for(int i = 1; i<tabEdges.size();i++) {
			Edge e = tabEdges.get(i);
			Edge e_precedent = tabEdges.get(i-1);
			e.addTask(numPath, population, e_precedent.getDecalage(numPath)+e_precedent.getLength(), evacRate);
		}
	}
	
	//supprime toutes les taches de ce chemin
	public void delTasks() {
		for(Edge e:tabEdges) {
			e.delTask(numPath);
		}
	}
	
	public String toString() {
		String msg = "Path "+numPath+" : \n";
		int num = 1;
		for(Edge e: tabEdges) {
			String inter = "Arc "+num+" : \n";
			for(Task t : e.getListeTasks()) {
				inter+= "Tache "+t.getId()+" : duree="+t.getDuree()+", decalage="+t.getDecalage()+"\n";
			}
			msg+=inter;
			num++;
		}
		return msg;
	}
	
	public int getMaxRate() {
		return maxRate;
	}
	
	//parfois dans les données, le taux maximal du chemin était plus élevé que la capacité de certains arc qu'il traversait
	//on calcule donc un maxrate plus réaliste pour qu'il puisse au moins passer dans chacuns de ses arcs
	public int getOptiRate() {
		int optiRate = Integer.MAX_VALUE;
		for(Edge e:tabEdges) {
			optiRate = Math.min(optiRate, e.getCapacity());
		}
		return optiRate;
	}
	
	public int getNumPath() {
		return numPath;
	}
	
}
