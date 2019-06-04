package gs;
import java.util.ArrayList;

public class Graph {
	
	private String name;
	private int numNodes;
	private int numEdges;
	private int numEvacNodes;
	private String idSafeNode;
	private ArrayList<Edge> tabEdges = new ArrayList<Edge>();
	private ArrayList<Path> tabPaths = new ArrayList<Path>();
	private Soluce soluce;
	
	public Graph(String name) {
		this.name = name;
	}
		
	
	public void addParams(int numNodes, int numEdges, int numEvacNodes, String idSafeNode) {
		this.numNodes = numNodes;
		this.numEdges = numEdges;
		this.numEvacNodes = numEvacNodes;
		this.idSafeNode = idSafeNode;
	}
	
	//ajoute un arc dans le graph
	public void addEdge(String ligne) {
		Edge e = new Edge(ligne);
		tabEdges.add(e);
	}
	
	private Edge getEdge(String nodeA, String nodeB) {
		for(Edge e : tabEdges) {
			if(e.isEdge(nodeA, nodeB) || e.isEdge(nodeB, nodeA)) {
				return e;
			}
		}
		System.out.println("arc "+nodeA+"-"+nodeB+" est introuvable");
		return new Edge("BUG");
	}
	
	//ajoute un chemin dans le graph et defini sa liste d'arcs
	public void addPath(int numPath, String ligne) {
		Path p = new Path(numPath, ligne);
		tabPaths.add(p);
	}
	
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
	
	public void printGraph() {
		System.out.println("Graph "+name+" : ");
		System.out.println("numNodes = "+numNodes);
		System.out.println("numEdges = "+numEdges);
		System.out.println("numEvacNodes = "+numEvacNodes);
		System.out.println("idSafeNode = "+idSafeNode);
		System.out.println("tabEdges = "+tabEdges.size());
		System.out.println("tabPaths = "+tabPaths.size());
	}
	
	public double bornInf() {	
		double temp = 0;	
		for(Path p : tabPaths) {
			temp = Math.max(temp, p.getTime());
		}
		return temp;
	}
	
	
	public void isValid() {
		for(Path p : tabPaths) {
			String idStartNode = p.getFirstNode();
			Node n = soluce.getNode(idStartNode);
			p.calcTasks(n.getEvacRate(), n.getEvacStart());
		}
		
		double maxDecal = 0;
		for(Edge e : tabEdges) {
			//calcul de la capacité utilisé de l'arc
			if(!e.hasEnoughCapacityForTasks()) {
				System.out.println("la solution n'est PAS valide : un arc a sa capacité dépassée");
				return;
			}
			
			if(e.isEnd(idSafeNode)) {
				double maxDecalTache = 0;
				for(Task t : e.getListeTasks()) {
					maxDecalTache = Math.max(maxDecalTache, t.getDecalage()+t.getDuree()+e.getLength());
				}
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
	
	//calcule la valeur obj du graph avec un ordre de chemins donnés
	public double calcBornSup2(ArrayList<Integer> listPath, double proba) {
		Long time = System.currentTimeMillis();
		Soluce s = new Soluce(name, numEvacNodes);	
		//System.out.println(n+" chemins");
		for(int i: listPath) {
			Path p_i = tabPaths.get(i);
			//System.out.println((100*((int)i/(int)n))+"%");
			Node n_i = new Node(p_i.getFirstNode(),p_i.getOptiRate(),0);
			p_i.calcTasks(n_i.getEvacRate(), n_i.getEvacStart());
			boolean pathNotOk = true;
			while(pathNotOk) {
				boolean error = false;
				for(Edge e : p_i.getEdges()) {
					if(!e.hasEnoughCapacityForTasks()) {
						error = true;
						Task t = e.getTask(p_i.getNumPath());
						p_i.delTasks();
						double tirage = Math.random();
						if(tirage < proba) {
							double dispoTime = e.getAvailableTime(t);
							double decal = dispoTime-t.getDecalage();
							//System.out.println(e.toString()+" "+dispoTime+" "+decal);
							n_i = new Node(p_i.getFirstNode(),p_i.getOptiRate(),n_i.getEvacStart()+decal);
							p_i.calcTasks(n_i.getEvacRate(), n_i.getEvacStart());
							break;
						}
						else {
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
