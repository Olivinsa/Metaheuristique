package gs;

import java.util.ArrayList;
import java.util.Collections;

public class Edge {
	
	private String nodeStart;
	private String nodeStop;
	//private int dueDate;
	private int length;
	private int capacity;
	private ArrayList<Task> tabTasks = new ArrayList<Task>();
	
	public Edge(String ligne) {
		String[] liste = ligne.split(" ");

		this.nodeStart = liste[0];
		this.nodeStop = liste[1];
		//this.dueDate = Integer.parseInt(liste[2]);
		this.length = Integer.parseInt(liste[3]);
		this.capacity = (int) Math.round(Integer.parseInt(liste[4]));
	}
	
	public boolean isEdge(String nodeA, String nodeB) {
		return nodeStart.equals(nodeA) && nodeStop.equals(nodeB);
	}
	
	public void addTask(int id,  int population, double decalage, int taux) {
		Task t = new Task(id, population, decalage, taux);
		//supprime la tache du meme id si elle existe
		delTask(id);
		//System.out.println("Arc "+nodeStart+"-"+nodeStop+" : "+"tache "+id+" duree:"+duree+" decalage:"+decalage);
		tabTasks.add(t);
	}
	
	
	public void delTask(int id) {
		for(int i =0; i<tabTasks.size(); i++) {
			Task t = tabTasks.get(i);
			if(t.getId()==id) {
				tabTasks.remove(t);
			}
		}
	}
	
	public int getLength() {
		return length;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public double getDecalage(int id) {
		for(Task t : tabTasks) {
			if(t.getId()==id) {
				return t.getDecalage();
			}
		}
		System.out.println("task "+id+" not found");
		return -1;
	}
	
	public ArrayList<Task> getListeTasks(){
		return tabTasks;
	}
	
	public Task getTask(int id) {
		for(Task t : tabTasks) {
			if(t.getId()==id) {
				return t;
			}
		}
		System.out.println("task "+id+" not found");
		return new Task(-1,0,0,0);
	}
	
	public boolean isEnd(String node) {
		return node.equals(nodeStop) || node.equals(nodeStart);
	}
	
	public String toString() {
		return "Arc : "+nodeStart+"-"+nodeStop;
	}
	
	//verifie si la capacité de l'arc n'est pas dépassée par les taches qui l'utilise
	public boolean hasEnoughCapacityForTasks() {
		int MaxUsedCapacity = 0;
		ArrayList<Double> listeTimes = new ArrayList<Double>();
		//on fait un tab qui contient tous les temps de début et fin de taches
		for(Task t: getListeTasks()) {
			listeTimes.add(t.getDecalage());
			listeTimes.add(t.getDecalage()+t.getDuree());
		}
		//pour chaque temps, on somme les taux utilisés par la taches à ces temps
		for(double time: listeTimes) {
			int capTime = calcCurrentTaux(time);
			//capacité maximale utilisé pour l'arc sur toute la durée de l'evacuation
			MaxUsedCapacity = Math.max(MaxUsedCapacity, capTime);
		}
		if(MaxUsedCapacity <= capacity) {
			return true;
		}
		else {
			//System.out.println(toString()+" capacity ="+capacity+" MaxUsedCapacity = "+MaxUsedCapacity );
			for(Task t: getListeTasks()) {
				//System.out.println("    Tache "+t.getId()+" debut "+t.getDecalage()+ "  fin "+(t.getDecalage()+t.getDuree())+"  taux "+t.getTaux());
			}
			return false;
		}
	}
	
	//renvoie le premier moment disponible pour ajouter une tache t dans l'arc 
	public double getAvailableTime(Task t) {
		ArrayList<Double> listeTimes = new ArrayList<Double>();
		//on fait un tab qui contient tous les temps de début et fin de taches
		for(Task t1: getListeTasks()) {
			listeTimes.add(t1.getDecalage());
			listeTimes.add(t1.getDecalage()+t1.getDuree());
		}
		Collections.sort(listeTimes);
		//pour chaque temps, on regarde si on peut placer la tache
		for(double time: listeTimes) {
			if(time >= t.getDecalage()) {
				addTask(t.getId(), t.getPopulation(), time, t.getTaux());
				if(hasEnoughCapacityForTasks()) {
					delTask(t.getId());
					return time;
				}
				else {
					delTask(t.getId());
				}
			}
		}
		System.out.println("pas de moment dispo trouvé");
		System.exit(0);
		return -1;
	}
	
	//renvoie le taux pour ajouter une tache t dans l'arc
	public int getAvailableTaux(Task t) {
		
		double startTime = t.getDecalage();
		//le taux ne peut pas etre plus grand que le taux de base de la tache
		int maxTaux = Math.min(t.getTaux(), capacity-calcCurrentTaux(startTime));
		
		ArrayList<Double> listeTimes = new ArrayList<Double>();
		//on fait un tab qui contient tous les temps de début et fin de taches
		for(Task t1: getListeTasks()) {
			listeTimes.add(t1.getDecalage());
			listeTimes.add(t1.getDecalage()+t1.getDuree());
		}
		Collections.sort(listeTimes);
		//pour chaque temps, on regarde si on peut placer la tache
		for(double time: listeTimes) {
			if(time > startTime) {
				
				maxTaux = Math.min(maxTaux, capacity-calcCurrentTaux(time));
				if(maxTaux == 0) {
					return 0;
				}
				if( (time-startTime)*maxTaux >= t.getPopulation()) {
					return maxTaux;
				}
			}
		}
		return maxTaux;
					
		
		
	}
	
	//calcule le taux utilisé à ce temps là
	public int calcCurrentTaux(double time) {
		int currenCap = 0;
		for(Task t: getListeTasks()) {
			if(t.getDecalage()<=time && t.getDecalage()+t.getDuree()>time) {
				currenCap+=t.getTaux();
			}
		}
		return currenCap;
	}
}
