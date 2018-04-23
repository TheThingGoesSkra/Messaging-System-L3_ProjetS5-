/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import Serveur.GestionBDD;

/**
 *
 * @author winuser
 */
public class CreerUtilisateurGUI extends javax.swing.JFrame {

	private DefaultListModel<String> userGroupes0 = new DefaultListModel<>();
	private DefaultListModel<String> userGroupes1 = new DefaultListModel<>();
	private DefaultListModel<String> groupesType0 = new DefaultListModel<>();
	private DefaultListModel<String> groupesType1 = new DefaultListModel<>();
	private List<List<String>> groupes0;
	private List<List<String>> groupes1;
	private String[] tempUserGroupes;
	
	private GestionBDD gestionBDD;
	private int type;
	private String oldId;
	private GestionUtilisateurGUI guGUI;

	/**
	 * Creates new form Creeutilisateur
	 */

	// Constructeur
	public CreerUtilisateurGUI(List<List<String>> groupes0, List<List<String>> groupes1, GestionBDD gestionBDD, GestionUtilisateurGUI guGUI) {
		this.groupes0 = groupes0;
		this.groupes1 = groupes1;
		this.type = -1;
		this.gestionBDD = gestionBDD;
		this.guGUI = guGUI;
		init(0);
	}

	public CreerUtilisateurGUI(String nom, String prenom, String mail, String id, String mdp, String type,String groupes,
			List<List<String>> groupes0, List<List<String>> groupes1, GestionBDD gestionBDD, GestionUtilisateurGUI guGUI) {
		this.groupes0=groupes0;
		this.groupes1=groupes1;
		this.gestionBDD = gestionBDD;
		this.guGUI = guGUI;
		int index = Integer.valueOf(type);
		this.type = index;
		init(index);
		jTextField1.setText(nom);
		jTextField2.setText(prenom);
		jTextField3.setText(mail);
		jPasswordField1.setText(mdp);
		jPasswordField2.setText(mdp);
		jTextField6.setText(id);
		this.oldId = id;
					
		tempUserGroupes = groupes.split(";");
		DefaultListModel<String> userGroupes = userGroupes0;
		DefaultListModel<String> groupesType = groupesType0;
		if (index == 1) {
			userGroupes = userGroupes1;
			groupesType = groupesType1;
		}
		for (int i = 0; i < tempUserGroupes.length; i++) {
			String tamponRecherche = tempUserGroupes[i];
			if (i > 0)
				tamponRecherche = tamponRecherche.substring(1);
			if (tamponRecherche != null) {
				userGroupes.addElement(tamponRecherche);
				groupesType.removeElement(tamponRecherche);
			}
		}	
		jComboBox1.setSelectedIndex(index); //Appel l'action listener du jComboBox qui initialise les groupes et les listes
	}
	
	public void initAndSetGroupes(int index){
		DefaultListModel<String> userGroupes = userGroupes0;
		DefaultListModel<String> groupesType = groupesType0;
		if (index == 1) {
			userGroupes = userGroupes1;
			groupesType = groupesType1;
		}
		jList2.setModel(userGroupes);
		jList3.setModel(groupesType);
	}

	private void init(int type) {
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
			java.util.logging.Logger.getLogger(CreerUtilisateurGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(CreerUtilisateurGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(CreerUtilisateurGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CreerUtilisateurGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>
		this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/Serveur/Icones/PaulSabatier.jpg")).getImage());
		
		jPanel1 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jTextField3 = new javax.swing.JTextField();
		jPasswordField1 = new javax.swing.JPasswordField();
		jPasswordField2 = new javax.swing.JPasswordField();
		jTextField6 = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jComboBox1 = new javax.swing.JComboBox<>();
		jScrollPane2 = new javax.swing.JScrollPane();
		jList2 = new javax.swing.JList<>();
		jScrollPane3 = new javax.swing.JScrollPane();
		jList3 = new javax.swing.JList<>();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jLabel6 = new javax.swing.JLabel();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		jLabel2.setText("NOM :");

		jLabel3.setText("PRENOM :");

		jLabel4.setText("Mail :");

		jLabel8.setText("MOT DE PASSE :");

		jLabel9.setText("CONFIRMER MOT DE PASSE :");

		jLabel10.setText("ID :");

		jTextField1.setText("");
		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		jTextField2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField2ActionPerformed(evt);
			}
		});

		jButton1.setText("Sauvegarder");

		jButton2.setText("Annuler");

		jLabel1.setText("TYPE :");

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "0 - Etudiant, Professeur...", "1 - Service Technique, Administratif..." }));
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1ActionPerformed(evt);
			}
		});

		for (List<String> tempList : groupes0) {
			groupesType0.addElement(tempList.get(0));
		}
		for (List<String> tempList : groupes1) {
			groupesType1.addElement(tempList.get(0));
		}

		if(type==0){
			jList2.setModel(userGroupes0);
			jList3.setModel(groupesType0);
		}
		else{
			jList2.setModel(userGroupes1);
			jList3.setModel(groupesType1);
		}
		jScrollPane2.setViewportView(jList2);
		jScrollPane3.setViewportView(jList3);

		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setText(">");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton4.setText("<");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jLabel6.setText("Appartient aux groupes :");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30)
						.addGroup(jPanel1Layout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										jPanel1Layout.createSequentialGroup()
												.addGroup(
														jPanel1Layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jLabel3).addComponent(jLabel2)
																.addComponent(jLabel4).addComponent(jLabel10)
																.addComponent(jLabel9).addComponent(jLabel8)
																.addComponent(jLabel1))
												.addGap(18, 18,
														18)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
												jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(jPasswordField1,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jTextField6,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jTextField3,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jTextField2)
														.addComponent(jTextField1,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jPasswordField2,
																javax.swing.GroupLayout.Alignment.LEADING))
												.addGap(31, 31, 31))
										.addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGroup(jPanel1Layout.createSequentialGroup().addGap(3, 3, 3)
														.addGroup(jPanel1Layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
																.addGroup(jPanel1Layout.createSequentialGroup()
																		.addComponent(jButton1).addGap(31, 31, 31)
																		.addComponent(jButton2,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				89,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
																.addGroup(jPanel1Layout.createSequentialGroup()
																		.addGroup(jPanel1Layout
																				.createParallelGroup(
																						javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(jLabel6)
																				.addComponent(jScrollPane2,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						142,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGap(18, 18, 18)
																		.addGroup(jPanel1Layout
																				.createParallelGroup(
																						javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(jButton4)
																				.addComponent(jButton3))
																		.addGap(18, 18, 18).addComponent(jScrollPane3,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				151,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))))
												.addContainerGap(39, Short.MAX_VALUE))))
						.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel5).addGap(0, 0,
								Short.MAX_VALUE)))));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(25, 25, 25)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel4).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel10).addComponent(jTextField6,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel8).addComponent(jPasswordField1,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9).addComponent(jPasswordField2,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(29, 29, 29)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(
								jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(
												jPanel1Layout.createSequentialGroup().addGap(29, 29, 29)
														.addGroup(jPanel1Layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.BASELINE)
																.addComponent(jLabel5).addComponent(jLabel6)))
								.addGroup(jPanel1Layout.createSequentialGroup().addGap(63, 63, 63)
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jButton3)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jButton4).addGap(0, 97, Short.MAX_VALUE))
												.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
														Short.MAX_VALUE)
												.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
														Short.MAX_VALUE))))
						.addGap(29, 29, 29)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButton1).addComponent(jButton2))
						.addGap(27, 27, 27)));

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>

	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField1ActionPerformed

	private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField2ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField2ActionPerformed

	//Changement d'index de combobox
	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
		int index = jComboBox1.getSelectedIndex();
		this.initAndSetGroupes(index);
	}// GEN-LAST:event_jComboBox1ActionPerformed

	//Vers droite
	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		DefaultListModel<String> userGroupes = userGroupes0;
		DefaultListModel<String> groupesType = groupesType0;
		int index = jComboBox1.getSelectedIndex();
		String tampon = jList2.getSelectedValue();
		if (tampon != null) {
			if (index == 1) {
				userGroupes = userGroupes1;
				groupesType = groupesType1;
			}
			groupesType.addElement(tampon);
			jList3.setModel(groupesType);
			userGroupes.removeElementAt(jList2.getSelectedIndex());
			jList2.setModel(userGroupes);
		}
	}// GEN-LAST:event_jButton3ActionPerformed

	//Vers gauche
	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
		DefaultListModel<String> userGroupes = userGroupes0;
		DefaultListModel<String> groupesType = groupesType0;
		int index = jComboBox1.getSelectedIndex();
		String tampon = jList3.getSelectedValue();
		if (tampon != null) {
			if (index == 1) {
				userGroupes = userGroupes1;
				groupesType = groupesType1;
			}
			groupesType.removeElementAt(jList3.getSelectedIndex());
			jList3.setModel(groupesType);
			userGroupes.addElement(tampon);
			jList2.setModel(userGroupes);
		}
	}// GEN-LAST:event_jButton4ActionPerformed

	//Sauvegarder
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		boolean valide = false;
		String nom = jTextField1.getText();
		String prenom = jTextField2.getText();
		String mail = jTextField3.getText();
		String iD = jTextField6.getText();
		char[] pass = jPasswordField1.getPassword();
		String passWord = String.copyValueOf(pass);
		char[] confirmationPass = jPasswordField2.getPassword();
		String confirmationPassWord = String.copyValueOf(confirmationPass);
		int type = jComboBox1.getSelectedIndex();
		if (nom.length() != 0 && prenom.length() != 0 && mail.length() != 0 && iD.length() != 0
				&& passWord.length() != 0) {
			if (confirmationPassWord.equals(passWord)) {
				if(this.type != -1){
					String requete = "IdU = \""+iD+"\", NomU = \""+nom+"\", PrenomU = \""+prenom;
					requete += "\", TypeU = "+type+", PasswordU = \""+passWord+"\", MailU = \""+mail+"\"";
					this.gestionBDD.requeteUpdate("utilisateur",requete,"IdU = \""+this.oldId+"\"");
					valide = true;
				}else{
					List<List<String>> temp = this.gestionBDD.requeteSelect("COUNT(*)", "utilisateur", "IdU = \""+iD+"\"");
					if(temp.get(0).get(0).equals("0")){
						String requete = "(\""+iD+"\",\""+nom+"\",\""+prenom+"\","+type+",\""+passWord+"\",\""+mail+"\",\"\",0)";
						this.gestionBDD.requeteInsertInto("utilisateur (IdU, NomU, PrenomU, TypeU, PasswordU, MailU, ListeIpU, BloqueU)", requete, "");
						valide = true;
					}else{
						JOptionPane.showMessageDialog(this, "Un utilisateur possede deja cet identifiant (ID).");
					}
				}
				if(valide){
					this.gestionBDD.requete("DELETE FROM appartient WHERE IdU = \""+iD+"\"", 1);
					for (int i = 0; i < jList2.getModel().getSize(); i++) {
						String nomGroupe = jList2.getModel().getElementAt(i);
						this.gestionBDD.requeteInsertInto("appartient (IdU, NomG)", "(\""+iD+"\", \""+nomGroupe+"\")", "");
					}
					//JOptionPane.showMessageDialog(this, "Vous venez de creer/modifier un utilisateur.");
					this.guGUI.actualiserUtilisateurs();
					dispose();	
				}
			} else {
				JOptionPane.showMessageDialog(this, "Echec de confirmation du mot de passe.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vous n'avez pas rempli tous les champs.");
		}
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField3ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField3ActionPerformed

	//Annuler
	private void jButton2ActionPerformed(ActionEvent evt) {
		dispose();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JList<String> jList2;
	private javax.swing.JList<String> jList3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPasswordField jPasswordField1;
	private javax.swing.JPasswordField jPasswordField2;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JTextField jTextField6;
	// End of variables declaration//GEN-END:variables

}
