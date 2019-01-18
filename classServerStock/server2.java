

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import access.StockEntrepotsDataAccess;

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

	//Traitement de la chaine
	public int[] TraiterLigne(String uneLigne) { // mettre en param�tre la ligne r�cup�rer du main
		uneLigne = uneLigne.replaceAll("\n", "");
		String[] tokens = uneLigne.split(";");
		int[] tabcommande = {Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]),Integer.parseInt(tokens[5]),Integer.parseInt(tokens[6])};
		return tabcommande;
	}


	// v�rifie si la chaine de caract�res donn�e en param�tre est un entier
	public boolean isInteger(String chaine) {
		try {
			Integer.parseInt(chaine);
			return true;
		}catch (NumberFormatException nfe) {
			return false;
		}
	}


	//Base de donn�es
	public void EnvoiRequete(Object tableauObject[]) {
		// Cr�er la requ�te SQl avec le tableau d'objet (prix quantit� command�es)
	}


	//Recuperation etat du stock demandé en BDD
	public int[] recupererStock() {
		String url = "jdbc:mysql://localhost/systemdistribues";
		String username = "root";
		String password = "";
		StockEntrepotsDataAccess StockEntrepotData = new StockEntrepotsDataAccess("com.mysql.jdbc.Driver", url, username, password);


		int[] stock = new int[]{StockEntrepotData.reads(1).get(0).getQuantiteEPour2(),StockEntrepotData.reads(1).get(0).getQuantiteEPour4(),StockEntrepotData.reads(1).get(0).getQuantiteEPour6(),StockEntrepotData.reads(1).get(0).getQuantiteEPour8(),StockEntrepotData.reads(1).get(0).getQuantiteEPour12()};

		return stock;
	}

	//Comparaison entre les stock dispo et les quantités demandé pour la commande
	public String disponibiliteCommande(int[] lestock, int[] tcommande) {
		int[] tdiff = new int[5];
		String message = "";
		for (int i = 0; i < tcommande.length; i++) {
			tdiff[i] = lestock[i] - tcommande[i];
			while (tdiff[i] == 0) {
				message = "ok\n";
			}
			if (tdiff[i] < 0 ) {
				message = String.valueOf(lestock[0])+";"+String.valueOf(lestock[1])+";"+String.valueOf(lestock[2])+";"+String.valueOf(lestock[3])+";"+String.valueOf(lestock[4]);
			}

		}

		return message;
	}

	//Attente client
	public boolean attenteClient() throws IOException {
		while( (socketduclient = socketserver.accept())!=null)
		{
			return true;
		}
		return false;

	}

	//Fermeture socket
	public void closeSocketServer() throws IOException {
		socketserver.close();
	}

	//Main
	public static void main(String[] args) throws IOException {

		//le serveur stock est instanci�
		server2 stock = new server2();
		//Le serveur est en �coute
		stock.ecoute(2004);

		while (stock.attenteClient()) { //boucle infine
			System.out.println("Un client s'est connect�");

			//Le serveur re�oit une ligne
			String uneLigne = stock.lireDonees();

			int[] tcommande = stock.TraiterLigne(uneLigne);
			int[] tstock = stock.recupererStock();

			//Le serveur envoit un message

			System.out.println(stock.disponibiliteCommande(tstock, tcommande)+"\n");
			stock.envoiMessage(stock.disponibiliteCommande(tstock, tcommande));

			stock.deconnexion();


		}
	}
}
