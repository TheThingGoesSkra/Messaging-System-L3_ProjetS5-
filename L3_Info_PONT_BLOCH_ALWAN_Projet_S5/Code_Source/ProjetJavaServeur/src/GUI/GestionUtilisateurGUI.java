/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Container;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import Serveur.GestionBDD;

/**
 *
 * @author winuser
 */
public class GestionUtilisateurGUI extends javax.swing.JFrame {

	private Map<List<String>, List<List<String>>> utilisateurs;
	private Map<List<String>, List<List<String>>> groupes;
	private List<List<String>> groupesType0;
	private List<List<String>> groupesType1;
	private List<List<String>> utilisateursType0;
	private List<List<String>> utilisateursType1;
	private GestionBDD gestionBDD;

	/**
	 * Creates new form GestionUtilisateur
	 */
	@SuppressWarnings("empty-statement")
	public GestionUtilisateurGUI(GestionBDD gestionBDD) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GestionUtilisateurGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GestionUtilisateurGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GestionUtilisateurGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GestionUtilisateurGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
		this.setVisible(true);
		this.gestionBDD = gestionBDD;
		this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Serveur/Icones/PaulSabatier.jpg")).getImage());

		//Chargement de tous les utilisateurs
		this.setUtilisateurs();

		//Chargement de tous les groupes
		this.setGroupes();
		
		//Chargement des groupes de type 0 et 1
		groupesType0 = new ArrayList<>();
		groupesType1 = new ArrayList<>();
		for(Map.Entry<List<String>, List<List<String>>> entry : groupes.entrySet()){
			List<String> groupe = entry.getKey();
			String typeGroupe = groupe.get(1);
			if(typeGroupe.equals("0"))
				groupesType0.add(groupe);
			else
				groupesType1.add(groupe);
		}
		
		//Chargement des utilisateurs de type 0 et 1
		utilisateursType0 = new ArrayList<>();
		utilisateursType1 = new ArrayList<>();
		for(Map.Entry<List<String>, List<List<String>>> entry : utilisateurs.entrySet()){
			List<String> utilisateur = entry.getKey();
			String typeUtilisateur = utilisateur.get(5);
			if(typeUtilisateur.equals("0"))
				utilisateursType0.add(utilisateur);
			else
				utilisateursType1.add(utilisateur);
		}
		
		init();
		// serveur();
	}
	
	public void setUtilisateurs(){
		utilisateurs = new HashMap<>();
		List<List<String>> tempU = this.gestionBDD.requeteSelect("NomU, PrenomU, MailU, IdU, PasswordU, TypeU",
				"utilisateur", "");
		for (List<String> tempListe : tempU) {
			String tempIdU = tempListe.get(3);
			List<List<String>> tempG = this.gestionBDD.requeteSelect("TG.NomG", "groupe TG, appartient TA",
					"TA.IdU = \"" + tempIdU + "\" and TA.NomG = TG.NomG");
			utilisateurs.put(tempListe, tempG);
		}
	}
	public void setTableUtilisateurs(){
		Object[][] table = new Object[utilisateurs.size()][7];
		int cpt = 0;
		for (Map.Entry<List<String>, List<List<String>>> entry : utilisateurs.entrySet()) {
			List<String> tampon = entry.getKey();
			table[cpt][0] = tampon.get(0);
			table[cpt][1] = tampon.get(1);
			table[cpt][2] = tampon.get(2);
			table[cpt][3] = tampon.get(3);
			table[cpt][4] = tampon.get(4);
			table[cpt][5] = tampon.get(5);
			table[cpt][6] = "";
			List<List<String>> tamponGroupes = entry.getValue();
			for (Iterator<List<String>> I = tamponGroupes.iterator(); I.hasNext();) {
				List<String> tamponGroupe = I.next();
				table[cpt][6] += tamponGroupe.get(0);
				if (I.hasNext())
					table[cpt][6] += "; ";
			}
			cpt++;
		}

		jTable2.setModel(new javax.swing.table.DefaultTableModel(table,
				new String[] { "NOM", "PRENOM", "MAIL", "ID", "MOT DE PASSE", "TYPE", "GROUPES" }));
	}
	public void actualiserUtilisateurs(){
		this.setUtilisateurs();
		this.setTableUtilisateurs();
	}
	
	public void setGroupes(){
		groupes = new HashMap<>();
		List<List<String>> tempG = this.gestionBDD.requeteSelect("NomG, TypeG", "groupe", "");
		List<List<String>> tempU;
		for (List<String> tempListe : tempG) {
			String tempNomG = tempListe.get(0);
			tempU = this.gestionBDD.requeteSelect("TU.IdU, TU.PrenomU, TU.NomU", "appartient TA, utilisateur TU",
					"TA.NomG = \"" + tempNomG + "\" and TA.IdU = TU.IdU");
			groupes.put(tempListe, tempU);
		}
	}
	public void setTableGroupes(){
		int cpt = 0;
		Object[][] table = new Object[groupes.size()][3];
		for (Map.Entry<List<String>, List<List<String>>> entry : groupes.entrySet()) {
			List<String> tampon = entry.getKey();
			table[cpt][0] = tampon.get(0);
			table[cpt][1] = tampon.get(1);
			table[cpt][2] = "";
			List<List<String>> tempUsers = entry.getValue();
			int tempCpt = 0;
			int size = tempUsers.size();
			for (List<String> tempUser : tempUsers) {
				table[cpt][2] += tempUser.get(0) + " (" + tempUser.get(1) + " " + tempUser.get(2) + ")";
				if (tempCpt < size - 1)
					table[cpt][2] += "; ";
				tempCpt++;
			}
			cpt++;
		}

		jTable3.setModel(
				new javax.swing.table.DefaultTableModel(table, new String[] { "NOM", "TYPE", "Utilisateurs" }));
	}
	public void actualiserGroupes(){
		this.setGroupes();
		this.setTableGroupes();
	}

	private void init() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		Entete = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTable2 = new javax.swing.JTable();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton2 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jButton5 = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		jTable3 = new javax.swing.JTable();
		jLabel3 = new javax.swing.JLabel();
		jTextField2 = new javax.swing.JTextField();
		jButton6 = new javax.swing.JButton();
		jButton7 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		Entete.setBackground(new java.awt.Color(153, 0, 51));
		Entete.setForeground(new java.awt.Color(153, 0, 0));
		Entete.setPreferredSize(new java.awt.Dimension(300, 100));

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setText("Université Paul-Sabatier");

		javax.swing.GroupLayout EnteteLayout = new javax.swing.GroupLayout(Entete);
		Entete.setLayout(EnteteLayout);
		EnteteLayout.setHorizontalGroup(EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(EnteteLayout
						.createSequentialGroup().addGap(34, 34, 34).addComponent(jLabel1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(338, Short.MAX_VALUE)));
		EnteteLayout.setVerticalGroup(EnteteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(EnteteLayout
						.createSequentialGroup().addGap(33, 33, 33).addComponent(jLabel1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(37, Short.MAX_VALUE)));

		getContentPane().add(Entete, java.awt.BorderLayout.PAGE_START);

		jButton1.setText("Creer utilisateur");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		this.setTableUtilisateurs();
		jScrollPane2.setViewportView(jTable2);
		jTable2.setDefaultEditor(Object.class, null);
		jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jLabel2.setText("Liste d'utilisateurs :");

		jTextField1.setText("Rechercher..");
		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		jButton2.setText("Modifier");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton4.setText("Supprimer");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jButton2)
												.addGap(45, 45, 45).addComponent(jButton4))
								.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jButton1)
						.addGap(18, 18, 18)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(19, 19, 19)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButton4).addComponent(jButton2))
						.addContainerGap()));

		jTabbedPane1.addTab("Gestion Utilisateur", jPanel1);

		jButton5.setText("Creer groupe");
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton5ActionPerformed(evt);
			}
		});

		this.setTableGroupes();
		jScrollPane3.setViewportView(jTable3);

		jTable3.setDefaultEditor(Object.class, null);
		jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jLabel3.setText("Liste des groupes : ");

		jTextField2.setText("Rechercher..");
		jTextField2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField2ActionPerformed(evt);
			}
		});

		jButton6.setText("Modifier");
		jButton6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton6ActionPerformed(evt);
			}
		});

		jButton7.setText("Supprimer");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
						.addGroup(jPanel4Layout.createSequentialGroup()
								.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 165,
												javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(jPanel4Layout.createSequentialGroup().addComponent(jButton6)
										.addGap(45, 45, 45).addComponent(jButton7))
								.addGroup(jPanel4Layout.createSequentialGroup()
										.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 124,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jButton5)
						.addGap(18, 18, 18)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(19, 19, 19)
						.addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButton6).addComponent(jButton7))
						.addContainerGap()));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel4,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel4,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jTabbedPane1.addTab("Gestion Groupe", jPanel2);

		getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

		pack();
	}//

	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField1ActionPerformed

	private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField2ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField2ActionPerformed
	
	//Creer utilisateur
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		CreerUtilisateurGUI optionPane = new CreerUtilisateurGUI(groupesType0,groupesType1,this.gestionBDD,this);
		optionPane.setAlwaysOnTop(true);
		optionPane.setVisible(true);
	}// GEN-LAST:event_jButton1ActionPerformed

	//Modifier utilisateur
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		int ligneSelectionne = jTable2.getSelectedRow();
		if (ligneSelectionne >= 0) {
			String nom = (String) jTable2.getValueAt(ligneSelectionne, 0);
			String prenom = (String) jTable2.getValueAt(ligneSelectionne, 1);
			String mail = (String) jTable2.getValueAt(ligneSelectionne, 2);
			String mdp = (String) jTable2.getValueAt(ligneSelectionne, 3);
			String id = (String) jTable2.getValueAt(ligneSelectionne, 4);
			String type = (String) jTable2.getValueAt(ligneSelectionne, 5);
			String groupe = (String) jTable2.getValueAt(ligneSelectionne, 6);
			JFrame modifUser = new CreerUtilisateurGUI(nom, prenom, mail, mdp, id, type, groupe,groupesType0, groupesType1,this.gestionBDD,this);
			modifUser.setAlwaysOnTop(true);
			modifUser.setVisible(true);
		}
	}// GEN-LAST:event_jButton2ActionPerformed
	
	//Creer groupe
	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
		CreerGroupeGUI optionPane = new CreerGroupeGUI(utilisateursType0, utilisateursType1,this.gestionBDD,this);
		optionPane.setAlwaysOnTop(true);
		optionPane.setVisible(true);
	}// GEN-LAST:event_jButton5ActionPerformed

	//Modifier groupe 
	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
		int ligneSelectionne = jTable3.getSelectedRow();
		if(ligneSelectionne>=0){ 
			String nom = (String)jTable3.getValueAt(ligneSelectionne, 0); 
			String type = (String)jTable3.getValueAt(ligneSelectionne, 1);
			String utilisateurs = (String) jTable3.getValueAt(ligneSelectionne, 2);
			JFrame createUser = new CreerGroupeGUI(nom,type,utilisateurs,utilisateursType0,utilisateursType1,this.gestionBDD,this);
			createUser.setAlwaysOnTop(true);
			createUser.setVisible(true);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel Entete;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JButton jButton7;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTable jTable1;
	private javax.swing.JTable jTable2;
	private javax.swing.JTable jTable3;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	// End of variables declaration//GEN-END:variables

}
