package Client;

import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Groupe implements Comparable<Groupe>{
	private String nom;
	private Boolean typeNormal;
	private Set<Utilisateur> listeUtilisateurs;
	private NavigableSet<Ticket> listeTickets;
	
	//Constructor
	public Groupe(String nom, Boolean typeNormal){
		this.nom = nom;
		this.typeNormal = typeNormal;
		this.listeUtilisateurs = new HashSet<>();
		this.listeTickets = new TreeSet<>();
	}

	//----------------------------------------
	//Getter and setter
	//----------------------------------------
	public String getNom() {
		return nom;
	}
	public Boolean getTypeNormal() {
		return typeNormal;
	}
	public Set<Utilisateur> getListeUtilisateurs() {
		return listeUtilisateurs;
	}
	public NavigableSet<Ticket> getListeTickets() {
		return listeTickets;
	}
	@Override
	public String toString() {
		String temp = "Groupe [nom=" + nom + ", typeNormal=" + typeNormal;
		for(Utilisateur user : this.listeUtilisateurs)
			temp += ", utilisateur="+user.getID();
		temp += ", listeTickets=" + listeTickets + "]";
		return temp;
	}
	
	//----------------------------------------
	//Methods
	//----------------------------------------
	public boolean equals(Object o){
		if(o instanceof Groupe){
			Groupe g = (Groupe) o;
			return this.getNom().equals(g.getNom());
		}
		return false;
	}
	@Override
	public int compareTo(Groupe o) {
		int comp = this.nom.compareTo(o.nom);
		return comp;
	}
	
	public void lierUtilisateur(Utilisateur utilisateur){
		this.listeUtilisateurs.add(utilisateur);
		utilisateur.ajouterGroupe(this);
	}
	public void lierTicket(Ticket ticket){
		this.listeTickets.add(ticket);
		ticket.setGroupe(this);
	}
	
	public Ticket findTicketWithID(int iD){
		NavigableSet<Ticket> listeTicket = this.getListeTickets();
		for(Ticket tempTicket : listeTicket){
			if(tempTicket.getiD() == iD)
				return tempTicket;	
		}
		return null;
	}
	public Ticket getTicketAtPos(int pos){
		NavigableSet<Ticket> temp = this.getListeTickets();
		int i = 0;
		for(Ticket ticket : temp){
			if(i == pos)
				return ticket;
			i++;
		}			
		return null;
	}
	
	//----------------------------------------	
	//DEBUG
	//----------------------------------------	
	public void afficherTickets(){
		NavigableSet<Ticket> listTicket = this.getListeTickets();
		System.out.println("---");
		for(Ticket tick : listTicket){
			System.out.println("_"+tick.getiD()+"_"+tick.getTitre()+"_"+ProgrammePrincipal.CalendarToString(tick.getDateDernierMessage()));
		}
		System.out.println("---");
	}
}
