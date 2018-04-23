package Serveur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GestionBDD {
	String url;
	String login;
	String password;
	List<List<String>> result;
	
	//Constructor
	public GestionBDD(String url, String login, String password){
		this.url = url;
		this.login = login;
		this.password = password;
		this.result = new ArrayList<>();
	}
	
	//Getter and setter
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogin() {
		return this.login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<List<String>> getResult() {
		return this.result;
	}
	
	//Methods
	public synchronized List<List<String>> requete(String requete, int mode){
		try {
			this.result.clear();
			Connection conn = DriverManager.getConnection(this.url, this.login, this.password);
			
		    //Creation d'un objet Statement
		    Statement state = conn.createStatement();
		    switch(mode){
		    	case 0:
				    //L'objet ResultSet contient le resultat de la requete SQL
				    ResultSet result = state.executeQuery(requete);
				    //On recupere les MetaData
				    ResultSetMetaData resultMeta = result.getMetaData();
				    
				    List<String> temp;
				    while (result.next()) {
				    	temp = new ArrayList<>();
				        for (int i = 1; i <= resultMeta.getColumnCount(); ++i)
				        	temp.add(result.getObject(i).toString());
				        this.result.add(temp);
				    }
				    
				    result.close(); // Chaque requete
				    break;
		    	case 1:
		    		state.executeUpdate(requete);
		    }
		    state.close(); // A la fin
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<List<String>> tempResult = new ArrayList<>();
		tempResult.addAll(this.result);
		return tempResult;
	}
	public List<List<String>> requeteSelect(String select, String from, String where){
		String requete = "";
		requete = "SELECT DISTINCT "+select+" FROM "+from;
		if(where!="")
			requete += " WHERE "+where;
		return this.requete(requete,0);
	}
	public List<List<String>> requeteInsertInto(String insertInto, String values, String where){
		String requete = "";
		requete = "INSERT INTO "+insertInto+" VALUES "+values;
		if(where!="")
			requete += " WHERE "+where;
		return this.requete(requete,1);
	}
	public List<List<String>> requeteUpdate(String nomTable, String colonnes, String where){
		String requete = "";
		requete = "UPDATE "+nomTable+" SET "+colonnes;
		if(where!="")
			requete += " WHERE "+where;
		return this.requete(requete,1);
	}
	public List<List<String>> requeteCreateTable(String nomTable, String nomColonnes_types){
		String requete = "";
		requete = "CREATE TABLE "+nomTable+" ("+nomColonnes_types+")";
		return this.requete(requete,1);		
	}
	private void printResultData(){
		System.out.println(this.result);
	}
	
	//Test
	public static void main(String[] args){
		String url = "jdbc:mysql://localhost:3306/ProjetS5BDD";
		url += "?autoReconnect=true&useSSL=false";
		String user = "root";
		String passwd = "";
		GestionBDD myBDD = new GestionBDD(url,user,passwd);
		myBDD.requete("SELECT * FROM utilisateur",0);
		myBDD.printResultData();
	}
}
