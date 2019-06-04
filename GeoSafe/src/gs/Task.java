package gs;

public class Task {
	
	private int id;
	private int population;
	private double decalage;
	private int taux;
	
	public Task(int i, int pop, double dec, int t) {
		id = i;
		population = pop;
		decalage = dec;
		taux = t;
	}

	public double getDuree() {
		//return Math.round((double)population/(double)taux);
		return (double)population/(double)taux;
	}

	public int getPopulation() {
		return population;
	}

	public double getDecalage() {
		return decalage;
	}

	public int getId() {
		return id;
	}
	
	public int getTaux() {
		return taux;
	}
}
