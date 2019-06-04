package gs;

import java.util.ArrayList;

public class Soluce {
	
	private String instanceName;
	private int nbEvacNodes;
	//liste des noeuds à evacuer
	private ArrayList<Node> listNodes;
	private boolean natureSoluce;
	private double valObj; //valeur de la fonction objective
	private long calcTime;
	private String method;
	private String freeField;
	
	public Soluce(String instanceName, int nbEvacNodes) {
		this.instanceName = instanceName;
		this.nbEvacNodes = nbEvacNodes;
		listNodes = new ArrayList<Node>();
	}
	
	public void addNode(Node n) {
		listNodes.add(n);
	}
	
	public void addParams(boolean natureSoluce, double valObj, long calcTime, String method, String freeField) {
		
		this.natureSoluce = natureSoluce;
		this.valObj = valObj;
		this.calcTime = calcTime;
		this.method = method;
		this.freeField = freeField;
	}
	
	public void printSoluce() {
		System.out.println("Soluce "+instanceName+" : ");
		System.out.println("nbEvacNodes = "+nbEvacNodes);
		System.out.println("listNodes = "+listNodes);
		System.out.println("natureSoluce = "+natureSoluce);
		System.out.println("valObj = "+valObj);
		System.out.println("calcTime = "+calcTime+" ms");
		System.out.println("method = "+method);
		System.out.println("freeField = "+freeField);
	}

	
	public Node getNode(String idNode) {
		for(Node n : listNodes) {
			if(n.getId().equals(idNode)) {
				return n;
			}
		}
		return new Node("ERROR");
	}
	
	public double getValObj() {
		return valObj;
	}
	
	public void printToTxt() {
		ArrayList<String> lignes = new ArrayList<String>();
		lignes.add(instanceName);
		lignes.add(""+nbEvacNodes);
		for(Node n:listNodes) {
			lignes.add(n.getId()+" "+n.getEvacRate()+" "+(int)(n.getEvacStart()));
		}
		lignes.add(""+natureSoluce);
		lignes.add(""+(int)valObj);
		lignes.add(""+calcTime+" ms");
		lignes.add(method);
		lignes.add(freeField);
		new GestionFichier().writeLines("/home/boulle/Bureau/Documents/Metaheuristique/Instances/soluce_"+instanceName+".txt", lignes);

	}

}