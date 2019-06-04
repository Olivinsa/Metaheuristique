package gs;

//classe pour representer un noeud
//usuellement, on n'en a pas bsoin et on ne se sert que des id
//on ne l'utilise que pour les noeuds de départs de chemins d'évacuation
public class Node {
	
	private String id;
	//taux d'évacuation du chemin partant de ce noeud
	private int evacRate;
	//temps de départ de l'évacuation
	private double evacStart;
	
	//construction du noeud à partir d'une ligne d'un fichier solution
	public Node(String ligne) {
		String[] liste = ligne.split(" ");
		this.id = liste[0];
		this.evacRate = Integer.parseInt(liste[1]);
		this.evacStart = Double.parseDouble(liste[2]);
	}
	
	//construction du noeud avec les parametres
	public Node(String id, int evacRate, double evacStart) {
		this.id = id;
		this.evacRate = evacRate;
		this.evacStart = evacStart;
	}
	
	public String toString () {
		return "("+id+"; "+evacRate+"; "+evacStart+")" ;
	}

	public int getEvacRate() {
		return evacRate;
	}
	
	public String getId() {
		return id;
	}
	
	public double getEvacStart() {
		return evacStart;
	}
}
