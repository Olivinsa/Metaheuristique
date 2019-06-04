package gs;

//classe représentant une tache
//l'accent de tache est omis volontairement
public class Task {
	
	private int id;
	//nombre de personne à évacuer avec cette tache
	private int population;
	private double decalage;
	private int taux;
	
	public Task(int i, int pop, double dec, int t) {
		id = i;
		population = pop;
		decalage = dec;
		taux = t;
	}

	//la durée d'une tache est définie par sa population divisée par son taux
	//on a décidé que la durée serait un double pour éviter des arrondis qui fausseraient le reste
	//le site de tests en ligne a des durées en int, du coup nos solutions ne sont pas compatibles avec 
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
