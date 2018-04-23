package Serveur;

import java.net.Socket;

public class InfosClient {

        public Socket socket = null;
        public Reception clientReception = null;
        public Emission clientEmission = null;
        public boolean authentifier;

        public String login;
        public String prenom;
        public String nom;
        public int type;

        public InfosClient(){
            this.authentifier=false;
        }

    public void setAuthentifier(boolean authentifier) {
        this.authentifier = authentifier;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
