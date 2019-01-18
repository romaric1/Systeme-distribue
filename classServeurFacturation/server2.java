

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import access.PrixVenduDataAccess;


public class server2 {
	ServerSocket socketserver;
	Socket socketduclient;
	BufferedReader in;
	PrintWriter out;
	StringBuilder sb = new StringBuilder();

	//Mise a l'ecoute
	public void ecoute(int numPort) throws IOException {

		socketserver = new ServerSocket(numPort);
		System.out.println("Le serveur est � l'�coute du port " + socketserver.getLocalPort() + "\n");

	}

	//Lecture de donnees ligne par ligne via BufferedReader et readLine
	public String lireDonees() throws IOException {
		in = new BufferedReader(new InputStreamReader(socketduclient.getInputStream()));
		String str;

		str = in.readLine();
		return str;

	}

	//Envoi de message avec PrintWriter
	public void envoiMessage(String unMessage) throws IOException {
		out = new PrintWriter(socketduclient.getOutputStream(), true);
		out.write(unMessage);
		out.flush();
	}

	//Fermeture du BufferReader, du PrintWriter et de la socket client
	public void deconnexion() throws IOException {
		out.close();
		in.close();
		socketduclient.close();
	}

	// Traitement de la chaine
	public ArrayList<Object> TraiterLigne(String uneLigne) { // mettre en param�tre la ligne r�cup�rer du main

		ArrayList<Object> ListObject = new ArrayList<Object>();
		uneLigne = uneLigne.replaceAll("\n", "");

		String[] tokens = uneLigne.split(";");

		for (int i = 0; i < tokens.length; i++) {
			if (this.isInteger(tokens[i])) {

				ListObject.add(Integer.parseInt(tokens[i]));

			} else {
				ListObject.add(tokens[i]);
			}
		}

		return ListObject;
	}

	// v�rifie si la chaine de caract�res donn�e en param�tre est un entier
	public boolean isInteger(String chaine) {
		try {
			Integer.parseInt(chaine);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/*public String RecupererPrix(String uneRequete) {
		// Bouchons

		int tabPrix[][] = { { 1, 8, 16, 20, 26, 30 }, { 2, 8, 16, 20, 26, 30 }, { 3, 8, 16, 20, 26, 30 } };

		int tabQuantit�[][] = { { 1, 8, 16, 20, 26, 30 }, { 2, 8, 16, 20, 26, 30 }, { 3, 8, 16, 20, 26, 30 } };

		int tabCommande[][] = new int[40][40];
		int valP = 0;
		int valQ = 0;
		System.out.println("debut");
		for (int i = 0; i < tabQuantit�.length; i++) {
			for (int j = 0; j < tabQuantit�[i].length; j++) {
				valP = tabPrix[i][j];
				valQ = tabPrix[i][j];
				tabCommande[i][j] = valP * valQ;
				System.out.println(tabCommande[i][j]);
			}
		}
		System.out.println("fin");

		return "";
	}*/

	//Recuperation prix demandé en BDD
	public String RecupererPrixBdd() {
		String url = "jdbc:mysql://localhost/systemdistribues";
		String username = "root";
		String password = "";

		PrixVenduDataAccess prixVendu = new PrixVenduDataAccess("com.mysql.jdbc.Driver", url, username, password);
		//On a essayé avec reads() mais la methode nous renvoyais null
		return prixVendu.read(1).getNomPlat() + ";"+ String.valueOf(prixVendu.read(1).getPrixPour2()) + ";"+ String.valueOf(prixVendu.read(1).getPrixPour4()) + ";"+String.valueOf(prixVendu.read(1).getPrixPour6())+ ";"+String.valueOf(prixVendu.read(1).getPrixPour8()) + ";"+String.valueOf(prixVendu.read(1).getPrixPour12());
	}

	//Attente client
	public boolean attenteClient() throws IOException {
		while ((socketduclient = socketserver.accept()) != null) {
			return true;
		}
		return false;

	}

	//Fermeture socket serveur
	public void closeSocketServer() throws IOException {
		socketserver.close();
	}

	public static void main(String[] args) throws IOException {
		server2 facturation = new server2();
		// Le serveur est en �coute
		facturation.ecoute(2008);

		while (facturation.attenteClient()) { // boucle infine
			System.out.println("Un client s'est connect�");
			// Le serveur re�oit une ligne
			String uneLigne = facturation.lireDonees();
			// Cr�ation d'une liste d'object qui contient chaque �l�ment de la string
			ArrayList<Object> toto = new ArrayList<Object>();
			// Traitement de la ligne
			toto.add(facturation.TraiterLigne(uneLigne));
			System.out.println(toto.get(0) + "\n");
			// Le serveur envoit un message
			facturation.envoiMessage(facturation.RecupererPrixBdd());
			//facturation.envoiMessage("ok\n");
			facturation.deconnexion();

		}

		facturation.closeSocketServer();

	}
}
