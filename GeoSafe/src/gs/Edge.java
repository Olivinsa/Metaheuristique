package gs;

import java.util.ArrayList;
import java.util.Collections;

//classe désignant les arcs du graphe
public class Edge {
	
	//noeud de départ
	private String nodeStart;
	//noeud d'arrivé
	private String nodeStop;
	//date (inutilisée)
	//private int dueDate;
	//longueur de l'arc
	private int length;
	//capacité de l'arc
	private int capacity;
	//listes des taches appliquées sur l'arc
	private ArrayList<Task> tabTasks = new ArrayList<Task>();
	
	//constructeur de l'arc à partir d'une ligne 
	//utilisé pour la construction d'un graphe à partir des fichiers de données
	public Edge(String ligne) {
		String[] liste = ligne.split(" ");
		this.nodeStart = liste[0];
		this.nodeStop = liste[1];
		//this.dueDate = Integer.parseInt(liste[2]);
		this.length = Integer.parseInt(liste[3]);
		this.capacity = (int) Math.round(Integer.parseInt(liste[4]));
	}
	
	//fonction pour savoir si l'arc est celui désigné par les 2 id de noeuds donnés
	public boolean isEdge(String nodeA, String nodeB) {
		return nodeStart.equals(nodeA) && nodeStop.equals(nodeB);
	}
	
	//ajoute une tache dans l'arc
	public void addTask(int id,  int population, double decalage, int taux) {
		Task t = new Task(id, population, decalage, taux);
		//supprime la tache du meme id si elle existe
		delTask(id);
		//System.out.println("Arc "+nodeStart+"-"+nodeStop+" : "+"tache "+id+" duree:"+duree+" decalage:"+decalage);
		tabTasks.add(t);
	}
	
	//supprime une tache de l'arc
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
	
	//recupere le décalage de la tache donnée (par son id)
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
	
	//recupere la tache donnée (par son id)
	public Task getTask(int id) {
		for(Task t : tabTasks) {
			if(t.getId()==id) {
				return t;
			}
		}
		System.out.println("task "+id+" not found");
		return new Task(-1,0,0,0);
	}
	
	//indique si le noeud est l'une des extremités de l'arc
	public boolean isEnd(String node) {
		return node.equals(nodeStop) || node.equals(nodeStart);
	}
	
	public String toString() {
		return "Arc : "+nodeStart+"-"+nodeStop;
	}
	
	//verifie si la capacité de l'arc n'est pas dépassée par les taches qui l'utilise
	// -> true si la capacité n'est pas dépassée ; false sinon
	public boolean hasEnoughCapacityForTasks() {
		int MaxUsedCapacity = 0;
		ArrayList<Double> listeTimes = new ArrayList<Double>();
		//on fait un tab qui contient tous les temps de début et fin de taches
		for(Task t: getListeTasks()) {
			listeTimes.add(t.getDecalage());
			listeTimes.add(t.getDecalage()+t.getDuree());
		}
		//pour chaque temps, on somme les taux utilisés par les taches à ces temps
		for(double time: listeTimes) {
			int capTime = calcCurrentTaux(time);
			//capacité maximale utilisé pour l'arc sur toute la durée de l'evacuation
			MaxUsedCapacity = Math.max(MaxUsedCapacity, capTime);
		}
		if(MaxUsedCapacity <= capacity) {
			//la capacité n'est pas dépassée
			return true;
		}
		else {
			//la capacité est dépassée
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
		//on trie dans l'ordre des temps croissants cette liste
		Collections.sort(listeTimes);
		//pour chaque temps, on regarde si on peut placer la tache
		for(double time: listeTimes) {
			if(time >= t.getDecalage()) {
				//on ajoute la tache pour tester
				addTask(t.getId(), t.getPopulation(), time, t.getTaux());
				if(hasEnoughCapacityForTasks()) {
					//si la capacité n'est pas dépassée, on retire la tache et on donne le temps
					delTask(t.getId());
					return time;
				}
				else {
					//sinon on retire la tache et on teste avec le temps suivant
					delTask(t.getId());
				}
			}
		}
		//techniquement c'est impossible de ne pas trouver de temps disponible, 
		//car si on place la tache au dernier temps possible la capacité ne sera pas dépassée
		//ou alors la tache est trop "large" pour cet arc
		System.out.println("pas de moment dispo trouvé");
		System.exit(0);
		return -1;
	}
	
	//renvoie le taux pour ajouter une tache t dans l'arc
	//la tache est ajoutée à un temps fixe, mais du coup on essaye de faire varier son taux
	//pour qu'elle rentre dans l'arc
	public int getAvailableTaux(Task t) {
		
		double startTime = t.getDecalage();
		//le taux ne peut pas etre plus grand que le taux de base de la tache
		int maxTaux = Math.min(t.getTaux(), capacity-calcCurrentTaux(startTime));
		
		ArrayList<Double> listeTimes = new ArrayList<Double>();
		//on fait une liste qui contient tous les temps de début et fin de taches
		for(Task t1: getListeTasks()) {
			listeTimes.add(t1.getDecalage());
			listeTimes.add(t1.getDecalage()+t1.getDuree());
		}
		//on trie cette liste
		Collections.sort(listeTimes);
		//pour chaque temps, on regarde le taux necessaire pour que la tache se termine à ce temps
		for(double time: listeTimes) {
			if(time > startTime) {
				//on réduit le taux, en faisant le min de l'ancien et du taux disponible à ce temps là
				maxTaux = Math.min(maxTaux, capacity-calcCurrentTaux(time));
				if(maxTaux == 0) {
					//si maxtaux = 0, c'est qu'une autre tache va prendre toute la place apres celle qu'on veut placer,
					//et qu'il est alors impossible de placer la tache quel que soit le taux
					return 0;
				}
				//si la tache peut faire passer toute sa population avec ce taux avant le temps qu'on est en train de regarder
				//alors ce taux est convenable et on le renvoie
				if( (time-startTime)*maxTaux >= t.getPopulation()) {
					return maxTaux;
				}
			}
		}
		//on a vu tous les temps et la tache n'est jamais passé,
		//le taux renvoyé est donc le plus petit taux disponible dans l'arc apres le début de la tache
		//la tache peut utiliser ce taux pour que la capacité de l'arc ne soit jamais dépassée, 
		//elle ne peut pas utiliser un taux plus grand, et si elle utilise un taux plus petit elle va juste durer plus longtemps,
		//donc c'est bien le meilleur taux qu'elle peut utiliser
		return maxTaux;
					
		
		
	}
	
	//calcule la capacité utilisé par les taches de l'arc au moment donné
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
