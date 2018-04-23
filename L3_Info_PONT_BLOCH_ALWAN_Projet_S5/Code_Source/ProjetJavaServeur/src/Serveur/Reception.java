package Serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class Reception extends Thread {

    private static String delimiteur = ProgrammePrincipal.delimiteur;

    private GestionEmission gestionEmission;
    private GestionBDD gestionBDD;
    private InfosClient infosClient;
    private Socket socket;

    private BufferedReader in;

    public Reception(InfosClient infosClient, GestionEmission gestionEmission, GestionBDD gestionBDD) throws IOException {
        this.infosClient = infosClient;
        this.gestionEmission = gestionEmission;
        this.gestionBDD=gestionBDD;
        this.socket = infosClient.socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void authentification() {
        try {
            String message;
            List<List<String>> temp,temp2,temp3;
            String tempWHERE;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(!infosClient.authentifier){
                message = in.readLine();
                System.out.println("R_"+message);
                if(isValid(message)){
                    System.out.println("Un client vient de se connecter\n");
                    StringBuilder reponse = new StringBuilder("0");
                    reponse.append(delimiteur);
                    reponse.append("1");
                    reponse.append(delimiteur);
                    //Recupere l'utilisateur principal
                    temp = gestionBDD.requeteSelect("IdU, NomU, PrenomU, TypeU","utilisateur","IdU = \""+infosClient.login+"\"");
                    List<String> tempListe = temp.get(0);
                    for(String tempInfos : tempListe){
                        reponse.append(tempInfos);
                        reponse.append(delimiteur);
                    }
                    //System.out.println(temp);
                    infosClient.nom=tempListe.get(1);
                    infosClient.prenom=tempListe.get(2);
                    infosClient.type=Integer.parseInt(tempListe.get(3));
                    //System.out.println(infosClient.login);
                    temp = gestionBDD.requeteSelect("TG.NomG, TG.TypeG", "groupe TG, appartient TA","TA.IdU = \""+infosClient.login+"\" and TA.NomG = TG.NomG");
                    temp2=gestionBDD.requeteSelect( "NomG, TypeG", "groupe", "TypeG != "+String.valueOf(infosClient.type));
                    temp2.removeAll(temp);
                    temp.addAll(temp2);
                    for(List<String> listeGrp : temp) {
                        //Utilisateurs
                        for(String tempInfos : listeGrp){
                            reponse.append(tempInfos);
                            reponse.append(delimiteur);
                        }
                        String nomGroupe=listeGrp.get(0);
                        temp2=gestionBDD.requeteSelect("TU.IdU, TU.NomU, TU.PrenomU, TU.TypeU", "utilisateur TU, appartient TA", "TA.NomG = \""+nomGroupe+"\" and TA.IdU = TU.IdU");
                        int nb=temp2.size();
                        reponse.append(String.valueOf(nb));
                        reponse.append(delimiteur);
                        for(List<String> liste : temp2){
                            for(String tempInfos : liste){
                                reponse.append(tempInfos);
                                reponse.append(delimiteur);
                            }
                        }
                        //Tickets
                        tempWHERE="NomG = \""+nomGroupe+"\"";
                        if( ! (listeGrp.get(1).equals(String.valueOf(infosClient.type))))
                            tempWHERE+=" and IdU = \""+String.valueOf(infosClient.login)+"\"";
                        temp2=gestionBDD.requeteSelect("IdT, TitreT, IdU, DateT", "ticket", tempWHERE);
                        nb=temp2.size();
                        reponse.append(String.valueOf(nb));
                        reponse.append(delimiteur);
                        for(List<String> liste : temp2){
                            String date=liste.get(liste.size()-1);
                            liste.remove(liste.size()-1);
                            liste.addAll(decouperDate(date));
                            for(String tempInfos : liste){
                                reponse.append(tempInfos);
                                reponse.append(delimiteur);
                            }
                            temp3 = gestionBDD.requeteSelect("COUNT(TS.StatusM)","statusm TS, message TM","TS.IdU = \""+infosClient.login+"\" and TM.IdT = "+liste.get(0)+" and TM.IdM = TS.IdM and StatusM != 2");
                            reponse.append(temp3.get(0).get(0)); //Nb messages non lus
                            reponse.append(delimiteur);
                            gestionBDD.requeteUpdate("statusm TS, message TM","TS.StatusM = 1","TS.IdU = \""+infosClient.login+"\" and TM.IdT = "+liste.get(0)+" and TS.IdM = TM.IdM and TS.StatusM < 1");
                            //On verifie si la couleur a change
                        	this.prevenirChangementCouleurTicket(nomGroupe,liste.get(0));
                            temp3=gestionBDD.requeteSelect("IdM, ContenuM, IdU, CouleurM, DateM", "message", "IdT = "+liste.get(0)+" ORDER BY DateM DESC LIMIT 1");
                            for(List<String> liste2 : temp3) {
                                date=liste2.get(liste2.size()-1);
                                liste2.remove(liste2.size()-1);
                                liste2.addAll(decouperDate(date));
                                for(String tempInfos : liste2){
                                    reponse.append(tempInfos);
                                    reponse.append(delimiteur);
                                }

                            }
                        }
                    }
                    String senderIP = socket.getInetAddress().getHostAddress();
                    infosClient.setAuthentifier(true);
                    gestionEmission.ajouterClient(infosClient);
                    infosClient.clientEmission.envoyerMessage(reponse.toString());
                }else{
                    String senderIP = socket.getInetAddress().getHostAddress();
                    String accesRefuse="0"+delimiteur+"0";
                    infosClient.clientEmission.envoyerMessage(accesRefuse);
                    System.out.println("Un client vient de rater son authentification\n");
                    //TODO BONUS : ADRESSE IP + JAVA MAIL ////////////////////////////////////
                }
            }
        } catch (IOException e) {
            System.err.println("Le client ne repond pas\n");
        }
    }

    private boolean isValid(String message) {
        boolean connexion = false;
        List<List<String>> temp;
        if (message != null) {
            String[] infosMessage = message.split(delimiteur);
            if (infosMessage[0].equals("0") && infosMessage.length == 3) {
                temp = gestionBDD.requeteSelect("COUNT(*)","utilisateur","IdU = \""+infosMessage[1]+"\" and PasswordU = \""+infosMessage[2]+"\"");
                //System.out.println(temp.get(0).get(0));
                infosClient.login=infosMessage[1];
                connexion = (temp.get(0).get(0).equals("1"));
            }
        }
        return connexion;
    }

    private ArrayList<String> decouperDate(String date){
        String[] tmp=date.split(" ");
        String[] tmpDate=tmp[0].split("-");
        String[] tmpHeure=tmp[1].split(":");
        ArrayList<String> resultat=new ArrayList<>();
        for(String tmp2 : tmpDate){
            resultat.add(tmp2);
        }
        for(String tmp2 : tmpHeure){
            resultat.add(tmp2);
        }
        return resultat;
    }

    private void ecoute(){
        try {
            String message;
            StringBuilder reponse;
            String reponsePerso;
            Vector<InfosClient> clientConcernes;
            Infos infos;
            String nomGroupe;
            String titreTicket;
            String idTicket;
            String contenu, requeteTemp;
            List<List<String>> tmp;
            List<List<String>> tmp2;
            int status;
            int numMessage;
            while (!isInterrupted()) {
                message = in.readLine();
                if (message == null)
                    break;
                System.out.println("R_"+message);
                String[] infosMessage = message.split(delimiteur);
                switch(infosMessage[0]) {
                    case "1":
                    	/*recoit:
	                    	1_0_nomGroupe_nomTicket_ContenuM
							1_1_nomGroupe_idTicket_ContenuM
							1_2_nomGroupe_idTicket_idMessage #un utilisateur nous dit qu'il a bien recu le message
							1_3_nomGroupe_idTicket_idMessage #un utilisateur nous dit qu'il a bien vu le message
                        */
                        int mode = Integer.parseInt(infosMessage[1]);
                        if (mode == 2 || mode == 3) {
                            nomGroupe = infosMessage[2];
                            idTicket = infosMessage[3];
                            String idM = infosMessage[4];
                            gestionBDD.requeteUpdate("statusm", "StatusM = "+(mode-1), "IdU = \"" + infosClient.login + "\" and IdM = " + idM+" and StatusM < 1");
                            this.prevenirChangementCouleur(nomGroupe, idTicket, String.valueOf(idM));
                        } else {
                            reponse = new StringBuilder("1");
                            reponse.append(delimiteur);
                            reponse.append(mode);
                            reponse.append(delimiteur);
                            int maxId;
                            ArrayList<String> tempListe;
                            nomGroupe = infosMessage[2];
                            reponse.append(nomGroupe);
                            reponse.append(delimiteur);
                            Calendar tempCalendar = Calendar.getInstance();
                            String tempDate = tempCalendar.get(Calendar.YEAR) + "-" + (tempCalendar.get(Calendar.MONTH) + 1) + "-" + tempCalendar.get(Calendar.DAY_OF_MONTH);
                            tempDate += " " + tempCalendar.get(Calendar.HOUR_OF_DAY) + ":" + tempCalendar.get(Calendar.MINUTE) + ":" + tempCalendar.get(Calendar.SECOND);
                            //System.out.println(tempDate);
                            tempListe = decouperDate(tempDate);
                            for (String element : tempListe) {
                                reponse.append(element);
                                reponse.append(delimiteur);
                            }
                            reponse.append(infosClient.login);
                            reponse.append(delimiteur);
                            if (mode == 0) {
                                titreTicket = infosMessage[3];
                                tmp = gestionBDD.requeteSelect("max(IdT)", "ticket", "");
                                maxId = Integer.parseInt(tmp.get(0).get(0)) + 1;
                                gestionBDD.requeteInsertInto("ticket (IdT, TitreT, DateT, NomG, IdU)", "(" + maxId + ",\"" + titreTicket + "\",\'" + tempDate + "\',\"" + nomGroupe + "\",\"" + infosClient.login + "\")", "");
                                idTicket = String.valueOf(maxId);
                                reponse.append(idTicket);
                                reponse.append(delimiteur);
                                reponse.append(titreTicket);
                                reponse.append(delimiteur);
                            } else {
                                idTicket = infosMessage[3];
                                reponse.append(idTicket);
                                reponse.append(delimiteur);
                            }
                            tmp = gestionBDD.requeteSelect("max(IdM)", "message", "");
                            maxId = Integer.parseInt(tmp.get(0).get(0)) + 1;
                            reponse.append(maxId);
                            reponse.append(delimiteur);
                            contenu = infosMessage[4];
                            reponse.append(contenu);
                            reponse.append(delimiteur);
                            reponse.append("1");
                            /* for(int i=0;i<6;i++) {
                            	date[i] = infosMessage[i + 5];
                        	}*/
                            gestionBDD.requeteInsertInto("message (IdM, ContenuM, CouleurM, DateM, IdT, IdU)", "(" + maxId + ",\"" + contenu + "\"," + 1 + ",\'" + tempDate + "\'," + idTicket + ",\"" + infosClient.login + "\")", "");
                            tmp = gestionBDD.requeteSelect("IdU", "appartient", "NomG = \"" + nomGroupe + "\" and IdU != \"" + infosClient.login + "\"");
                            tmp2 = gestionBDD.requeteSelect("IdU", "ticket", "IdT = " + idTicket + " and IdU != \"" + infosClient.login + "\"");
                            tmp2.removeAll(tmp);
                            tmp.addAll(tmp2);
                            /*System.out.println(reponse);
                            System.out.println(tmp);
                            System.out.println(gestionEmission.getClients());*/
                            requeteTemp = "(\"" + infosClient.login + "\", " + maxId + ", 2)";
                            List<String> clientsConcernes = new ArrayList<>();
                            for (List<String> tempList : tmp) {
                                String tamp = tempList.get(0);
                                clientsConcernes.add(tamp);
                                requeteTemp += ",(\"" + tamp + "\", " + maxId + ", 0)";
                            }
                            gestionBDD.requeteInsertInto("statusm (IdU, IdM, StatusM)", requeteTemp, "");
                            //On verifie si la couleur a change
                            this.prevenirChangementCouleur(nomGroupe,idTicket,String.valueOf(maxId));
                            infos = new Infos(reponse.toString(), clientsConcernes);
                            gestionEmission.envoyerClientsConcernes(infos);
                            //Alerte le client que le serveur a bien recu le message
                            reponsePerso = "1" + delimiteur + (2+mode) + delimiteur + nomGroupe + delimiteur + idTicket + delimiteur + String.valueOf(maxId);
                            for (String element : tempListe)
                            	reponsePerso+=delimiteur+element;
                            infosClient.clientEmission.envoyerMessage(reponsePerso);
                        }
                        break;
                    case "2":
                    	//recoit: 2_nomGroupe_idTicket_indice
                        nomGroupe = infosMessage[1];
                        idTicket = infosMessage[2];
                        StringBuilder tampon;
                        String tamp;
                        tampon = new StringBuilder("2");
                        tampon.append(delimiteur);
                        tampon.append(nomGroupe);
                        tampon.append(delimiteur);
                        tampon.append(idTicket);
                        gestionBDD.requeteUpdate("statusm TS, message TM","TS.StatusM = 2","TS.IdU = \""+infosClient.login+"\" and TM.IdT = "+idTicket+" and TS.IdM = TM.IdM and TS.StatusM < 2");
                        //On verifie si la couleur a change
                    	this.prevenirChangementCouleurTicket(nomGroupe,idTicket);
                        int indice = Integer.parseInt(infosMessage[3]);
                        if(indice != -1){
                            // Recuperer les messages a  envoyer dans la base de donnee
                            //tmp = gestionBDD.requeteSelect("IdM, ContenuM, IdU, CouleurM, DateM","message","IdT = "+idTicket+" ORDER BY DateM DESC LIMIT 30");
                            tmp = gestionBDD.requeteSelect("IdM, ContenuM, IdU, CouleurM, DateM","message","IdT = "+idTicket+" ORDER BY DateM DESC");
                            for (List<String> tempListe : tmp) {
                                String tempDate = tempListe.get(tempListe.size() - 1);
                                tempListe.remove(tempListe.size() - 1);
                                tempListe.addAll(decouperDate(tempDate));
                                for (String tempInfos : tempListe) {
                                    tampon.append(delimiteur);
                                    tampon.append(tempInfos);
                                }
                            }
                            infosClient.clientEmission.envoyerMessage(tampon.toString());	
                        }
                        break;
                    case "3":
                    	//recoit: 3_nomGroupe_idTicket_idMessage
                        nomGroupe = infosMessage[1];
                        idTicket = infosMessage[2];
                        numMessage = Integer.parseInt(infosMessage[3]);
                        reponse = new StringBuilder("3");
                        reponse.append(delimiteur);
                        reponse.append(nomGroupe);
                        reponse.append(delimiteur);
                        reponse.append(idTicket);
                        reponse.append(delimiteur);
                        reponse.append(numMessage);
                        tmp = gestionBDD.requeteSelect("TS.IdU, TS.StatusM","statusm TS, message TM","TS.IdM = "+numMessage+" and TS.IdU != \""+infosClient.login+"\" and TM.IdM = "+numMessage+" and TS.IdU != TM.IdU");
                        for(List<String> tempListe : tmp){
                        	reponse.append(delimiteur);
                            reponse.append(tempListe.get(0));
                            reponse.append(delimiteur);
                            reponse.append(tempListe.get(1));
                        }
                        infosClient.clientEmission.envoyerMessage(reponse.toString());
                        break;
                    case "4":
                    	//recoit: 4_nomGroupe_idTicket_booleen
                        nomGroupe = infosMessage[1];
                        idTicket = infosMessage[2];
                        boolean isWriting = Boolean.parseBoolean(infosMessage[3]);
                        reponse = new StringBuilder("4");
                        reponse.append(delimiteur);
                        reponse.append(nomGroupe);
                        reponse.append(delimiteur);
                        reponse.append(idTicket);
                        reponse.append(delimiteur);
                        reponse.append(infosClient.login);
                        reponse.append(delimiteur);
                        reponse.append(isWriting);
                        tmp = gestionBDD.requeteSelect("IdU", "appartient", "NomG = \"" + nomGroupe + "\" and IdU != \"" + infosClient.login + "\"");
                        tmp2 = gestionBDD.requeteSelect("IdU", "ticket", "IdT = " + idTicket + " and IdU != \"" + infosClient.login + "\"");
                        tmp2.removeAll(tmp);
                        tmp.addAll(tmp2);
                        List<String> clientsConcernes = new ArrayList<>();
                        for (List<String> tempList : tmp) {
                            tamp = tempList.get(0);
                            clientsConcernes.add(tamp);
                        }
                        infos = new Infos(reponse.toString(), clientsConcernes);
                        gestionEmission.envoyerClientsConcernes(infos);
                        break;
                    case "6":
                        nomGroupe = infosMessage[1];
                        idTicket = infosMessage[2];
                        List<List<String>> temp=gestionBDD.requeteSelect("TU.IdU", "utilisateur TU, appartient TA", "TA.NomG = \""+nomGroupe+"\" and TA.IdU = TU.IdU and TA.IdU != \"" + infosClient.login +"\"");
                        List<List<String>> temp2 = gestionBDD.requeteSelect("IdU", "ticket", "IdT = " + idTicket + " and IdU != \"" + infosClient.login + "\"");
                        temp2.removeAll(temp);
                        temp.addAll(temp2);
                        System.out.println(temp);
                        reponse = new StringBuilder("6");
                        reponse.append(delimiteur);
                        reponse.append(nomGroupe);
                        reponse.append(delimiteur);
                        reponse.append(idTicket);
                        Vector<InfosClient> clients = gestionEmission.getClients();
                        for (List<String> tempListe : temp) {
                            String tempInfos = tempListe.get(0);
                            for(InfosClient client : clients){
                                if(client.login.equals(tempInfos)){
                                    reponse.append(delimiteur);
                                    reponse.append(tempInfos);
                                }
                            }
                        }
                        infosClient.clientEmission.envoyerMessage(reponse.toString());
                        break;
                    }
                }
        } catch (IOException e) {
            // Problem reading from socket (communication is broken)
        }
        // Communication is broken. Interrupt both listener and sender threads
        infosClient.clientEmission.interrupt();
        gestionEmission.supprimerClient(infosClient);
    }
        /**
         * Until interrupted, reads messages from the client socket, forwards them
         * to the server dispatcher's queue and notifies the server dispatcher.
         */
    
    	public void prevenirChangementCouleurTicket(String nomGroupe, String idTicket){
    		List<List<String>> temp = gestionBDD.requeteSelect("IdM","message","IdT = "+idTicket);
    		for(List<String> tempID : temp){
    			this.prevenirChangementCouleur(nomGroupe, idTicket, tempID.get(0));
    		}
    	}

        public void prevenirChangementCouleur(String nomGroupe, String idTicket, String idMessage){
            List<List<String>> temp = gestionBDD.requeteSelect("CouleurM","message","IdM = "+idMessage);
            int couleurM = Integer.parseInt(temp.get(0).get(0));
            temp = gestionBDD.requeteSelect("StatusM","statusm","IdM = "+idMessage);
            int min = 3, statusM;
            for(List<String> status : temp){
                statusM = Integer.parseInt(status.get(0));
                if(statusM < min)
                	min = statusM;
            }
            if(min+1 > couleurM){
            	//La couleur du message est differente
            	couleurM = min+1;
                gestionBDD.requeteUpdate("message","CouleurM = "+couleurM,"IdM = "+idMessage);
                StringBuilder tampon = new StringBuilder("5");
                tampon.append(delimiteur);
                tampon.append(nomGroupe);
                tampon.append(delimiteur);
                tampon.append(idTicket);
                tampon.append(delimiteur);
                tampon.append(idMessage);
                tampon.append(delimiteur);
                tampon.append(couleurM);
                List<List<String>> tmp = gestionBDD.requeteSelect("IdU", "appartient", "NomG = \"" + nomGroupe + "\" and IdU != \"" + infosClient.login + "\"");
                List<List<String>> tmp2 = gestionBDD.requeteSelect("IdU", "ticket", "IdT = " + idTicket + " and IdU != \"" + infosClient.login + "\"");
                tmp2.removeAll(tmp);
                tmp.addAll(tmp2);
                List<String> clientsConcernes = new ArrayList<>();
                String tamp;
                for (List<String> tempList : tmp) {
                    tamp = tempList.get(0);
                    clientsConcernes.add(tamp);
                }
                clientsConcernes.add(infosClient.login);
                Infos infos = new Infos(tampon.toString(), clientsConcernes);
                gestionEmission.envoyerClientsConcernes(infos);
            }
        }

        public void run() {
            authentification();
            ecoute();
        }
    }
