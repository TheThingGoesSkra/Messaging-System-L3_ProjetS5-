package Client;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Ticket implements Comparable<Ticket>{
	private int iD;
	private String titre;
	private Calendar date;
	private int nbrMessageNonLu;
	private Groupe groupe;
	private Utilisateur createur;
	private NavigableSet<Message> listeMessages;
	private boolean alreadyLoad;
	private List<Utilisateur> userWriting;
	private Map<EnumStatusUtilisateur, NavigableSet<Utilisateur>> listeStatus;
	
	//Constructor
	public Ticket(int iD, String titre, Calendar date, int nbrMessageNonLu, Groupe groupe, Utilisateur createur){
		this.iD = iD;
		this.titre = titre;
		this.date = date;
		this.nbrMessageNonLu = nbrMessageNonLu;
		this.groupe = groupe;
		this.createur = createur;
		this.listeMessages = new TreeSet<>();
		this.alreadyLoad = false;
		this.userWriting = new LinkedList<>();
		this.listeStatus = new EnumMap<>(EnumStatusUtilisateur.class);
	}

	//----------------------------------------
	//Getter and setter
	//----------------------------------------
	public int getiD(){
		return this.iD;
	}
	public void setiD(int iD){
		this.iD = iD;
	}
	public int getNbrMessageNonLu() {
		return nbrMessageNonLu;
	}
	public void setNbrMessageNonLu(int nbrMessageNonLu) {
		this.nbrMessageNonLu = nbrMessageNonLu;
	}
	public void addNbrMessageNonLu(){
		int temp = this.getNbrMessageNonLu();
		this.setNbrMessageNonLu(temp+1);
	}
	public Groupe getGroupe() {
		return groupe;
	}
	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}
	public String getTitre() {
		return titre;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date){
		this.date = date;
	}
	public Calendar getDateDernierMessage(){
		NavigableSet<Message> listMessage = this.getListeMessages();
		if(!listMessage.isEmpty())
			return listMessage.last().getDate();
		else
			return this.getDate();
	}
	public Utilisateur getCreateur() {
		return createur;
	}
	public void setCreateur(Utilisateur utilisateur){
		this.createur = utilisateur;
	}
	public NavigableSet<Message> getListeMessages() {
		return this.listeMessages;
	}
	public boolean getAlreadyLoad(){
		return this.alreadyLoad;
	}
	public void setAlreadyLoad(boolean value){
		this.alreadyLoad = value;
	}
	public List<Utilisateur> getUserWriting(){
		return this.userWriting;
	}
	public Map<EnumStatusUtilisateur, NavigableSet<Utilisateur>> getListeStatus(){
        return this.listeStatus;
    }
    public void setListeStatus(Map<EnumStatusUtilisateur, NavigableSet<Utilisateur>> listeStatus){
        this.listeStatus=listeStatus;
    }
	@Override
	/*public String toString() {
		return "Ticket [iD=" + iD + ", titre=" + titre + ", date=" + ProgrammePrincipal.CalendarToString(date) + ", nbrMessageNonLu=" + nbrMessageNonLu
				+ ", groupe=" + groupe.getNom() + ", createur=" + createur.getID() + ", listeMessages=" + listeMessages + "]";
	}*/
	public String toString(){
		String temp = "";
		int nbMessNonLu = this.getNbrMessageNonLu();
		if(nbMessNonLu != 0)
			temp += "<html><b>";
		temp += this.getTitre()+" ("+nbMessNonLu+")";
		if(nbMessNonLu != 0)
			temp += "</b></html>";
		return temp;
	}

	//----------------------------------------	
	//Methods
	//----------------------------------------
	public boolean equals(Object o){
		if(o instanceof Ticket){
			Ticket t = (Ticket)o;
			return this.iD == t.iD;
		}
		return false;
	}
	@Override
	public int compareTo(Ticket o) {
		int comp = o.getDateDernierMessage().compareTo(this.getDateDernierMessage());
		if(comp == 0)
			comp = this.titre.compareTo(o.titre);
		return comp;
	}
	
	public void ajouterMessage(Message message){
		this.listeMessages.add(message);
	}
	
	public void ajouterUserWriting(Utilisateur user){
		this.userWriting.add(user);
	}
	public void enleverUserWriting(Utilisateur user){
		this.userWriting.remove(user);
	}
	
	public Message findMessage(int iDMessage){
		NavigableSet<Message> listeMessage = this.getListeMessages();
		for(Message tempMessage : listeMessage)
			if(tempMessage.getID() == iDMessage)
				return tempMessage;
		return null;
	}
	
	public int findMessageIdWithPosition(int pos){
		NavigableSet<Message> listMessage = this.getListeMessages();
		int ind = 0;
		for(Message mess : listMessage){
			if(ind == pos)
				return mess.getID();
			ind++;
		}
		return -1;
	}
	
	public int findMessagePositionWithID(int idMessage){
		NavigableSet<Message> listMessage = this.getListeMessages();
		int ind = 0;
		for(Message mess : listMessage){
			if(mess.getID() == idMessage)
				return ind;
			ind++;
		}
		return -1;
	}
	
	//----------------------------------------	
	//DEBUG
	//----------------------------------------
	//Ne pas utiliser la fonction suivante, car si deux tickets ont le meme nom il y aura surement un resultat faux
	//Les tickets sont identifiés par leur iD et non leur nom
	public static Ticket findTicketWithName(Groupe groupe, String name){
		NavigableSet<Ticket> listeTicket = groupe.getListeTickets();
		for(Ticket tempTicket : listeTicket)
			if(tempTicket.getTitre().equals(name))
				return tempTicket;
		return null;
	}
	
	public void afficherMessages(){
		NavigableSet<Message> listMessage = this.getListeMessages();
		System.out.println("---");
		for(Message mess : listMessage){
			System.out.println("_"+mess.getID()+"_"+mess.getContenu()+"_"+ProgrammePrincipal.CalendarToString(mess.getDate())+"_"+mess.getExpediteur());
		}
		System.out.println("---");
	}
}
