/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Serveur.GestionBDD;

/**
 *
 * @author winuser
 */
public class CreerGroupeGUI extends javax.swing.JFrame {

	private DefaultListModel<String> groupeUsers0 = new DefaultListModel<>();
	private DefaultListModel<String> groupeUsers1 = new DefaultListModel<>();
	private DefaultListModel<String> utilisateursType0 = new DefaultListModel<>();
	private DefaultListModel<String> utilisateursType1 = new DefaultListModel<>();
	private List<List<String>> utilisateurs0;
	private List<List<String>> utilisateurs1;
	private String[] tempUserUtilisateurs;
	
	private GestionBDD gestionBDD;
	private int type;
	private String oldName;
	private GestionUtilisateurGUI guGUI;
	/**
	 * Creates new form CreeGroupe
	 */

	// Constructeur
	public CreerGroupeGUI(List<List<String>> utilisateurs0, List<List<String>> utilisateurs1, GestionBDD gestionBDD, GestionUtilisateurGUI guGUI) {
		this.utilisateurs0 = utilisateurs0;
		this.utilisateurs1 = utilisateurs1;
		this.gestionBDD = gestionBDD;
		this.guGUI = guGUI;
		this.type = -1;
		init(0);
	}

	public CreerGroupeGUI(String nom, String type, String utilisateurs,List<List<String>> utilisateurs0, 
			List<List<String>> utilisateurs1, GestionBDD gestionBDD, GestionUtilisateurGUI guGUI) {
		this.utilisateurs0 = utilisateurs0;
		this.utilisateurs1 = utilisateurs1;
		this.gestionBDD = gestionBDD;
		this.guGUI = guGUI;
		int index = Integer.valueOf(type);
		this.type = index;
		init(index);
		jTextField1.setText(nom);
		this.oldName = nom;
		
		this.tempUserUtilisateurs = utilisateurs.split(";");
		DefaultListModel<String> groupeUsers = groupeUsers0;
		DefaultListModel<String> utilisateursTypes = utilisateursType0;
		if (index == 1) {
			groupeUsers = groupeUsers1;
			utilisateursTypes = utilisateursType1;
		}
		for (int i = 0; i < tempUserUtilisateurs.length; i++) {
			String tamponRecherche = tempUserUtilisateurs[i];
			if (i > 0)
				tamponRecherche = tamponRecherche.substring(1);
			if (tamponRecherche != null) {
				groupeUsers.addElement(tamponRecherche);
				utilisateursTypes.removeElement(tamponRecherche);
			}
		}	
		jComboBox1.setSelectedIndex(index);
	}
	
	public void initAndSetUtilisateurs(int index){
		DefaultListModel<String> groupeUsers = groupeUsers0;
		DefaultListModel<String> utilisateursType = utilisateursType0;
		if (index == 1) {
			groupeUsers = groupeUsers1;
			utilisateursType = utilisateursType1;
		}
		jList2.setModel(groupeUsers);
		jList3.setModel(utilisateursType);
	}

	public void init(int mode) {
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
		
		jScrollPane2 = new javax.swing.JScrollPane();
		jList1 = new javax.swing.JList<>();
		jPanel1 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jComboBox1 = new javax.swing.JComboBox<>();
		jScrollPane3 = new javax.swing.JScrollPane();
		jList2 = new javax.swing.JList<>();
		jScrollPane4 = new javax.swing.JScrollPane();
		jList3 = new javax.swing.JList<>();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		jLabel2.setText("NOM :");

		jLabel3.setText("TYPE :");

		jLabel4.setText("Utilisateurs :");

		jTextField1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextField1ActionPerformed(evt);
			}
		});

		jButton1.setText("Sauvegarder");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Annuler");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "0 - Etudiant, Professeur...", "1 - Service Technique, Administratif..." }));
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jComboBox1ActionPerformed(evt);
			}
		});

		for (List<String> tempList : utilisateurs0) {
			utilisateursType0.addElement(tempList.get(3)+" ("+tempList.get(1)+" "+tempList.get(0)+")");
		}
		for (List<String> tempList : utilisateurs1) {
			utilisateursType1.addElement(tempList.get(3)+" ("+tempList.get(1)+" "+tempList.get(0)+")");
		}

		if(mode==0){
			jList2.setModel(groupeUsers0);
			jList3.setModel(utilisateursType0);
		}
		else{
			jList2.setModel(groupeUsers1);
			jList3.setModel(utilisateursType1);
		}
		jScrollPane3.setViewportView(jList2);
		jScrollPane4.setViewportView(jList3);

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

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel3).addComponent(jLabel2).addComponent(jLabel4))
						.addGap(86, 86, 86)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup().addComponent(jButton1)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 89,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(31, 31, 31))
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(jPanel1Layout.createSequentialGroup()
												.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 142,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jButton4).addComponent(jButton3))
												.addGap(18, 18, 18).addComponent(jScrollPane4,
														javax.swing.GroupLayout.PREFERRED_SIZE, 151,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap())
								.addComponent(jTextField1))));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(25, 25, 25)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(26, 26, 26)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel4).addGap(298, 298,
										298))
								.addGroup(jPanel1Layout.createSequentialGroup()
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(jPanel1Layout.createSequentialGroup().addComponent(jButton3)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(jButton4).addGap(0, 0, Short.MAX_VALUE))
										.addComponent(jScrollPane3).addComponent(jScrollPane4,
												javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
										.addGap(54, 54, 54)))
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButton2).addComponent(jButton1))
						.addContainerGap(50, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(40, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}
	
	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextField1ActionPerformed

	//Changement index de combobox
	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
		int index = jComboBox1.getSelectedIndex();
		this.initAndSetUtilisateurs(index);
	}// GEN-LAST:event_jComboBox1ActionPerformed

	//Vers droite
	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		DefaultListModel<String> groupeUsers = groupeUsers0;
		DefaultListModel<String> utilisateursType = utilisateursType0;
		int index = jComboBox1.getSelectedIndex();
		String tampon = jList2.getSelectedValue();
		if (tampon != null) {
			if (index == 1) {
				groupeUsers = groupeUsers1;
				utilisateursType = utilisateursType1;
			}
			utilisateursType.addElement(tampon);
			jList3.setModel(utilisateursType);
			groupeUsers.removeElementAt(jList2.getSelectedIndex());
			jList2.setModel(groupeUsers);
		}
	}// GEN-LAST:event_jButton3ActionPerformed

	//Vers gauche
	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
		DefaultListModel<String> groupeUsers = groupeUsers0;
		DefaultListModel<String> utilisateursType = utilisateursType0;
		int index = jComboBox1.getSelectedIndex();
		String tampon = jList3.getSelectedValue();
		if (tampon != null) {
			if (index == 1) {
				groupeUsers = groupeUsers1;
				utilisateursType = utilisateursType1;
			}
			utilisateursType.removeElementAt(jList3.getSelectedIndex());
			jList3.setModel(utilisateursType);
			groupeUsers.addElement(tampon);
			jList2.setModel(groupeUsers);
		}
	}// GEN-LAST:event_jButton4ActionPerformed

	//Bouton sauvegarder
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		String nomGroupe = jTextField1.getText();
		boolean valide = false;
		if (nomGroupe.length() != 0) {
			int index = jComboBox1.getSelectedIndex();
			/*if (jList2.getModel().getSize() == 0) {
				JOptionPane.showMessageDialog(this, "Aucun utilisateurs dans le groupe.");
			} else {*/
			if(this.type != -1){
				String requete = "NomG = \""+nomGroupe+"\", TypeG = "+index;
				this.gestionBDD.requeteUpdate("groupe",requete,"NomG = \""+this.oldName+"\"");
				valide = true;
			}else{
				List<List<String>> temp = this.gestionBDD.requeteSelect("COUNT(*)", "groupe", "NomG = \""+nomGroupe+"\"");
				if(temp.get(0).get(0).equals("0")){
					String requete = "(\""+nomGroupe+"\","+index+")";
					this.gestionBDD.requeteInsertInto("groupe (NomG, TypeG)", requete, "");	
					valide = true;
				}else{
					JOptionPane.showMessageDialog(this, "Un groupe possede deja ce nom.");
				}
			}
			if(valide){
				this.gestionBDD.requete("DELETE FROM appartient WHERE NomG = \""+nomGroupe+"\"", 1);
				for (int i = 0; i < jList2.getModel().getSize(); i++) {
					String infosUtilisateur = jList2.getModel().getElementAt(i);
					String[] tampon = infosUtilisateur.split(" ");
					String idUtilisateur = tampon[0];
					this.gestionBDD.requeteInsertInto("appartient (IdU, NomG)", "(\""+idUtilisateur+"\", \""+nomGroupe+"\")", "");
				}
				//JOptionPane.showMessageDialog(this, "Vous venez de creer/modifier un groupe.");
				this.guGUI.actualiserGroupes();
				dispose();	
			}
			//}
		} else {
			JOptionPane.showMessageDialog(this, "Vous n'avez pas mis de nom au groupe.");
		}
	}// GEN-LAST:event_jButton1ActionPerformed

	//Bouton annuler
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		dispose();
	}// GEN-LAST:event_jButton2ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JList<String> jList1;
	private javax.swing.JList<String> jList2;
	private javax.swing.JList<String> jList3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTextField jTextField1;
	// End of variables declaration//GEN-END:variables
}
