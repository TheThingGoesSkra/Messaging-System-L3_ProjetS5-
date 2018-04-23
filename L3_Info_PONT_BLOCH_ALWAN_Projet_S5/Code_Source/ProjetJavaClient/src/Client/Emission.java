package Client;

import java.io.*;
import java.util.Vector;

class Emission extends Thread {

	private Vector messageQueue = new Vector();
	private PrintWriter out;
	
	public Emission(PrintWriter aOut) {
		out = aOut;
	}
	
	public void terminer(){
		this.out.close();
		this.interrupt();
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
	 *         is empty, falls in sleep until notified for message arrival by
	 *         envoyerMessage method.
	 */

	private synchronized String getNextMessageFromQueue() throws InterruptedException {
		while (messageQueue.size() == 0)
			wait();
		String message = (String) messageQueue.get(0);
		messageQueue.removeElementAt(0);
		return message;
	}

	/**
	 * Sends given message to the client's socket.
	 */

	private void envoyerMessageAuServeur(String message) {
		out.println(message);
		out.flush();
	}

	/**
	 * Until interrupted, reads messages from the message queue and sends them
	 * to the client's socket.
	 */

	public void run() {
		try {
			while (!isInterrupted()) {
				String message = getNextMessageFromQueue();
				envoyerMessageAuServeur(message);
			}
		} catch (Exception e) {
			// Commuication problem
		}
	}
}
