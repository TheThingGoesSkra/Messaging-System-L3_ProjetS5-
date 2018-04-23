package Serveur;

import java.net.*;
import java.io.*;

public class AccepterConnexion implements Runnable {

        private int LISTENING_PORT;
        private GestionBDD gestionBDD;
        
        public AccepterConnexion(GestionBDD gestionBDD, int LISTENING_PORT){
        	this.gestionBDD = gestionBDD;
        	this.LISTENING_PORT = LISTENING_PORT;
        }
        
        public void run() {
            // Open server socket for listening
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(LISTENING_PORT);
                System.out.println("Le serveur est a l'ecoute du port "+LISTENING_PORT+"\n");
            } catch (IOException e) {
                System.err.println("Le port "+LISTENING_PORT+" est deja  utilise !\n");
                e.printStackTrace();
            }

            // Start GestionEmission thread
            GestionEmission gestionEmission = new GestionEmission(gestionBDD);
            gestionEmission.start();

            // Accept and handle client connections
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    InfosClient clientInfo = new InfosClient();
                    clientInfo.socket = socket;
                    Reception reception = new Reception(clientInfo, gestionEmission, gestionBDD);
                    Emission emission = new Emission(clientInfo, gestionEmission);
                    clientInfo.clientReception = reception;
                    clientInfo.clientEmission = emission;
                    reception.start();
                    emission.start();
                } catch (IOException e) {
                    System.err.println("Erreur serveur\n");
                    e.printStackTrace();
                }
            }
        }

        /*public static void main(String[] args){
            AccepterConnexion test=new AccepterConnexion(null);
            test.run();
        }*/
    }
