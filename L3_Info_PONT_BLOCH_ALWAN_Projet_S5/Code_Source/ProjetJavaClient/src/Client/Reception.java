package Client;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reception extends Thread {

	public BufferedReader in;
	public ProgrammePrincipal programmePrincipal;
	private static String delimiteur = ProgrammePrincipal.delimiteur;
	public boolean returnToMainScreen;

	public Reception(BufferedReader in, ProgrammePrincipal programmePrincipal) {
		this.in = in;
		this.programmePrincipal = programmePrincipal;
		this.returnToMainScreen = false;
	}
	
	public void terminer(){
		/*try {
			this.in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		this.interrupt();
	}
	
	private void reconnexion(){
		this.programmePrincipal.horsLigne = true;
		while(!this.programmePrincipal.reconnexionServeur()){
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		try {
			PrintWriter out = new PrintWriter(new PrintStream(this.programmePrincipal.socket.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(this.programmePrincipal.socket.getInputStream()));
            // Create and start Emission thread
            this.programmePrincipal.emission = new Emission(out);
            this.programmePrincipal.emission.setDaemon(true);
            this.programmePrincipal.emission.start();
            // Create and start Reception thread
			this.in = input;
			//this.setDaemon(true);
			this.programmePrincipal.reAuthentification();
            this.run();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public void run() {
		String message = null;
		String nomGroupe;
		/*String titreTicket;
		String contenu;
		String date;
		int status;
		int numMessage;*/
		while (!isInterrupted()) {
			try {
				message = in.readLine();
			/*}catch(InterruptedIOException i){
				System.out.println("waw!");
                Thread.currentThread().interrupt();
                break;  */          	
			}catch (SocketException ex) {
				System.out.println("paswaw!");
				if(returnToMainScreen){
					Thread.currentThread().interrupt();
	                break;
				}else{
					this.reconnexion();
				}            
			}catch (IOException e) {
				e.printStackTrace();
			}
			if (message == null)
				break;
			message=message.replace("\\n", "\n");
			System.out.println("R_"+message);
			String[] infosMessage = message.split(delimiteur);
			int taille = infosMessage.length;
			switch (infosMessage[0]) {
			case "0":
				//recoit: 0_reponse_IdU_NomU_PrenomU_TypeU_[NomG_TypeG_NBUSER_[IdU_NomU_PrenomU_TypeU]_NBTICKET_[IdT_TitreT_IdU_DateT_NbrMessNonLu_IdM_ContenuM_IdU_CouleurM_DateM]]
		        if (infosMessage[1].equals("1")) {
		            ArrayList<String> utilisateurPrincipal = new ArrayList<>();
		            utilisateurPrincipal.add(infosMessage[2]); //Login
		            utilisateurPrincipal.add(infosMessage[3]); //Nom
		            utilisateurPrincipal.add(infosMessage[4]); //Prenom
		            utilisateurPrincipal.add(infosMessage[5]); //Type
		            ArrayList<ArrayList<String>> groupes = new ArrayList<>();
		            //System.out.println(taille);
		            for (int i = 6; i < taille;) {
		                ArrayList<String> groupe = new ArrayList<>();
		                for (int k = 0; k < 2; k++) {
		                    groupe.add(infosMessage[i++]);
		                }
		                int nbUtilisateurs = Integer.parseInt(infosMessage[i]);
		                groupe.add(infosMessage[i++]);
		                int tmp = i;
		                //System.out.println("1_" + tmp);
		                for (int j = tmp; j < (tmp + nbUtilisateurs * 4); ) {
		                    for (int k = 0; k < 4; k++) {
		                        groupe.add(infosMessage[j++]);
		                    }
		                    i = j;
		                }
		                int nbTickets = Integer.parseInt(infosMessage[i]);
		                groupe.add(infosMessage[i++]);
		                tmp = i;
		                //System.out.println("2_" + tmp);
		                for (int j = tmp; j < (tmp + nbTickets * 20); ) {
		                    for (int k = 0; k < 20; k++) {
		                        groupe.add(infosMessage[j++]);
		                    }
		                    i = j;
		                }
		                groupes.add(groupe);
		            }
		            //System.out.println(utilisateurPrincipal);
		            //System.out.println(groupes);
		            this.programmePrincipal.remplirDataAuthentification(utilisateurPrincipal,groupes);
		        }else{
		        	this.programmePrincipal.mauvaiseConnexion();
		        }
				break;
			case "1":
				/*recoit:
					1_0_nomGroupe_date_IdU_idTicket_nomTicket_IdM_ContenuM_CouleurM
					1_1_nomGroupe_date_IdU_idTicket_IdM_ContenuM_CouleurM
					#message recu par le serveur:
					1_2_nomGroupe_idTicket_idMessage #nouveau ticket
					1_3_nomGroupe_idTicket_idMessage #nouveau message
				 */
				String mode = infosMessage[1];
				nomGroupe = infosMessage[2];
				if (mode.equals("2") || mode.equals("3")) { 
					int idTicket = Integer.parseInt(infosMessage[3]);
					int idM = Integer.parseInt(infosMessage[4]);
					List<String> date = new ArrayList<>();
					for(int i = 0; i < 6; ++i)
						date.add(infosMessage[5+i]);
					this.programmePrincipal.remplirDataMessageRecuServeur(nomGroupe, idTicket, idM, mode,date);
				} else {
					List<String> infos = new ArrayList<>();
					List<String> ticket = new ArrayList<>();
					List<String> tamponMessage = new ArrayList<>();

					int i;
					int j = 0;
					for (i = 3; i <= 9; i++) {
						infos.add(infosMessage[i]);
					}
					if (mode.equals("0")) {
						ticket.add(infosMessage[i++]); // IdTicket
						ticket.add(infosMessage[i++]); // nomTicket
						//j = 12;
					}else if (mode.equals("1")) {
						ticket.add(infosMessage[i++]); // IdTicket
						//j = 11;
					}
					j = i;
					for (i = j; i <= j + 2; i++) {
						tamponMessage.add(infosMessage[i]);
					}
					/*System.out.println("_1_"+infos);
					System.out.println("_1_"+ticket);
					System.out.println("_1_"+tamponMessage);*/
					String retour = "1"+delimiteur;
					if(this.programmePrincipal.getConversationGuiTicket() != null && this.programmePrincipal.getConversationGuiTicket().getiD() == Integer.parseInt(ticket.get(0)))
						retour += "3";
					else
						retour += "2";
					retour += delimiteur+nomGroupe+delimiteur+ticket.get(0)+delimiteur+tamponMessage.get(0);
					this.programmePrincipal.emission.envoyerMessage(retour);
					this.programmePrincipal.remplirDataNouveauMessage(nomGroupe, infos, ticket, tamponMessage, Integer.parseInt(mode));
				}
				break;
			case "2":
				//recoit: 2_nomGroupe_idTicket_[IdM_ContenuM_IdU_CouleurM_DateM]
				String idTicket;
				List<List<String>> tmpMessages;
				nomGroupe = infosMessage[1];
				idTicket = infosMessage[2];
				tmpMessages = new ArrayList<>();
				for (int i = 3; i < taille;) {
					ArrayList<String> tmpMessage = new ArrayList<>();
					for (int k = 0; k < 10; k++) {
						tmpMessage.add(infosMessage[i++]);
					}
					tmpMessages.add(tmpMessage);
				}
				/*System.out.println("_2_"+nomGroupe);
				System.out.println("_2_"+idTicket);
				System.out.println("_2_"+tmpMessages);*/
				this.programmePrincipal.remplirDataConversation(nomGroupe, idTicket, tmpMessages);
				break;
            case "3":
            	//recoit: 3_nomGroupe_idTicket_idMessage_[IdU_Status]
                String idMessage;
                List<List<String>> statusM = new ArrayList<>();
                nomGroupe=infosMessage[1];
                idTicket=infosMessage[2];
                idMessage=infosMessage[3];
                taille = infosMessage.length;
                for (int i = 4; i < taille;) {
                    ArrayList<String> tmpUtilisateurs = new ArrayList<>();
                    tmpUtilisateurs.add(infosMessage[i++]);
                    tmpUtilisateurs.add(infosMessage[i++]);
                    statusM.add(tmpUtilisateurs);
                }
                this.programmePrincipal.remplirDataStatusMessage(nomGroupe, idTicket, idMessage, statusM);
                break;
            case "4":
                //recoit: 4_nomGroupe_idTicket_IdU_booleen
                //traduis: String idU, Boolean ecris, String nomGroupe, String idTicket
                nomGroupe = infosMessage[1];
                idTicket = infosMessage[2];
                String idU = infosMessage[3];
                boolean ecris=Boolean.parseBoolean(infosMessage[4]);
                this.programmePrincipal.remplirDataUserWriting(nomGroupe, idTicket, idU, ecris);
                break;
            case "5":
                //recoit: 5_nomGroupe_idTicket_idMessage_CouleurM
                //traduit:  String nomGroupe, String idTicket, String idMessage, String couleurM
                nomGroupe = infosMessage[1];
                idTicket = infosMessage[2];
                idMessage = infosMessage[3];
                String CouleurM = infosMessage[4];
                this.programmePrincipal.remplirDataCouleurMessage(nomGroupe, idTicket, idMessage, CouleurM);
                break;
            case "6" :
                nomGroupe = infosMessage[1];
                int idT= Integer.parseInt(infosMessage[2]);
                List<String> utilisateurs = new ArrayList<>();
                taille = infosMessage.length;
                for(int i=3;i<taille;i++){
                       utilisateurs.add(infosMessage[i]);
                }
                System.out.println(utilisateurs);
                this.programmePrincipal.remplirDataInfos(nomGroupe, idT, utilisateurs);
                break;
			}
		}
	}
}
