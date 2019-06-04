package gs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//classe pour interagir avec les fichiers
public class GestionFichier {

	//fonction pour lire un fichier txt et stocker le contenu dans lignes
	public ArrayList<String> readFile(String lienFile) {
		
		ArrayList<String> lignes = new ArrayList<String>();
		FileInputStream f = null;
		
		try {
			f = new FileInputStream(new File(lienFile));
			//conversion du fichier en String
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
	
			//ligne lues
			String strLine;
	
			//lecture ligne par ligne du fichier
			try {
				while ((strLine = br.readLine()) != null)   {
				  // Print the content on the console
				  lignes.add(strLine);
				}
			} catch (IOException e1) {
				System.out.println("Erreur lecture");
			}
	
			//quoi qu'il arrive, on ferme le fichier
			finally {
				br.close();
			} 
		} catch (Exception e) {
			System.out.println("Erreur lecture2");
		}
		return lignes;
	}
	
	//supprime tout le contenu d'un fichier
	public void cleanFile(String lienFile) {
		
		BufferedWriter br = null;
		try { 
			br = new BufferedWriter(new FileWriter(lienFile)); 
			br.write(""); 
		} 
		catch(Exception e){
			System.out.println("Erreur vidage "+lienFile);
		}
		finally {
			try {
				br.close(); 
			}
			catch(Exception e){
				System.out.println("Erreur fermeture de "+lienFile+" dans le vidage");
			}
		} 
	}
	
	//ecrit la ligne à la fin du fichier (append)
	public void writeLine(String lienFile, String message) {
		BufferedWriter br = null;
		try { 
			//le true permet de faire un append au lieu de remplacer le texte
			br = new BufferedWriter(new FileWriter(lienFile, true)); 
			//ajout du message
			br.write(message+"\n"); 
		} 
		catch(Exception e){
			System.out.println("Erreur ecriture de ligne "+lienFile);
		}
		finally {
			try {
				br.close(); 
			}
			catch(Exception e){
				System.out.println("Erreur fermeture de "+lienFile+" dans ecrireLigne");
			}
		} 
	}
	
	//ecrit des lignes dans un fichier en remplaçant le contenu
	public void writeLines(String lienFile, ArrayList<String> lignes) {
		//on commence par vider le fichier
		cleanFile(lienFile);
		for(String l : lignes) {
			writeLine(lienFile, l);
		}
	}
}
