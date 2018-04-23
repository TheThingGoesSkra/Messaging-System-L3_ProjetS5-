package Serveur;

import java.net.*;
import java.util.*;

public class GestionEmission extends Thread {

        private GestionBDD gestionBDD;

        private Vector<Infos> messageQueue = new Vector<>();
        private Vector<InfosClient> clients = new Vector<>();

        /**
         * Adds given client to the server's client list.
         */

        public GestionEmission(GestionBDD gestionBDD){
            this.gestionBDD=gestionBDD;
        }

    public Vector<InfosClient> getClients() {
        return clients;
    }
    public synchronized void ajouterClient(InfosClient clientInfo) {
        clients.add(clientInfo);
    }

    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */

    public synchronized void supprimerClient(InfosClient clientInfo) {
        int clientIndex = clients.indexOf(clientInfo);
        if (clientIndex != -1)
            clients.removeElementAt(clientIndex);
        System.out.println("Deconnexion de "+clientInfo.login);
    }

    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * envoyerClientsConcernés method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void envoyerClientsConcernes(Infos infosMessage) {
        messageQueue.add(infosMessage);
        notify();
    }


    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by envoyerClientsConcernés method.
     */

    private synchronized Infos getNextMessageFromQueue() throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        Infos infosMessage = messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return infosMessage;
    }

    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */

    private synchronized void envoyerAuxClients(Infos infosMessage) {
        for (String tmpLogin: infosMessage.clientsConcernes) {
            for(InfosClient infosClients : clients){
                if(infosClients.login.equals(tmpLogin)){
                    infosClients.clientEmission.envoyerMessage(infosMessage.message);
                    break;
                }
            }
        }
    }

    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */

    public void run() {
        try {
            while (true) {
                Infos infosMessage = getNextMessageFromQueue();
                envoyerAuxClients(infosMessage);
            }
        } catch (InterruptedException ie) {
            // Thread interrupted. Stop its execution
        }

    }
}
