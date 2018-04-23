package Serveur;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import GUI.GestionUtilisateurGUI;

public class ProgrammePrincipal {
	
	public String fileName = "serveur_config.txt"; //nom du fichier où chercher les informations de config sour la forme:
	/* LISTENING_PORT
	 * URL_BDD
	 * LOGIN_BDD
	 * PASSWORD_BDD
	 */
	//BDD
	public GestionBDD gestionBDD;
	private String urlBDD = "jdbc:mysql://localhost:3306/ProjetS5BDD";
	private String userBDD = "root";
	private String passwordBDD = "";
	//Reseau
	private int LISTENING_PORT = 2002;
	public static String delimiteur = "¤";
	
	public void readConfig(){
		try (BufferedReader input = new BufferedReader(new FileReader(fileName))){
			String line;
			if((line=input.readLine()) != null)
				LISTENING_PORT = Integer.parseInt(line);
			if((line=input.readLine()) != null)
				urlBDD = line;
			if((line=input.readLine()) != null)
				userBDD = line;
			if((line=input.readLine()) != null)
				passwordBDD = line;
		} catch (IOException e) {
			System.out.println("erreur lors de la lecture du fichier");
			e.printStackTrace();
		}
	}
		
	public void initBDD(){
        /*try {
        	Class.forName("com.mysql.jdbc.Driver");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }*/
        //urlBDD = "jdbc:mysql://localhost:3306/ProjetS5BDD";
		urlBDD += "?autoReconnect=true&useSSL=false";
        /*userBDD = "root";
        passwordBDD = "";*/
        this.gestionBDD = new GestionBDD(urlBDD,userBDD,passwordBDD);
	}
	
	public void lancerAccepterConnexion(){
		Thread authentificationThread = new Thread(new AccepterConnexion(this.gestionBDD,LISTENING_PORT));
		authentificationThread.setDaemon(true);
		authentificationThread.run();
	}
	
	public void lancer(){
		this.readConfig();
		this.initBDD();
		new GestionUtilisateurGUI(this.gestionBDD);
		this.lancerAccepterConnexion();
	}
	
	//Main
	public static void main(String[] args){
		ProgrammePrincipal prog = new ProgrammePrincipal();
		prog.lancer();
	}
}
