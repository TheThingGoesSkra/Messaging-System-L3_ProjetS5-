package Client;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Utilisateur implements Comparable<Utilisateur>{
	private String iD;
	private String prenom;
	private String nom;
	private Boolean typeNormal;
	private Boolean utilisateurPrincipal;
	private NavigableSet<Groupe> listeGroupes;
	
	//Constructor
	public Utilisateur(String iD, String prenom, String nom, Boolean typeNormal, Boolean utilisateurPrincipal) {
		this.iD = iD;
		this.prenom = prenom;
		this.nom = nom;
		this.typeNormal = typeNormal;
		this.utilisateurPrincipal = utilisateurPrincipal;
		this.listeGroupes = new TreeSet<>();
	}

	//----------------------------------------
	//Getter and setter
	//----------------------------------------
	public String getID(){
		return this.iD;
	}
	public Boolean getUtilisateurPrincipal() {
		return utilisateurPrincipal;
	}
	public void setUtilisateurPrincipal(Boolean utilisateurPrincipal) {
		this.utilisateurPrincipal = utilisateurPrincipal;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getNom() {
		return nom;
	}
	public Boolean getTypeNormal() {
		return typeNormal;
	}
	public NavigableSet<Groupe> getListeGroupes() {
		return listeGroupes;
	}
	@Override
	public String toString() {
		String temp = "Utilisateur [iD=" + iD + ", prenom=" + prenom + ", nom=" + nom + ", typeNormal=" + typeNormal
				+ ", utilisateurPrincipal=" + utilisateurPrincipal;
		for(Groupe groupe : this.listeGroupes)
			temp += ", groupe="+groupe.getNom();
		temp += "]";
		return temp;
	}
	
	//----------------------------------------
	//Methods
	//----------------------------------------
	public boolean equals(Object o){
		if(o instanceof Utilisateur){
			Utilisateur temp = (Utilisateur)o;
			return this.getID().equals(temp.getID());
		}
		return false;
	}
	public int hashCode(){
		return 31 * this.getID().hashCode();
	}
	@Override
	public int compareTo(Utilisateur o){
		/*int comp = this.prenom.compareTo(o.prenom);
		if(comp == 0)
			comp = this.nom.compareTo(o.nom);*/
		int comp = this.getID().compareTo(o.getID());
		return comp;
	}
	public void ajouterGroupe(Groupe groupe){
		this.listeGroupes.add(groupe);
	}
}
