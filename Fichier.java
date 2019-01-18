package donnees;


import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Fichier {
	
	//Méthode qui permet de parcourir tout le fichier csv et de récupérer une array liste qui contient 
	//chaque ligne de commande
	public ArrayList<String> RecupererCommande(String filePath) {
	BufferedReader buff = null;
	String ligne = null;
	ArrayList<String> result = new ArrayList<String>();
	try {
		buff = new BufferedReader(new FileReader(filePath));
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	}
	
	try {
		while ((ligne = buff.readLine()) != null) {
		
		result.add(ligne);
		}
		buff.close();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	return result;
	
	
}
}