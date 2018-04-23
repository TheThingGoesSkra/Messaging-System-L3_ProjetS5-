/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client.*;

public class ConversationGUI extends javax.swing.JPanel {

	ProgrammePrincipal progPrincipal;
	Ticket ticket;
	public int xLastSelMessage;
	public int yLastSelMessage;
	
    private NavigableSet<Message> messagesGauche=new TreeSet<>();
    private NavigableSet<Message> messagesDroite=new TreeSet<>();

    private ArrayList<JPanel> jpanels = new ArrayList<>();
    private ArrayList<JPanel> jpanelsGauches = new ArrayList<>();
    private ArrayList<JPanel> jpanelsDroites = new ArrayList<>();
  
    /**
     * Creates new form Discussion
     */
    public ConversationGUI(ProgrammePrincipal prog, Ticket ticket) {
    	this.progPrincipal = prog;
    	if(ticket != null){
        	this.ticket = ticket;
            initialiser(ticket); 
    		this.setVisible(true);
    		this.xLastSelMessage = 0;
    		this.yLastSelMessage = 0;
    	}
    }
    
    public Ticket getTicket(){
    	return this.ticket;
    }
    
    public void initialiser(Ticket ticket){ 
    	this.ticket = ticket;
        this.removeAll();
        messagesGauche.clear();
        messagesDroite.clear();
        jpanels.clear();
        jpanelsGauches.clear();
        jpanelsDroites.clear();
        
    	construireListes(); 
        this.setBackground(new java.awt.Color(251, 251, 251));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        GroupLayout.ParallelGroup groupePBase1=layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup groupePBase2=layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup groupePBase3=layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.ParallelGroup groupePBase4=layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup groupeSBase1=layout.createSequentialGroup();
        GroupLayout.SequentialGroup groupeSBase2=layout.createSequentialGroup();
        GroupLayout.SequentialGroup groupeSBase3=layout.createSequentialGroup();
        
        int i=0;
        for(Message m : messagesGauche){
            groupePBase3.addComponent(jpanelsGauches.get(i), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            i++;
        }
        groupePBase3.addGap(0, 400, Short.MAX_VALUE);
        groupeSBase2.addGroup(groupePBase3);
        int j=0;
        for(Message m : messagesDroite){
            groupePBase4.addComponent(jpanelsDroites.get(j), javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            j++;
        }
        groupeSBase3.addGap(0,0,Short.MAX_VALUE);
        groupeSBase3.addGroup(groupePBase4);
        groupePBase2.addGroup(groupeSBase2);
        groupePBase2.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupeSBase3);
        groupeSBase1.addContainerGap();
        groupeSBase1.addGroup(groupePBase2);
        groupeSBase1.addContainerGap();
        groupePBase1.addGroup(groupeSBase1);
        layout.setHorizontalGroup(groupePBase1);
        
        
        GroupLayout.ParallelGroup groupe2PBase1=layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup groupe2SBase1=layout.createSequentialGroup();
        groupe2SBase1.addContainerGap(19, Short.MAX_VALUE);
        i=0;
        for(Message m : this.ticket.getListeMessages()){
            groupe2SBase1.addComponent(jpanels.get(i), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            groupe2SBase1.addGap(18, 18, 18);
            i++;
        }
        groupe2PBase1.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, groupe2SBase1);
        layout.setVerticalGroup(groupe2PBase1);  
        
        this.revalidate();
        this.repaint();        
    }
    
     public void construireListes(){
         for(Message m : this.ticket.getListeMessages()){
        	 //System.out.println(m.toString());
             if(m.getExpediteur().getUtilisateurPrincipal()){
              	this.ajouterMessageDroite(m);
             }else{
                  this.ajouterMessageGauche(m);
             }  
         }
     }
    
     public void ajouterMessageGauche(Message message){
        messagesGauche.add(message);
        JPanel jpanel=jPanelMessage(message);
        jpanels.add(jpanel);
        jpanelsGauches.add(jpanel);
     }
    
     public void ajouterMessageDroite(Message message){
        messagesDroite.add(message);
        JPanel jpanel=jPanelMessage(message);
        jpanels.add(jpanel);
        jpanelsDroites.add(jpanel);
     }
    
     public JPanel jPanelMessage(Message message){
        JPanel jpanel = new javax.swing.JPanel();
        JPanel jpanel2 = new javax.swing.JPanel();
        javax.swing.JTextArea contenu = new  javax.swing.JTextArea();
        JLabel heure = new javax.swing.JLabel();
        javax.swing.JLabel expediteur = new javax.swing.JLabel();
        jpanel.setBackground(new java.awt.Color(235, 233, 233));

        if(message.getCouleur()==0)
            jpanel2.setBackground(Color.GRAY);
        else if(message.getCouleur()==1)
            jpanel2.setBackground(new java.awt.Color(255, 0, 51));
        else if(message.getCouleur()==2)
            jpanel2.setBackground(new java.awt.Color(255, 153, 51));
        else if(message.getCouleur()==3)
            jpanel2.setBackground(new java.awt.Color(51, 204, 0));
        
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jpanel2);
        jpanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        
        expediteur.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        expediteur.setText(message.getExpediteur().getPrenom()+" "+message.getExpediteur().getNom());
        if(!message.getExpediteur().getTypeNormal())
            expediteur.setForeground(new java.awt.Color(204, 40, 71));
            
        contenu.setEditable(false);
        contenu.setOpaque(false);
        contenu.setBackground(new java.awt.Color(235, 233, 233));
        contenu.setBorder(null);
        contenu.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        contenu.setText(message.getContenu());
        String[] tamponLignes = message.getContenu().split("\n");
        int i=0;
        while(i<tamponLignes.length){
            String tamponLigne=tamponLignes[i];
            if(tamponLigne.length()>40){
                        contenu.setColumns(40);
                        contenu.setLineWrap(true);
                        contenu.setWrapStyleWord(true);
                        i=tamponLignes.length;
                        break;
            }
            i++;
        }
        
        Calendar calendar = Calendar.getInstance(); 
        String tampon = "";
        int decalage=0;
        String[] jours={"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};
        if(calendar.get(Calendar.YEAR)==message.getDate().get(Calendar.YEAR)){
            if(calendar.get(Calendar.MONTH)==message.getDate().get(Calendar.MONTH) && (calendar.get(Calendar.DAY_OF_MONTH)-message.getDate().get(Calendar.DAY_OF_MONTH)<7)){
                if(calendar.get(Calendar.MONTH)==message.getDate().get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH)==message.getDate().get(Calendar.DAY_OF_MONTH)){
                    tampon += String.valueOf(message.getDate().get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(message.getDate().get(Calendar.MINUTE));
                    decalage=38;
                }else{
                    switch(calendar.get(Calendar.DAY_OF_WEEK)){
                        case 1:
                            decalage=76;
                            break;
                        case 2:
                            decalage=75;
                            break;
                        case 3:
                            decalage=93;
                            break;
                        case 4:
                            decalage=75;
                            break;
                        case 5:
                            decalage=96;
                            break;
                        case 6: 
                            decalage=86;
                            break;
                        case 7:
                            decalage=95;
                            break;
                    }
                    tampon += jours[message.getDate().get(Calendar.DAY_OF_WEEK)-1]+" "+String.valueOf(message.getDate().get(Calendar.HOUR_OF_DAY));
                    tampon += ":"+String.valueOf(message.getDate().get(Calendar.MINUTE));
                }
            }else{
                decalage=75;
                tampon += message.getDate().get(Calendar.DAY_OF_MONTH)+"/"+(message.getDate().get(Calendar.MONTH)+1);
                tampon += " "+message.getDate().get(Calendar.HOUR_OF_DAY)+":"+message.getDate().get(Calendar.MINUTE);
            }
        }else{
            decalage=108;
            tampon += message.getDate().get(Calendar.DAY_OF_MONTH)+"/"+(message.getDate().get(Calendar.MONTH)+1)+"/"+message.getDate().get(Calendar.YEAR);
            tampon += " "+message.getDate().get(Calendar.HOUR_OF_DAY)+":"+message.getDate().get(Calendar.MINUTE);
        }
        
        heure.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        heure.setText(tampon);
        heure.setForeground(Color.DARK_GRAY);
        
        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jpanel);
        jpanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                //.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expediteur)
                    .addComponent(contenu))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(heure, javax.swing.GroupLayout.PREFERRED_SIZE, decalage, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(expediteur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(contenu, javax.swing.GroupLayout.PREFERRED_SIZE,  javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heure, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                /*.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlabel)
                .addComponent(jText)
                .addContainerGap())*/
        );        
        jpanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				demanderStatusMessage(jpanel);
			}
		});
        contenu.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				//Here
				JPanel tempJpanel = (JPanel)contenu.getParent();
				demanderStatusMessage(tempJpanel);
			}
		});
        return jpanel;
     }
     
     public void demanderStatusMessage(JPanel jpanel){
		int cpt = 0;
		this.xLastSelMessage = (int)jpanel.getLocationOnScreen().getX();
		this.yLastSelMessage = (int)jpanel.getLocationOnScreen().getY();
		//On verifie que l'affichage ne sera pas hors ecran
		int diff = (this.xLastSelMessage + StatusMessageGUI.width) - (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		if(diff > 0)
			this.xLastSelMessage -= diff;
		for(JPanel jpan : jpanels){
			if(jpan.equals(jpanel))
				break;
			cpt++;
		}
		this.progPrincipal.chargerStatusMessage(cpt);
     }
}
