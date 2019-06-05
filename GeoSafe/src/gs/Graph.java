package gs;
import java.util.ArrayList;

//classe pour representer les graphes
public class Graph {
	
	//parametres du graphes au noms explicites
	private String name;
	private int numNodes;
	private int numEdges;
	private int numEvacNodes;
	private String idSafeNode;
	//tab ces arcs
	private ArrayList<Edge> tabEdges = new ArrayList<Edge>();
	//tab des chemins
	private ArrayList<Path> tabPaths = new ArrayList<Path>();
	private Soluce soluce;
	
	//initialisation du graphe
	public Graph(String name) {
		this.name = name;
	}
		
	//fonction pour ajouter les parametres dans le graphe
	public void addParams(int numN, int numE, int numEvacN, String idSafeN) {
		numNodes = numN;
		numEdges = numE;
		numEvacNodes = numEvacN;
		idSafeNode = idSafeN;
	}
	
	//ajoute un arc dans le graph
	public void addEdge(String ligne) {
		Edge e = new Edge(ligne);
		tabEdges.add(e);
	}
	
	//recupere un arc du graph à partir des ids des 2 noeuds à son extremité
	private Edge getEdge(String nodeA, String nodeB) {
		for(Edge e : tabEdges) {
			//apparament les arcs n'ont pas de direction
			if(e.isEdge(nodeA, nodeB) || e.isEdge(nodeB, nodeA)) {
				return e;
			}
		}
		//normalement ça n'arrive pas
		System.out.println("arc "+nodeA+"-"+nodeB+" est introuvable");
		return new Edge("BUG");
	}
	
	//ajoute un chemin dans le graphe et definis sa liste d'arcs
	public void addPath(int numPath, String ligne) {
		Path p = new Path(numPath, ligne);
		tabPaths.add(p);
	}
	
	//chaque chemin constient une liste d'arcs
	//elle est remplie ici
	public void addListEdgePath() {
		for(Path p : tabPaths) {
			ArrayList<Edge> edgesPath = new ArrayList<Edge>();
			for(int i = 0; i+1 < p.getNodes().size(); i++) {
				String nodeA = p.getNodes().get(i);
				String nodeB = p.getNodes().get(i+1);
				Edge e = getEdge(nodeA,nodeB);
				edgesPath.add(e);
			}
			p.addTabEdges(edgesPath);
		}
	}
	
	public void addSoluce(Soluce s) {
		soluce = s;
	}
	
	//fonction pour afficher le graphe dans la console
	public void printGraph() {
		System.out.println("Graph "+name+" : ");
		System.out.println("numNodes = "+numNodes);
		System.out.println("numEdges = "+numEdges);
		System.out.println("numEvacNodes = "+numEvacNodes);
		System.out.println("idSafeNode = "+idSafeNode);
		System.out.println("tabEdges = "+tabEdges.size());
		System.out.println("tabPaths = "+tabPaths.size());
	}
	
	//fonction pour calculer la borne inferieure du graphe
	public double bornInf() {	
		double temps = 0;	
		//on calcule le temps maximal necessaire parmis les durées d'évacuation de chaques chemins
		for(Path p : tabPaths) {
			temps = Math.max(temps, p.getTime());
		}
		//impossible que tous les chemins aient finis d'évacuer avant ce moment
		return temps;
	}
	
	//fonction qui indique si la solution associée au graphe est valide/cohérente
	//affiche dans la console la validité de la solution
	public void isValid() {
		//d'abords, on calcule les taches associées à tous les chemins du graphe
		for(Path p : tabPaths) {
			String idStartNode = p.getFirstNode();
			Node n = soluce.getNode(idStartNode);
			p.calcTasks(n.getEvacRate(), n.getEvacStart());
		}
		//maxDecal = temps de fin de la derniere tache
		double maxDecal = 0;
		//on va parcourir tous les arcs
		for(Edge e : tabEdges) {
			//calcul de la capacité utilisé de l'arc
			//la solution ne peut pas etre valide si un arc a sa capacité dépassée à un moment
			if(!e.hasEnoughCapacityForTasks()) {
				System.out.println("la solution n'est PAS valide : un arc a sa capacité dépassée");
				//donc non valide
				return;
			}
			//si l'arc se termine au noeud d'évacuation :
			if(e.isEnd(idSafeNode)) {
				//dernier temps de fin parmis les taches de cet arc
				double maxDecalTache = 0;
				for(Task t : e.getListeTasks()) {
					maxDecalTache = Math.max(maxDecalTache, t.getDecalage()+t.getDuree()+e.getLength());
				}
				//maxDecal est donc le max parmi ces temps
				maxDecal = Math.max(maxDecal, maxDecalTache);
			}
		}
		if(soluce.getValObj() == maxDecal) {
			System.out.println("la solution est VALIDE (time = "+maxDecal+")");
		}
		else {
			System.out.println("la solution n'est PAS valide");
			System.out.println("maxDecal = "+maxDecal);
			System.out.println("valObj = "+soluce.getValObj());
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumEvacNodes() {
		return numEvacNodes;
	}
	
	public Soluce getSoluce() {
		return soluce;
	}
	
	//heuristique pour calculer la borne supérieure
	//méthode grossiere (voir calcBornSup2 pour une méthode plus élégante)
	//on contruit la solution en plaçant les noeuds de départ de chaques chemins
	//mais à chaque fois, on fait démarrer le chemin que lorsque le précédent est entierement terminé
	//du coup il n'y a aucuns conflits, mais le temps d'évacuation est très grand
	public void calcBornSup() {
		
		Long time = System.currentTimeMillis();
		Soluce s = new Soluce(name, numEvacNodes);
		
		int evacTime = 0;	
	
		for(Path p : tabPaths) {
			Node n = new Node(p.getFirstNode(),p.getOptiRate(),evacTime);
			evacTime += p.getTime();
			s.addNode(n);
		}
		time = System.currentTimeMillis()-time;
		s.addParams(true, evacTime, time, "heuristique borne sup", "") ;
		soluce = s;
		s.printSoluce();
	}
	
	//heuristique pour calculer la borne supérieure
	//meilleure que la premiere
	//le principe est d'ajouter les chemins dans un ordre fixé,
	//dès qu'il y a un conflit, on le regle soit par décalage du chemin, soit en réduisant le taux
	//ce choix est fait aléatoirement en utilisant la variable proba
	public double calcBornSup2(ArrayList<Integer> listPath, double proba) {
		Long time = System.currentTimeMillis();
		Soluce s = new Soluce(name, numEvacNodes);	
		//System.out.println(n+" chemins");
		for(int i: listPath) {
			Path p_i = tabPaths.get(i);
			//par défault, le chemin par de l'instant t=0
			Node n_i = new Node(p_i.getFirstNode(),p_i.getOptiRate(),0);
			//on calcule les taches du graphe apres l'ajout de ce chemin,
			//pour on va regarder les conflits que cela a provoqué
			p_i.calcTasks(n_i.getEvacRate(), n_i.getEvacStart());
			boolean pathNotOk = true;
			while(pathNotOk) {
				//la boucle tourne tant que tous les conflits n'ont pas été réglés
				boolean error = false;
				for(Edge e : p_i.getEdges()) {
					if(!e.hasEnoughCapacityForTasks()) {
						//cas où il y a un conflit
						error = true;
						Task t = e.getTask(p_i.getNumPath());
						//on supprime la tache qu'on vient d'ajouter
						p_i.delTasks();
						double tirage = Math.random();
						//on tire au sort pour savoir comment on va résoudre ce conflit
						if(tirage < proba) {
							//résolution par décalage du début de la tache
							double dispoTime = e.getAvailableTime(t);
							double decal = dispoTime-t.getDecalage();
							//System.out.println(e.toString()+" "+dispoTime+" "+decal);
							n_i = new Node(p_i.getFirstNode(),p_i.getOptiRate(),n_i.getEvacStart()+decal);
							p_i.calcTasks(n_i.getEvacRate(), n_i.getEvacStart());
							break;
						}
						else {
							//résolution par diminution du taux de la tache
							int dispoTaux = e.getAvailableTaux(t);
							//le taux disponible dans l'arc est nul, donc on est obligé de décaler
							if(dispoTaux == 0) {
								double dispoTime = e.getAvailableTime(t);
								double decal = dispoTime-t.getDecalage();
								//System.out.println(e.toString()+" "+dispoTime+" "+decal);
								n_i = new Node(p_i.getFirstNode(),p_i.getOptiRate(),n_i.getEvacStart()+decal);
							}
							else {
								n_i = new Node(p_i.getFirstNode(),dispoTaux,n_i.getEvacStart());
							}
							p_i.calcTasks(n_i.getEvacRate(), n_i.getEvacStart());
							break;
						}
					}
				}
				pathNotOk = error;
			}
			s.addNode(n_i);
		}
		//tous les chemins ont maintenanté été ajoutés et tous les conflits sont résolus
		//on calcule la valeur objectif
		double maxDecal = 0;
		for(Edge e : tabEdges) {
			if(e.isEnd(idSafeNode)) {
				double maxDecalTache = 0;
				for(Task t : e.getListeTasks()) {
					maxDecalTache = Math.max(maxDecalTache, t.getDecalage()+t.getDuree()+e.getLength());
				}
				maxDecal = Math.max(maxDecal, maxDecalTache);
			}
		}
		time = System.currentTimeMillis()-time;
		s.addParams(true, maxDecal, time, "heuristique borne sup 2", "") ;
		soluce = s;
		//s.printSoluce();
		return s.getValObj();
	}
	
	public void printSoluce() {
		soluce.printToTxt();
	}
}
