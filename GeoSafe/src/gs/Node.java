package gs;

public class Node {
	
	private String id;
	private int evacRate;
	private double evacStart;
	
	public Node(String ligne) {
		String[] liste = ligne.split(" ");
		this.id = liste[0];
		this.evacRate = Integer.parseInt(liste[1]);
		this.evacStart = Double.parseDouble(liste[2]);
	}
	
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
