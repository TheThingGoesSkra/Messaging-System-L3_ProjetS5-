/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.List;
import java.util.NavigableSet;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import Client.*;

//Fenetre principale.

public class ArborescenceTicketGUI extends javax.swing.JFrame {

	ProgrammePrincipal progPrincipal;
	boolean isWriting;
    /**
     * Creates new form ArborescenceTicketGUI
     */
    public ArborescenceTicketGUI(ProgrammePrincipal progPrincipal) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ArborescenceTicketGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ArborescenceTicketGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ArborescenceTicketGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ArborescenceTicketGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    	this.progPrincipal = progPrincipal;
    	this.isWriting = false;
    	this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/PaulSabatier.jpg")).getImage());
    	this.setVisible(true);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); 
        this.initJTree();
    }
    
    public void initJTree(){
    	Utilisateur tempUserP = this.progPrincipal.getUtilisateurPrincipal();
		NavigableSet<Groupe> listGroupe = tempUserP.getListeGroupes();
		System.out.println(listGroupe);
		NavigableSet<Ticket> listTicket;
		String tempNomGroupe;
		for(Groupe groupe : listGroupe){
			if(!groupe.getListeTickets().isEmpty()){
				tempNomGroupe = groupe.getNom();
				this.ajouterGroupe(tempNomGroupe);
				listTicket = groupe.getListeTickets();
				for(Ticket ticket : listTicket)
					this.ajouterTicket(tempNomGroupe,ticket,0);
					//this.ajouterTicket(tempNomGroupe, ticket.getTitre()+" ("+ticket.getNbrMessageNonLu()+")");	
			}
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
	           public void run() {
	        	   expandAll(jTree1);
	           }
		});
    }
    public void resetJTree(){
    	DefaultTreeModel model = (DefaultTreeModel)this.jTree1.getModel();
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
        rootNode.removeAllChildren();
        model.setRoot(rootNode);
    }
    
    public void actualiserJTree(Ticket ticket, int mode){
    	DefaultTreeModel model = (DefaultTreeModel)this.jTree1.getModel();
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
    	TreePath path = jTree1.getSelectionPath();
    	Enumeration enume = saveExpansionState(jTree1);
    	DefaultMutableTreeNode temp = this.ajouterTicket(ticket.getGroupe().getNom(),ticket,1);
    	loadExpansionState(jTree1, enume);
    	if(ticket.equals(this.progPrincipal.getConversationGuiTicket())){
        	if(path != null)
        		path = path.getParentPath();
        	if(path != null)
        		path = path.pathByAddingChild(temp);
    	}
    	if(mode == 0){
    		path = new TreePath(temp.getPath());
    	}
    	jTree1.setSelectionPath(path);
        /*rootNode.removeAllChildren();
        model.setRoot(rootNode);
        this.initJTree();*/
    }
    
    public DefaultMutableTreeNode ajouterGroupe(String nomGroupe){
    	DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nomGroupe);
    	DefaultTreeModel model = (DefaultTreeModel)this.jTree1.getModel();
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
    	rootNode.add(treeNode);
    	model.reload();
    	return treeNode;
    }
    
    public DefaultMutableTreeNode ajouterTicket(String nomGroupe, Ticket ticket, int mode){
		DefaultMutableTreeNode treeNode = null; 
    	DefaultTreeModel model = (DefaultTreeModel)this.jTree1.getModel();
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
    	
		//Recherche du noeud groupe
		DefaultMutableTreeNode groupNode = searchNode(rootNode,nomGroupe);
		
		//Si on ne le trouve pas on creer le groupe
		if(groupNode == null)
			groupNode = this.ajouterGroupe(nomGroupe);
		
		//Creation du noeud ticket
		treeNode = new DefaultMutableTreeNode(ticket);
		if(mode==0)
			groupNode.add(treeNode);
		else{
			DefaultMutableTreeNode temp = searchNode(groupNode,ticket);
			if(temp != null)
				groupNode.remove(temp);
			groupNode.insert(treeNode, 0);
		}
    	model.reload();	
		return treeNode;
	}
    
    public static DefaultMutableTreeNode searchNode(DefaultMutableTreeNode parentNode, Object childNode){
		DefaultMutableTreeNode temp = null; 
		Enumeration<?> children = parentNode.children();
		while (children.hasMoreElements()) {
	        DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
	        if(child.getUserObject().equals(childNode)){
	        	temp = child;
	        	break;
	        }
	    }
		return temp;
    }
    public static Enumeration saveExpansionState(JTree tree) {
        return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
    }
    public static void loadExpansionState(JTree tree, Enumeration enumeration) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                TreePath treePath = (TreePath) enumeration.nextElement();
                tree.expandPath(treePath);
            }
        }
    }
    
	/*public DefaultMutableTreeNode ajouterTicket(String nomGroupe, String nomTicket){
		DefaultMutableTreeNode treeNode = null; 
    	DefaultTreeModel model = (DefaultTreeModel)this.jTree1.getModel();
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
    	
		//Recherche du noeud groupe
		DefaultMutableTreeNode groupNode = null;
		Enumeration<?> children = rootNode.children();
		while (children.hasMoreElements()) {
	        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
	        if(childNode.getUserObject().equals(nomGroupe)){
	        	groupNode = childNode;
	        	break;
	        }
	    }
		
		//Si on ne le trouve pas on creer le groupe
		if(groupNode == null)
			groupNode = this.ajouterGroupe(nomGroupe);
		
		//Creation du noeud ticket
		treeNode = new DefaultMutableTreeNode(nomTicket);
    	groupNode.add(treeNode);
    	model.reload();	
		return treeNode;
	}*/
	
	public void expandAll(JTree tree) {
	    for (int i = 0; i < tree.getRowCount(); i++) {
	    	tree.expandRow(i);
	    }
	}
	
	public ConversationGUI ouvrirConversation(Ticket ticket){
		//Point saved = jScrollPane2.getViewport().getViewPosition();
		this.discussion2 = new ConversationGUI(this.progPrincipal,ticket);
		this.setTitreTicket(ticket.getTitre());
		this.jScrollPane2.setViewportView(discussion2);
		this.setScrollBarConversation(-1);
		//jScrollPane2.getViewport().setViewPosition( saved );
		return this.discussion2;
	}
	
	public int getScrollBarConversation(){
		return this.jScrollPane2.getVerticalScrollBar().getValue();
    }
    public void setScrollBarConversation(int value){
 	   	if(value == -1)
 	   		value = jScrollPane2.getVerticalScrollBar().getMaximum();
 	   	final int valueF = value;
		//java.awt.EventQueue.invokeLater(new Runnable() {
		       //public void run() {
		    	   jScrollPane2.getVerticalScrollBar().setValue(valueF);
		       //}
		//});
    	/*if(value == -1)
    		value = this.jScrollPane2.getVerticalScrollBar().getMaximum();
    	//int extent = this.jScrollPane2.getVerticalScrollBar().getModel().getExtent();
    	this.jScrollPane2.getVerticalScrollBar().setValue(value);*/
    }
	
	public void setTitreTicket(String text){
		this.jLabel15.setText(text);
	}
	
	public void setUserWriting(List<Utilisateur> utilisateurs){
		jLabel18.setText("");
		if(!utilisateurs.isEmpty()){
	        int cpt=0, size = utilisateurs.size();
	        for(Utilisateur u : utilisateurs){
	            cpt++;
	            if(cpt != 1){
		            if(cpt<size)
		                jLabel18.setText(jLabel18.getText()+", ");
		            else if(cpt==size)
		                jLabel18.setText(jLabel18.getText()+" et ");	
	            }
	            jLabel18.setText(jLabel18.getText()+u.getPrenom()+" "+u.getNom());
	        }
	        if(size==1)
	            jLabel18.setText(jLabel18.getText()+" est en train d'écrire...");
	        else
	            jLabel18.setText(jLabel18.getText()+" sont en train d'écrire...");
	        jLabel18.setVisible(true);
	        jLabel19.setVisible(true);	
		}else{
			jLabel18.setVisible(false);
	        jLabel19.setVisible(false);
		}
    }
	
	public void setReconnexionServeur(boolean connected){
		if(!connected){
			jLabel18.setForeground(new java.awt.Color(255, 0, 0));
			jLabel18.setText("Connexion avec le serveur perdue. Tentative de reconnexion...");
	        jLabel18.setVisible(true);
		}else{
			jLabel18.setForeground(new java.awt.Color(173, 173, 173));
			jLabel18.setText("");
			jLabel19.setVisible(false);
		}
	}
    	
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        Entete = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        discussion2 = new ConversationGUI(this.progPrincipal,null);
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTree1 = new javax.swing.JTree();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        
        jLabel18.setForeground(new java.awt.Color(173, 173, 173));
        jLabel18.setText("");
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-bulle-32.png"))); // NOI18N
        jLabel18.setVisible(false);
        jLabel19.setVisible(false);
        
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.addKeyListener(new KeyListener() {
        	//Redimensionnement du jTextArea en fonction de son contenu
        	public void setLignes(int diff){
                jPanel2.setBounds(jPanel2.getX(), jPanel2.getY()-diff, jPanel2.getWidth(), jPanel2.getHeight()+diff);
                jPanel8.setBounds(jPanel8.getX(), jPanel8.getY(), jPanel8.getWidth(), jPanel8.getHeight()-diff);
                jScrollPane2.setSize(new Dimension(jScrollPane2.getWidth(), jScrollPane2.getHeight()-diff)); 
                jScrollPane1.setSize(new Dimension(jScrollPane1.getWidth(), jScrollPane1.getHeight()+diff));
                jTextArea1.setSize(new Dimension(jTextArea1.getWidth(), jTextArea1.getHeight()+diff));
                jButton1.setSize(new Dimension(jButton1.getWidth(), jButton1.getHeight()+diff));
        	}
        	public void actualiserLignes(){
                java.awt.Font font = jTextArea1.getFont();
                java.awt.FontMetrics fontMetrics = jTextArea1.getFontMetrics(font);
                int fontHeight = fontMetrics.getHeight();
                try {
                    int height = jTextArea1.modelToView(jTextArea1.getDocument().getEndPosition().getOffset() - 1).y;
                    int line=height / fontHeight + 1;
                    if(line>lineCount){
                        if(cptLigneJTexteArea1<7){
                            int tampon=line-lineCount;
                             if(tampon>7)
                                tampon=6;
                            int diff=22*tampon;
                            cptLigneJTexteArea1=cptLigneJTexteArea1+tampon;
                            jTextArea1.setRows(jTextArea1.getRows()+(line-lineCount));
                            lineCount=line;
                            setLignes(diff);
                        }
                    }
                    if(line<lineCount && line >1){
                        int tampon=(lineCount-line);
                        int diff=22*tampon;
                        cptLigneJTexteArea1=cptLigneJTexteArea1-(lineCount-line);
                        jTextArea1.setRows(jTextArea1.getRows()-(lineCount-line));
                        lineCount=line;
                        setLignes(-diff);
                        validate();
                    }
                    if(line<lineCount && line==1 && lineCount!=2){
                        int tampon=(lineCount-line-1);
                        int diff=22*tampon;
                        cptLigneJTexteArea1=cptLigneJTexteArea1-(lineCount-line-1);
                        jTextArea1.setRows(jTextArea1.getRows()-(lineCount-line-1));
                        lineCount=2;
                        setLignes(-diff);
                        validate();
                    }
                } catch (Exception e) { 
                    lineCount = 2;
                }
        	}
            private boolean shift=false;
            private int cptLigneJTexteArea1=0;
            private int lineCount=2;
            @Override  
            public void keyPressed(KeyEvent arg0) { 
                if(arg0.getKeyCode()==KeyEvent.VK_SHIFT){
                   shift=true;
                }
                if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
                    if(shift){
                        jTextArea1.append("\n");
                    }else{
                        arg0.consume();
                        if(cptLigneJTexteArea1>0){
                            int diff=22*cptLigneJTexteArea1;
                            setLignes(-diff);
                            cptLigneJTexteArea1=0;
                            lineCount=2;
                            jTextArea1.setRows(2);
                            validate();
                        }
                        envoyerMessage();
                    }
                }
                actualiserLignes();
            }  
            @Override  
            public void keyReleased(KeyEvent arg0) {  
                if(arg0.getKeyCode()==KeyEvent.VK_SHIFT){
                   shift=false;
                }
            }  
            @Override  
            public void keyTyped(KeyEvent arg0) {   
                if(arg0.getKeyChar()==ProgrammePrincipal.delimiteur.charAt(0)){
                    arg0.consume();
                }
                if(jTextArea1.getText().equals("") && isWriting){
                	isWriting = false;
                	progPrincipal.sendUserIsWriting(isWriting);
                }
                if(!jTextArea1.getText().equals("") && !isWriting){
                	isWriting = true;
                	progPrincipal.sendUserIsWriting(isWriting);
                }
                actualiserLignes();
            } 
            
        });

        jScrollPane2.getVerticalScrollBar().setUnitIncrement(8);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Entete.setBackground(new java.awt.Color(153, 0, 51));
        Entete.setForeground(new java.awt.Color(153, 0, 0));
        Entete.setPreferredSize(new java.awt.Dimension(300, 100));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Université Paul-Sabatier");
        
        /*
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Close Window-26.png"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Maximize Window-26.png"))); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Minimize Window-26.png"))); // NOI18N
        */
        
        javax.swing.GroupLayout EnteteLayout = new javax.swing.GroupLayout(Entete);
        Entete.setLayout(EnteteLayout);
        EnteteLayout.setHorizontalGroup(
            EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnteteLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1166, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EnteteLayout.setVerticalGroup(
            EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnteteLayout.createSequentialGroup()
                .addGroup(EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EnteteLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EnteteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        getContentPane().add(Entete, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        discussion2.setPreferredSize(new Dimension(480,480));
        
        /*jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField1.setMargin(new java.awt.Insets(1, 1, 1, 1));*/
        
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextArea1.setRows(2);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setBackground(new java.awt.Color(218, 217, 217));
        jButton1.setText("Envoyer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Full Image-32.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Voicemail-32.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Checklist-32.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Collect-32.png"))); // NOI18N
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel17MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        //.addComponent(jTextField1)
                    	.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1258, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1216, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    //.addComponent(jTextField1)
                    .addComponent(jScrollPane1)
                    )
                .addContainerGap())
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Information-64.png"))); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                progPrincipal.chargerInfosTicket();
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 1282, Short.MAX_VALUE))
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 865, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel4))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                        ))
                .addContainerGap(10, Short.MAX_VALUE)
                )
        );

        jPanel8.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setToolTipText("");

        jScrollPane2.setViewportView(discussion2);

        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel8, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField2.setText("Rechercher...");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Add-32.png"))); // NOI18N
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Trash Can-32(1).png"))); // NOI18N
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Support-32.png"))); // NOI18N
        //jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/Parametre.png"))); // NOI18N
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Error-32.png"))); // NOI18N
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel13MouseExited(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Exit-32.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        /*jTree1.addTreeWillExpandListener(new TreeWillExpandListener() {	
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				
			}
			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				throw new ExpandVetoException(event);
			}
		});*/
        jTree1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("root");
        jTree1.setModel(new DefaultTreeModel(treeNode1));
        jTree1.setRootVisible(false);
        jTree1.setToggleClickCount(1);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this.jTree1.getCellRenderer();
        renderer.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/GroupeIcone.png")));
        renderer.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/GroupeIcone.png")));
        renderer.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/TicketIcone.png")));
        jPanel3.add(jTree1, java.awt.BorderLayout.CENTER);
        
        jSplitPane2.setLeftComponent(jPanel3);

        getContentPane().add(jSplitPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
       jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Add-32(1).png")));
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
       jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Add-32.png")));
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Trash Can-32(2).png")));
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Trash Can-32(1).png")));
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Support-32(1).png")));
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Support-32.png")));
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseEntered
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Error-32(1).png")));
    }//GEN-LAST:event_jLabel13MouseEntered

    private void jLabel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseExited
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Error-32.png")));
    }//GEN-LAST:event_jLabel13MouseExited

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Exit-32(1).png")));
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Exit-32.png")));
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
     jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Information-64(1).png"))); 
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
      jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Information-64.png"))); 
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
      jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Checklist-32(1).png")));
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
      jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Checklist-32.png")));
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
      jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Full Image-32(1).png")));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
      jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Full Image-32.png")));
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
      jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Voicemail-32(1).png")));
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
      jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Voicemail-32.png")));
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseEntered
      jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Collect-32(1).png")));
    }//GEN-LAST:event_jLabel17MouseEntered

    private void jLabel17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseExited
      jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/Icones/icons8-Collect-32.png")));
    }//GEN-LAST:event_jLabel17MouseExited
    
    //Bouton creer nouveau ticket
    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new NouveauTicketGUI(this.progPrincipal);
    }  
    
    //Bouton retour ecran authentification
    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {       
    	dispose();
        this.progPrincipal.retourEcranAuthentification();
    } 
    
    //Selection d'un ticket dans le jTree
    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
    	JTree tree = (JTree)evt.getSource();
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    	if(node != null && node.isLeaf()){
        	Ticket ticket = (Ticket)node.getUserObject();
        	this.progPrincipal.chargerConversation(ticket, 0);	
    	}
    	
    	/*TreePath path = evt.getPath();
    	int posTicketInGroupe = jTree1.getRowForPath(path) - jTree1.getRowForPath(path.getParentPath()) - 1; //-1 pour avoir un indice commencant a 0
        if(path.getPathCount()==3){
        	String nomGroupe = path.getPathComponent(1).toString();
            Groupe groupe = this.progPrincipal.findGroupe(nomGroupe);
            if(groupe != null){
                String nomTicket = path.getPathComponent(2).toString();
                nomTicket = nomTicket.substring(0,nomTicket.lastIndexOf('(')-1);
            	//Ticket ticket = Ticket.findTicketWithName(groupe, nomTicket);
                Ticket ticket = groupe.getTicketAtPos(posTicketInGroupe);
            	if(ticket != null)
            		this.progPrincipal.chargerConversation(ticket, 0);
            }
        }*/
    }         
    
    //Action bouton envoyer
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	this.envoyerMessage();
    }//GEN-LAST:event_jButton1ActionPerformed    
    
    private void envoyerMessage(){
    	String contenuM = this.jTextArea1.getText();
    	String contenuTemp = contenuM.replace(" ", ""); //On verifie que le message ne contient pas que des espaces
    	String contenuTemp2 = contenuM.replace("\n", ""); //On verifie que le message ne contient pas que des sauts de ligne
    	if(this.progPrincipal.isTicketLoaded() && !contenuTemp.equals("") && !contenuTemp2.equals("")){
    		/*System.out.println("---");
        	System.out.println(contenuM);
        	System.out.println("---");*/
    		this.jTextArea1.setText("");
    		this.progPrincipal.envoyerMessage(contenuM, this.progPrincipal.getConversationGuiTicket(), 1);
    	}
    }


    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
           public void run() {
                new ArborescenceTicketGUI().setVisible(true);
           }
       });*/
                
    //}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Entete;
    private ConversationGUI discussion2;
    private javax.swing.JButton jButton1; //Bouton envoyer
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15; //Titre ticket
    private javax.swing.JLabel jLabel16; //Description
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTree jTree1;
    //Utilisateur en train d'ecrire label:
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    // End of variables declaration//GEN-END:variables
}
