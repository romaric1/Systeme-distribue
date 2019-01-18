package donnees;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class client2 {
	public int numPort;
	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;

	//Creation socket pour connexion
	public void connexion(int numPort) throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getLocalHost(), numPort); // Cr�ation du socket client

	}


	//Envoi message
	public void envoiMessage(String unMessage) throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);
		out.write(unMessage);
		out.write("\n");
		out.flush();

	}

	//Lecture ligne par ligne
	public String lireMessage() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String messageDuserveur;
		messageDuserveur = in.readLine();
		return messageDuserveur;


	}

	//Traitement du prix recu de la BDD
	public int[] traiterPrix(String uneLigne) throws IOException {
		uneLigne = uneLigne.replaceAll("\n", "");
		String[] tokens = uneLigne.split(";");
		int[] tabPrix = {Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]),Integer.parseInt(tokens[5])};
		return tabPrix;

	}

	//Traitement ligne par ligne de la commande
	public int[] traiterCommande(String uneLigne) { // mettre en param�tre la ligne r�cup�rer du main
		uneLigne = uneLigne.replaceAll("\n", "");
		String[] tokens = uneLigne.split(";");
		int[] tabcommande = {Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[4]),Integer.parseInt(tokens[5]),Integer.parseInt(tokens[6])};
		return tabcommande;
	}

	//Fermeture des Buffer,Print et de la Socket pour deconnexion
	public void deconnexionDuClient() throws IOException {
		in.close();
		out.close();
		socket.close();
	}

	//Main
	public static void main(String[] args) throws UnknownHostException, IOException {

		client2 commercial = new client2();
		Fichier monfichier = new Fichier();

		//On instancie un scanner pour r�cup�rer le chemin du CSV
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le chemin du fichier CSV :");
		String Filepath = sc.nextLine();

		//On r�cup�re toutes lignes du fichier CSV
		ArrayList<String> maCommande = monfichier.RecupererCommande(Filepath);

		Facture maFacture = new Facture();

		//Envoit ligne par ligne dans une socket au serveur stock
		System.out.println("------------------------------------------------------------------"+"\nDemande de stock");
		for (int i = 0; i < maCommande.size(); i++) {
			commercial.connexion(2004);
			commercial.envoiMessage(maCommande.get(i));
			System.out.println("La quantite en stock est : "+commercial.lireMessage());
			commercial.deconnexionDuClient();
		}

		System.out.println("------------------------------------------------------------------"+
				"\n------------------------------------------------------------------"+"\nDemande des Prix");
		//Envoit ligne par ligne dans une socket au serveur facturation
		for (int i = 0; i < maCommande.size(); i++) {
			commercial.connexion(2008);

			commercial.envoiMessage(maCommande.get(i));

			String lesPrix =commercial.lireMessage();

			System.out.println("Les prix par portions pour le plat command�  : "+lesPrix);

			//Enregistrement dans un fichier text de la facture du client
			maFacture.EnregistrementFacture(commercial.traiterPrix(lesPrix),commercial.traiterCommande(maCommande.get(0)));
			commercial.deconnexionDuClient();

		}



	}

}
