package Serveur;

import java.io.*;
import java.net.*;
import java.util.*;

public class Emission extends Thread {

        private Vector<String> messageQueue = new Vector<>();
        private static String delimiteur = ProgrammePrincipal.delimiteur;

        private GestionEmission gestionEmission;
        private InfosClient clientInfo;

        private PrintWriter out;

        public Emission(InfosClient clientInfo, GestionEmission gestionEmission) throws IOException {
            this.clientInfo = clientInfo;
            this.gestionEmission = gestionEmission;
            Socket socket = clientInfo.socket;
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * envoyerMessage is called by other threads (ServeDispatcher).
     */

    public synchronized void envoyerMessage(String message) {
    	System.out.println("E_"+message);
    	message=message.replace("\n", "\\n");
        messageQueue.add(message);
        notify();
    }


    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by envoyerMessage
     * method.
     */

    private synchronized String getNextMessageFromQueue() throws InterruptedException {
        while (messageQueue.size()==0)
            wait();
        String message = (String) messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */

    private void envoyerMessageAuClient(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */

    public void run() {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                envoyerMessageAuClient(message);
            }
        } catch (Exception e) {
            // Commuication problem
        }
        // Communication is broken. Interrupt both listener and sender threads
        clientInfo.clientReception.interrupt();
        gestionEmission.supprimerClient(clientInfo);
    }
}
