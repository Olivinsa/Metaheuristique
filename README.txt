Manuel d'utilisation des programmes

Tous les programmes peuvent être utilisés à partir du Main,
il faut écrire les commandes dans la fonction 
	static void main

la variable address doit être égale au chemin du dossier contenant les instances
	(ex : address = "/home/boulle/Bureau/Documents/Metaheuristique/Instances/";)

pour lire un graphe à partir d'un fichier, utiliser
	Graph g = new ReadDataGraph(nomGraphe).getGraph();

	(ex : Graph g = new ReadDataGraph("dense_10_30_3_5_I").getGraph();)


pour lire une solution à partir d'un fichier, utiliser
	Soluce s = new ReadDataSoluce(nomSoluce).getSoluce();

une solution peut être liée à un graph avec
	g.addSoluce(s);

pour vérifier que la solution liée au graph est valide, utiliser 
	g.isValid();

pour executer l'heuristique calcBornSup2 sur le graphe, utiliser
	g.calcBornSup2(listPath, proba);
avec listPath la liste d'ordre d'ajout des chemins
proba une valeur entre 0 et 1
la solution sera liée au graphe automatiquement

on peut récuperer la valeur objectif d'une solution avec
	s.getValObj();
ou si le graphe contient une solution
	double valObj = g.getSoluce.getValObj();

on peut aussi calculer la valeur objectif avec la recherche locale 
	RechercheLocale rL = new RechercheLocale(nomGraphe, nbrChemins, proba);
	double valObj = rL.Recherche(valObj1, listPath);

et avec la diversification
	double valObj = new Diversification(nomGraphe, nbrChemins, nbrStartPoints ,proba).getValObj();

