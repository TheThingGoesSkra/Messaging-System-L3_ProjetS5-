package Client;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Message implements Comparable<Message> {
	private int iD;
	private String contenu;
	private Calendar date;
	private Map<EnumStatusMessage, NavigableSet<Utilisateur>> listeUtilisateurStatus;
	private Utilisateur expediteur;
	private int couleur; //0(gris) _ 1(rouge) _ 2(orange) _ 3(vert) 
	
	//Constructor
	public Message(int iD, String contenu, Calendar date, Utilisateur expediteur, int couleur){
		this.iD = iD;
		this.contenu = contenu;
		this.date = date;
		this.expediteur = expediteur;
		this.listeUtilisateurStatus = new EnumMap<>(EnumStatusMessage.class);
		this.listeUtilisateurStatus.put(EnumStatusMessage.ENATTENTE, new TreeSet<>());
		this.listeUtilisateurStatus.put(EnumStatusMessage.RECU, new TreeSet<>());
		this.listeUtilisateurStatus.put(EnumStatusMessage.LU, new TreeSet<>());
		this.couleur = couleur;
	}
	
	//----------------------------------------
	//Getter and setter
	//----------------------------------------
	public String getContenu() {
		return contenu;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date){
		this.date = date;
	}
	public Map<EnumStatusMessage, NavigableSet<Utilisateur>> getListeUtilisateurStatus() {
		return listeUtilisateurStatus;
	}
	public Utilisateur getExpediteur() {
		return expediteur;
	}
	public void setExpediteur(Utilisateur utilisateur){
		this.expediteur = utilisateur;
	}
	public int getCouleur(){
		return couleur;
	}
	public void setCouleur(int couleur){
		this.couleur = couleur;
	}
	public int getID(){
		return iD;
	}
	public void setID(int iD){
		this.iD = iD;
	}
	@Override
	public String toString() {
		return "Message [contenu=" + contenu + ", date=" + ProgrammePrincipal.CalendarToString(date) + ", listeUtilisateurStatus=" + listeUtilisateurStatus
				+ ", expediteur=" + expediteur.getID() + "]";
	}
	
	//----------------------------------------
	//Methods
	//----------------------------------------
	public boolean equals(Object o){
		if(o instanceof Message){
			Message m = (Message)o;
			return this.getID() == m.getID();
		}
		return false;
	}
	@Override
	public int compareTo(Message mess) {
		int comp = this.date.compareTo(mess.date);
		if(comp == 0)
			comp = this.getID() - mess.getID(); 
		return comp;
	}
	public void ajouterUtilisateurStatus(EnumStatusMessage status, Utilisateur utilisateur){
		this.listeUtilisateurStatus.get(status).add(utilisateur);
	}
}
