package Client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import GUI.*;

public class ProgrammePrincipal {

	private Utilisateur utilisateurPrincipal;
	private ArborescenceTicketGUI arborescenceTicketGUI;
	private AuthentificationGUI authentificationGUI;
	private ConversationGUI conversationGUI;
	//Reseau
	Socket socket;
	Reception reception;
	Emission emission;
    public String SERVER_HOSTNAME = "localhost";
    public int SERVER_PORT = 2002;
	public String fileName = "client_config.txt"; //nom du fichier où chercher les informations de config sour la forme:
	/* SERVER_HOSTNAME
	 * SERVER_PORT
	 */
	public static String delimiteur = "¤"; //delimiteur des champs des trames réseaux
	//pour la reconnexion
	public String userPassword;
	public boolean alreadyConnected = false;
	public boolean horsLigne = false;
	public List<String> trameToSend = new ArrayList<>();
	
	//----------------------------------------
	//Getter and setter
	//----------------------------------------
	public Utilisateur getUtilisateurPrincipal(){
		return this.utilisateurPrincipal;
	}
	public void setUtilisateurPrincipal(Utilisateur utilisateurPrincipal){
		this.utilisateurPrincipal = utilisateurPrincipal;
	}
	public Ticket getConversationGuiTicket(){
		if(this.conversationGUI != null)
			return this.conversationGUI.getTicket();
		return null;
	}
	public boolean isTicketLoaded(){
		return this.conversationGUI != null;
	}
	
	//----------------------------------------
	//Methodes pour le reseau
	//----------------------------------------
	//-----Communication 0----- (Authentification)
	public void reAuthentification(){
		String login = this.getUtilisateurPrincipal().getID();
		String password = this.userPassword;
		String trame = "0"+delimiteur+login+delimiteur+password;
	    emission.envoyerMessage(trame);	
	}
	public boolean authentification(String login, String password){
		if(this.connexionServeur()){
			this.userPassword = password;
			String trame = "0"+delimiteur+login+delimiteur+password;
	        emission.envoyerMessage(trame);	
	        return true;
		}
		return false;
	}
	public void remplirDataAuthentification(ArrayList<String> utilisateurPrincipal, ArrayList<ArrayList<String>> groupes){
		Utilisateur tempUser;
		int tempIdT, tempIdM;
		List<String> messagesMalConstruitsS = new ArrayList<>();
		List<Message> messagesMalConstruitsM = new ArrayList<>();
		List<String> ticketsMalConstruitsS = new ArrayList<>();
		List<Ticket> ticketsMalConstruitsT = new ArrayList<>();
		//Initialisation utilisateur principal
		String tempID = utilisateurPrincipal.get(0);
		String tempNom = utilisateurPrincipal.get(1);
		String tempPrenom = utilisateurPrincipal.get(2);
		Boolean tempTypeNormal = ((utilisateurPrincipal.get(3)).equals("1")) ? false : true;
		Utilisateur tempUserP;
		if(!this.alreadyConnected){
			tempUserP = new Utilisateur(tempID,tempPrenom,tempNom,tempTypeNormal,true);
			this.setUtilisateurPrincipal(tempUserP);	
		}else
			tempUserP = this.getUtilisateurPrincipal();
		
		//Initialisation des groupes
		Groupe tempGroupe;
		int tempInt, cpt;
		for(List<String> listGroup : groupes){
			cpt = 0;
			tempNom = listGroup.get(cpt++);
			tempTypeNormal = ((listGroup.get(cpt++)).equals("1")) ? false : true;
			tempGroupe = null;
			if(this.alreadyConnected)
				tempGroupe = this.findGroupe(tempNom);
			if(tempGroupe == null)
				tempGroupe = new Groupe(tempNom,tempTypeNormal);
			tempGroupe.lierUtilisateur(tempUserP);
			
			//Initialisation des utilisateurs du groupe
			tempInt = Integer.parseInt(listGroup.get(cpt++));
			for(int i = 0; i < tempInt; ++i){
				tempID = listGroup.get(cpt++);
				tempUser = this.findUser(tempID);
				if(tempUser == null){
					tempNom = listGroup.get(cpt++);
					tempPrenom = listGroup.get(cpt++);
					tempTypeNormal = ((listGroup.get(cpt++)).equals("1")) ? false : true;
					tempUser = new Utilisateur(tempID,tempPrenom,tempNom,tempTypeNormal,false);	
				}else
					cpt += 3;
				tempGroupe.lierUtilisateur(tempUser);
			}
			
			//Initialisation des tickets du groupe
			Ticket tempTicket;
			tempInt = Integer.parseInt(listGroup.get(cpt++));
			int tempNbrNonLu, tempCouleur;
			int[] tempDateI = new int[6];;
			Calendar tempDate;
			Message tempMessage;
			String tempContenu;
			for(int i = 0; i < tempInt; ++i){
				tempIdT = Integer.parseInt(listGroup.get(cpt++));
				tempNom = listGroup.get(cpt++);
				tempID = listGroup.get(cpt++);
				for(int j = 0; j < 5; ++j)
					tempDateI[j] = Integer.parseInt(listGroup.get(cpt++));
				tempDateI[5] = (int)Double.parseDouble(listGroup.get(cpt++));
				tempDateI[1]--; //Le mois d'un Calendar va de 0 a 11 et non 1 a 12
				tempDate = ProgrammePrincipal.intToCalendar(tempDateI[0],tempDateI[1],tempDateI[2],tempDateI[3],tempDateI[4],tempDateI[5]);
				tempNbrNonLu = Integer.parseInt(listGroup.get(cpt++));
				tempUser = this.findUser(tempID);
				tempTicket = null;
				if(this.alreadyConnected)
					tempTicket = tempGroupe.findTicketWithID(tempIdT);
				if(tempTicket == null)
					tempTicket = new Ticket(tempIdT,tempNom,tempDate,tempNbrNonLu,tempGroupe,tempUser);
				if(tempUser == null){
					ticketsMalConstruitsS.add(tempID);
					ticketsMalConstruitsT.add(tempTicket);
				}
				
				//Initialisation du dernier message du ticket
				tempIdM = Integer.parseInt(listGroup.get(cpt++));
				tempContenu = listGroup.get(cpt++);
				tempID = listGroup.get(cpt++);
				tempCouleur = Integer.parseInt(listGroup.get(cpt++));
				for(int j = 0; j < 5; ++j)
					tempDateI[j] = Integer.parseInt(listGroup.get(cpt++));
				tempDateI[5] = (int)Double.parseDouble(listGroup.get(cpt++));
				tempDateI[1]--; //Le mois d'un Calendar va de 0 a 11 et non 1 a 12
				tempDate = ProgrammePrincipal.intToCalendar(tempDateI[0],tempDateI[1],tempDateI[2],tempDateI[3],tempDateI[4],tempDateI[5]);
				tempUser = this.findUser(tempID);
				tempMessage = new Message(tempIdM,tempContenu,tempDate,tempUser,tempCouleur);
				if(tempUser == null){
					messagesMalConstruitsS.add(tempID);
					messagesMalConstruitsM.add(tempMessage);
				}
				tempTicket.ajouterMessage(tempMessage);
				
				tempGroupe.lierTicket(tempTicket);
			}
		}
		
		//Reparation des tickets et messages mal construits
		//(du au fait que le createur ou l'expediteur n'etait pas encore cree au moment de la creation du ticket ou du message)
		int ind = 0;
		for(String id : ticketsMalConstruitsS){
			Ticket ticket = ticketsMalConstruitsT.get(ind);
			ticket.setCreateur(this.findUser(id));
			ind++;
		}
		ind = 0;
		for(String id : messagesMalConstruitsS){
			Message message = messagesMalConstruitsM.get(ind);
			message.setExpediteur(this.findUser(id));
			ind++;
		}
		
		//Fermeture de AuthentificationGUI
		this.authentificationGUI.closeAuthentification();
		
		//Ouverture de ArborescenceTicketGUI
		if(!this.alreadyConnected)
			this.arborescenceTicketGUI = new ArborescenceTicketGUI(this);
		else{
			this.arborescenceTicketGUI.resetJTree();
			this.arborescenceTicketGUI.initJTree();	
			this.horsLigne = false;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String trame : trameToSend)
				emission.envoyerMessage(trame);
			trameToSend.clear();
		}
		
		this.alreadyConnected = true;
	}
	public void mauvaiseConnexion(){
		this.authentificationGUI.mauvaisIdentifiants();
	}
	
	//-----Communication 1----- (Creer ticket/Nouveau message)
	public void envoyerMessage(String contenuM, Ticket ticket, int mode){
		Calendar temp = Calendar.getInstance();
		Groupe groupe = ticket.getGroupe();
		groupe.getListeTickets().remove(ticket);
		Message newM = new Message(-1,contenuM,temp,this.getUtilisateurPrincipal(),0);
		ticket.ajouterMessage(newM);
		groupe.getListeTickets().add(ticket);
		this.arborescenceTicketGUI.actualiserJTree(ticket,mode);
		
		this.ouvrirConversation(ticket);
		
		String trame = "1"+delimiteur+String.valueOf(mode)+delimiteur+ticket.getGroupe().getNom()+delimiteur;
		if(mode==0)
			trame += ticket.getTitre();
		else
			trame += String.valueOf(ticket.getiD());
		trame += delimiteur+contenuM;
		emission.envoyerMessage(trame);
		if(this.horsLigne)
			this.trameToSend.add(trame);
	}
	public void remplirDataNouveauMessage(String nomGroupe, List<String> infos, List<String> ticket, List<String> message, int mode){
		//Recherche du groupe
		Groupe tempGroupe = this.findGroupe(nomGroupe);
		
		//Gestion du ticket
		if(tempGroupe != null){
			int cpt = 0;
			int[] tempDateI = new int[6];
			for(int j = 0; j < 5; ++j)
				tempDateI[j] = Integer.parseInt(infos.get(cpt++));
			tempDateI[5] = (int)Double.parseDouble(infos.get(cpt++));
			tempDateI[1]--; //Le mois d'un Calendar va de 0 a 11 et non 1 a 12 comme sur la BDD
			Calendar tempDate = ProgrammePrincipal.intToCalendar(tempDateI[0],tempDateI[1],tempDateI[2],tempDateI[3],tempDateI[4],tempDateI[5]);
			String tempIdU = infos.get(cpt++);
			cpt = 0;
			int tempIdT = Integer.parseInt(ticket.get(cpt++));
			String tempNom = "";
			Ticket tempTicket = null;
			if(mode==0){
				//Creation du nouveau ticket
				tempNom = ticket.get(cpt++);
				tempTicket = new Ticket(tempIdT,tempNom,tempDate,0,tempGroupe,this.findUser(tempIdU));
				tempTicket.setAlreadyLoad(true);
				tempGroupe.lierTicket(tempTicket);
			}else{
				//Recherche du ticket existant
				tempTicket = tempGroupe.findTicketWithID(tempIdT);
			}
			//Creation du nouveau message
			if(tempTicket != null){
				cpt = 0;
				int tempIdM = Integer.parseInt(message.get(cpt++));
				String tempContenu = message.get(cpt++);
				int tempCouleur = Integer.parseInt(message.get(cpt++));
				tempTicket.getGroupe().getListeTickets().remove(tempTicket);
				Message tempMessage = new Message(tempIdM,tempContenu,tempDate,this.findUser(tempIdU),tempCouleur);
				tempTicket.ajouterMessage(tempMessage);
				if(tempTicket.equals(this.getConversationGuiTicket())){
					this.ouvrirConversation(tempTicket);
				}else
					tempTicket.addNbrMessageNonLu();
				tempTicket.getGroupe().getListeTickets().add(tempTicket);
				this.arborescenceTicketGUI.actualiserJTree(tempTicket,1);
			}
		}
	}
	public void remplirDataMessageRecuServeur(String nomGroupe, int idTicket, int idMessage, String mode, List<String> date){
		Groupe groupe = this.findGroupe(nomGroupe);
		int[] tempDateI = new int[6];
		for(int j = 0; j < 5; ++j)
			tempDateI[j] = Integer.parseInt(date.get(j));
		tempDateI[5] = (int)Double.parseDouble(date.get(5));
		tempDateI[1]--; //Le mois d'un Calendar va de 0 a 11 et non 1 a 12 comme sur la BDD
		Calendar tempDate = ProgrammePrincipal.intToCalendar(tempDateI[0],tempDateI[1],tempDateI[2],tempDateI[3],tempDateI[4],tempDateI[5]);
		if(groupe != null){
			Ticket ticket = null;
			if(mode.equals("2")){
				NavigableSet<Ticket> listTicket = groupe.getListeTickets();
				for(Ticket t : listTicket){
					if(t.getiD() == -1){
						t.setiD(idTicket);
						t.setDate(tempDate);
						ticket = t;
						break;
					}
				}	
			}else //Mode = 3
				ticket = groupe.findTicketWithID(idTicket);
			
			if(ticket != null){
				NavigableSet<Message> listMess = ticket.getListeMessages();
				for(Message m : listMess){
					if(m.getID() == -1){
						m.setID(idMessage);
						m.setCouleur(1);
						m.setDate(tempDate);
						if(ticket.equals(this.getConversationGuiTicket()))
							this.ouvrirConversation(ticket);
						break;
					}
				}	
			}
		}
	}
	
	//-----Communication 2----- (Demander le chargement d'un ticket)
	public void chargerConversation(Ticket ticket, int offset){
		if(ticket.getNbrMessageNonLu() != 0){
			ticket.setNbrMessageNonLu(0);
		}
		if(offset == 0){
			if(ticket.getAlreadyLoad()){
				//Si le ticket est deja charge on ne redemande pas les informations mais on previent le serveur que l'on charge ce ticket
				offset = -1;
				this.ouvrirConversation(ticket);	
			}
			int idTicket = ticket.getiD();
			String trame = "2"+delimiteur+ticket.getGroupe().getNom();
			trame += delimiteur+String.valueOf(idTicket);
			trame += delimiteur+String.valueOf(offset);
			emission.envoyerMessage(trame);
		}
	}
	public void remplirDataConversation(String nomGroupe, String idTicket, List<List<String>> messages){
		Groupe tempGroupe = this.findGroupe(nomGroupe);
		if(tempGroupe != null){
			int iDT = Integer.parseInt(idTicket);
			Ticket tempTicket = tempGroupe.findTicketWithID(iDT);
			if(tempTicket != null){
				Message tempMessage;
				String tempContenu, tempIdU;
				int tempCouleur, cpt, tempIdM;
				int[] tempDateI = new int[6];
				Calendar tempDate;
				for(List<String> listeMessage : messages){
					cpt = 0;
					tempIdM = Integer.parseInt(listeMessage.get(cpt++));
					tempContenu = listeMessage.get(cpt++);
					tempIdU = listeMessage.get(cpt++);
					tempCouleur = Integer.parseInt(listeMessage.get(cpt++));
					for(int j = 0; j < 5; ++j)
						tempDateI[j] = Integer.parseInt(listeMessage.get(cpt++));
					tempDateI[5] = (int)Double.parseDouble(listeMessage.get(cpt++));
					tempDateI[1]--; //Le mois d'un Calendar va de 0 a 11 et non 1 a 12
					tempDate = ProgrammePrincipal.intToCalendar(tempDateI[0],tempDateI[1],tempDateI[2],tempDateI[3],tempDateI[4],tempDateI[5]);
					tempMessage = new Message(tempIdM,tempContenu,tempDate,this.findUser(tempIdU),tempCouleur);
					tempTicket.ajouterMessage(tempMessage);
				}	
				this.ouvrirConversation(tempTicket);
				tempTicket.setAlreadyLoad(true);
			}	
		}
	}
	public void ouvrirConversation(Ticket ticket){
		this.conversationGUI = this.arborescenceTicketGUI.ouvrirConversation(ticket);
	}
	
	//-----Communication 3----- (Demander le status d'un message)
	public void chargerStatusMessage(int messPos){
		Ticket ticket = this.getConversationGuiTicket();
		if(ticket != null){
			int idM = ticket.findMessageIdWithPosition(messPos);
			if(idM != -1){
				String trame = "3";
				trame += delimiteur+ticket.getGroupe().getNom();
				trame += delimiteur+ticket.getiD();
				trame += delimiteur+idM;
				emission.envoyerMessage(trame);	
			}
		}
	}
	public void remplirDataStatusMessage(String nomGroupe, String idTicket, String idMessage, List<List<String>> status){
		Groupe groupe = this.findGroupe(nomGroupe);
		if(groupe != null){
			Ticket ticket = groupe.findTicketWithID(Integer.parseInt(idTicket));
			if(ticket != null){
				Message message = ticket.findMessage(Integer.parseInt(idMessage));
				if(message != null){
					Map<EnumStatusMessage, NavigableSet<Utilisateur>> listeUtilisateurStatus = message.getListeUtilisateurStatus();
					NavigableSet<Utilisateur> listeUtilisateur;
					EnumStatusMessage[] enumS = EnumStatusMessage.values();
					Utilisateur tempU;
					int ind;
					for(List<String> tempStatus : status){
						tempU = this.findUser(tempStatus.get(0));
						if(tempU != null){
							ind = Integer.parseInt(tempStatus.get(1));
							listeUtilisateur = listeUtilisateurStatus.get(enumS[ind]);
							listeUtilisateur.add(tempU);
						}
					}
					int nbUser = status.size();
					int x = this.conversationGUI.xLastSelMessage;
					int y = this.conversationGUI.yLastSelMessage;
					new StatusMessageGUI(listeUtilisateurStatus,x,y,nbUser);
				}
			}
		}
	}
	
	//-----Communication 4----- (Utilisateur en train d'écrire)
	public void sendUserIsWriting(boolean writing){
		Ticket ticket = this.getConversationGuiTicket();
		if(ticket != null){
			String nomGroupe = ticket.getGroupe().getNom();
			String trame = "4"+delimiteur+nomGroupe+delimiteur+ticket.getiD()+delimiteur+writing;
			this.emission.envoyerMessage(trame);
		}
	}
	public void remplirDataUserWriting(String nomGroupe, String idTicket, String idUser, boolean writing){
		Groupe groupe = this.findGroupe(nomGroupe);
		if(groupe != null){
			Ticket ticket = groupe.findTicketWithID(Integer.parseInt(idTicket));
			if(ticket != null){
				Utilisateur user = this.findUser(idUser);
				if(user != null){
					if(writing)
						ticket.ajouterUserWriting(user);
					else
						ticket.enleverUserWriting(user);
					if(ticket.equals(this.getConversationGuiTicket()))
						this.arborescenceTicketGUI.setUserWriting(ticket.getUserWriting());
				}
			}
		}
	}
	
	//-----Communication 5----- (Changement de couleur d'un message)
	public void remplirDataCouleurMessage(String nomGroupe, String idTicket, String idMessage, String couleurM){
		Groupe groupe = this.findGroupe(nomGroupe);
		if(groupe != null){
			Ticket ticket = groupe.findTicketWithID(Integer.parseInt(idTicket));
			if(ticket != null){
				Message message = ticket.findMessage(Integer.parseInt(idMessage));
				if(message != null){
					message.setCouleur(Integer.parseInt(couleurM));
					if(ticket.equals(this.getConversationGuiTicket())){
						this.ouvrirConversation(ticket);
					}
				}
			}
		}
	}
	
	//-----Communication 6----- (Demander les utilisateurs en ligne)
    public void chargerInfosTicket(){
        Ticket ticket = this.getConversationGuiTicket();
        if(ticket!=null){
            Groupe groupe = ticket.getGroupe();
            String nomGroupe=groupe.getNom();
            String idTicket=String.valueOf(ticket.getiD());
            String trame = "6";
            trame += delimiteur+nomGroupe;
            trame += delimiteur+idTicket;
            emission.envoyerMessage(trame);	

        }
                   
    }
    public void remplirDataInfos(String nomGroupe, int idTicket, List<String> utilisateurs){
        //Recherche du groupe
        Groupe tempGroupe = this.findGroupe(nomGroupe);
        Set<Utilisateur> tamponUtilisateurs= new HashSet<>();
        tamponUtilisateurs.addAll(tempGroupe.getListeUtilisateurs());
        Map<EnumStatusUtilisateur, NavigableSet<Utilisateur>> listeStatus;
        //Recherche du ticket
        Ticket tempTicket;
        tempTicket = tempGroupe.findTicketWithID(idTicket);
        tamponUtilisateurs.add(tempTicket.getCreateur());
        //Creation du hashMap
        Comparator<EnumStatusUtilisateur> myComp2 = (EnumStatusUtilisateur o1, EnumStatusUtilisateur o2) -> (o1.compareTo(o2));
        listeStatus=new TreeMap<>(myComp2);
        //creation des listes utilisateurs
        Comparator<Utilisateur> myComp = (Utilisateur o1, Utilisateur o2) -> (o1.getNom().compareTo(o2.getNom()));
        NavigableSet<Utilisateur> tamponUtilisateurs1 = new TreeSet<>(myComp);
        System.out.println("users en ligne : "+utilisateurs);
         System.out.println("users : "+tamponUtilisateurs);
        for(String utilisateur : utilisateurs){
            Utilisateur tamponUtilisateur1=this.findUser(utilisateur);
            tamponUtilisateurs1.add(tamponUtilisateur1);
            tamponUtilisateurs.remove(tamponUtilisateur1);
        }
        listeStatus.put(EnumStatusUtilisateur.ENLIGNE, tamponUtilisateurs1);
        NavigableSet<Utilisateur> tamponUtilisateurs2 = new TreeSet<>(myComp);
        tamponUtilisateurs2.addAll(tamponUtilisateurs);
        listeStatus.put(EnumStatusUtilisateur.HORSLIGNE, tamponUtilisateurs2);
        tempTicket.setListeStatus(listeStatus);
        //Affichage du panneau Infos 
        new TicketInfosGUI(this, tempTicket,300,300);
    }
	
	//----------------------------------------
	//Autres methodes
	//----------------------------------------
	public static Calendar intToCalendar(int year, int month, int day, int hour, int minute, int second){
		//Calendar temp = Calendar.getInstance();
		Calendar temp = new GregorianCalendar();
		temp.clear();
		temp.set(Calendar.YEAR, year);
		temp.set(Calendar.MONTH, month);
		temp.set(Calendar.DAY_OF_MONTH, day);
		temp.set(Calendar.HOUR_OF_DAY, hour);
		temp.set(Calendar.MINUTE, minute);
		temp.set(Calendar.SECOND, second);
		return temp;
	}
	public static String CalendarToString(Calendar calendar){
		String temp = "";
		temp += calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
		temp += " "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
		return temp;
	}
	public Utilisateur findUser(String iD){
		NavigableSet<Groupe> listeGroupes = this.getUtilisateurPrincipal().getListeGroupes();
		Set<Utilisateur> listeUtilisateurs;
		for(Groupe groupe : listeGroupes){
			listeUtilisateurs = groupe.getListeUtilisateurs();
			if(listeUtilisateurs.contains(new KeyUtilisateur(iD))) // Complexite de O(1) car HashSet
				for(Utilisateur user : listeUtilisateurs)
					if(user.getID().equals(iD))
						return user;
		}
		return null;
	}
	public Groupe findGroupe(String nomGroupe){
		NavigableSet<Groupe> listGroupe = this.getUtilisateurPrincipal().getListeGroupes();
		for(Groupe groupe : listGroupe)
			if(groupe.getNom().equals(nomGroupe))
				return groupe;
		return null;
	}
	public void retourEcranAuthentification(){
		this.utilisateurPrincipal = null;
		this.arborescenceTicketGUI = null;
		this.conversationGUI = null;
		this.reception.returnToMainScreen = true;
		this.alreadyConnected = false;
		this.userPassword = "";
		this.emission.terminer();
		this.reception.terminer();
		/*try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		this.lancer();
	}
	
	//----------------------------------------
	//Inits
	//----------------------------------------
	public boolean reconnexionServeur(){
		this.arborescenceTicketGUI.setReconnexionServeur(false);
        try {
            // Connect to Chat Server
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Connecté au serveur " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            this.arborescenceTicketGUI.setReconnexionServeur(true);
            return true;
        } catch (IOException ioe) {
            System.err.println("Erreur connexion au serveur :  " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            return false;
        }
	}
	
	public boolean connexionServeur(){
        try {
            // Connect to Chat Server
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            System.out.println("Connecté au serveur " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            
        	PrintWriter out = new PrintWriter(new PrintStream(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Create and start Emission thread
            emission = new Emission(out);
            emission.setDaemon(true);
            emission.start();
            //Create and start Reception thread
            reception=new Reception(input, this);
            reception.setDaemon(true);
            reception.start();
            return true;
        } catch (IOException ioe) {
            System.err.println("Erreur connexion au serveur :  " + SERVER_HOSTNAME + ":" + SERVER_PORT);
            this.authentificationGUI.mauvaiseConnexionServeur();
            return false;
            //ioe.printStackTrace();
        }
	}
	public void readConfig(){
		try (BufferedReader input = new BufferedReader(new FileReader(fileName))){
			String line;
			if((line=input.readLine()) != null)
				SERVER_HOSTNAME = line;
			if((line=input.readLine()) != null)
				SERVER_PORT = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println("erreur lors de la lecture du fichier");
			e.printStackTrace();
		}
	}
	public void lancer(){
		this.readConfig();
		this.authentificationGUI = new AuthentificationGUI(this);
	}
	
	//Main
	public static void main(String[] args){
		ProgrammePrincipal prog = new ProgrammePrincipal();
		prog.lancer();
	}
}
