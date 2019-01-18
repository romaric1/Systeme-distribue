package donnees;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Facture {

	//Cr�ation de la facture en fonction des prix et des disponibilit�s
	public void EnregistrementFacture(int[] tabprix, int[] tabcommande) {
		//int sommePlat = 0;
		File f = new File("monFichier.txt");
		String client = "M. BOUIX Emannuel";

		//Bouchons
		/*int tabCommande[][]  = {{ 64, 256, 20, 26, 30 },
	            { 8, 16, 20, 26, 30 },
	            { 8, 16, 20, 26, 30 }};*/

		String tabNomPlat[][]  = {{"1","Tajine de choux   "},
	            {"2","Tajine d'aubergine" },
	            {"3","Tajine de poulet  " }};

		/*int tabPrix[][]  = {tabprix,
				{ 8, 16, 20, 26, 30 },
	            { 8, 16, 20, 26, 30 }} ;*/
		//FIN Bouchons*/

		int[] tabToto = tabprix;
		int[] tabCommande = tabcommande;
		int[] tabfinale = new int[5];


		try {
			//Tentative d ecriture dans le fichier txt
			FileWriter fw = new FileWriter(f);
	    fw.write("Facture de : "+client +"\r\n"+"------------------------------------------------------------------"+"\r\n");
	    fw.write("Ref | Nom du Plat Cuisin� | Pt2  | Pt4  | Pt6  | Pt8  | Pt12  |  Somme "+"\r\n");
	    fw.write("------------------------------------------------------------------"+"\r\n");
	    fw.write(tabNomPlat[1][1]);
	    for (int i = 0; i < tabfinale.length; i++) {
			tabfinale[i] = tabToto[i] * tabCommande[i];
			fw.write("  |"+String.valueOf(tabfinale[i]) + "|  ");
		}

	    /* Le code comment� suivant permet d'�crire dans la facture avec tous les prix et commandes
	     * de toutes les lignes

	    for (int i = 0; i < tabCommande.length; i++) {
	    	//Recuperation de l'ID Plat
	    	double IDtemp = tabCommande[i][0];
	    	//sqrt sur l'ID car il avait �t� multipli� par lui m�me auparavant
	    	IDtemp = Math.sqrt(IDtemp) -1;
	    	//Transtypage
	    	int IDPlat = (int) IDtemp;
	    	String IDPlatCourant = String.valueOf(IDPlat);
	    	fw.write(" "+IDPlatCourant+"  | ");
	    	//Recuperation du NomPlat en fonction de l'ID Plat
	    	//String NomPlat = tabNomPlat [IDPlat][1];
	    	//fw.write(NomPlat);

	    	for (int j = 1; j < tabCommande[i].length; j++) {
	    		String valCourante = String.valueOf(tabCommande[i][j]);
	    		sommePlat = sommePlat+ (tabCommande [i][j] * tabPrix[i][j]);
	    		fw.write("  |  "+valCourante);
				}
	    	String sommeCourante = String.valueOf(sommePlat);
	    	fw.write("  |  "+sommeCourante+"\r\n");
			}*/

	    fw.flush();
	    fw.close();
		}
		catch (IOException e){
			e.printStackTrace();
	}
	}

}
