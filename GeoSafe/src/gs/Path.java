package gs;

import java.util.ArrayList;

public class Path {
	
	private int numPath;
	private String idStartNode;
	private int population;
	private int maxRate;
	private int length;	
	private ArrayList<String> tabNodes = new ArrayList<String>();
	private ArrayList<Edge> tabEdges = new ArrayList<Edge>();
	
	public Path(int numPath, String ligne) {
		this.numPath = numPath;
		String[] liste = ligne.split(" ");
		this.idStartNode = liste[0];
		this.population = Integer.parseInt(liste[1]);
		this.maxRate = Integer.parseInt(liste[2]);
		this.length = Integer.parseInt(liste[3]);
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
	
	public void addTabEdges(ArrayList<Edge> tabEdges) {
		this.tabEdges = tabEdges;
	}
	
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
	
	public void calcTasks(int evacRate, double evacStart) {
		Edge firstEdge = tabEdges.get(0);
		firstEdge.addTask(numPath, population, evacStart, evacRate);
		for(int i = 1; i<tabEdges.size();i++) {
			Edge e = tabEdges.get(i);
			Edge e_precedent = tabEdges.get(i-1);
			e.addTask(numPath, population, e_precedent.getDecalage(numPath)+e_precedent.getLength(), evacRate);
		}
	}
	
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
