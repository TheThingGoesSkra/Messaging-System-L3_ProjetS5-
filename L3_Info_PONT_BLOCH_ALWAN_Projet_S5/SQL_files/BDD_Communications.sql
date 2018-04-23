#0 Authentification
#1 Creer ticket/Nouveau message
#2 Demander le chargement d'un ticket
#3 Demander le status d'un message
#4 Utilisateur en train d'écrire
#5 Changement de couleur d'un message
--------------------------------------------------------------------------------
Communication 0 #Authentification
    Client:  "0_login_password"
    Serveur: "0_reponse_utilisateurPrincipal_groupes_utilisateurs_tickets_messages"
        valider login et pass:
            SELECT COUNT(*) FROM utilisateur WHERE IdU = <login> and PasswordU = <password>
            #si la requete renvoit 1 alors l'authentification est reussie
        utilisateurPrincipal:
            SELECT IdU, NomU, PrenomU, TypeU FROM utilisateur WHERE IdU = <login>
        groupes:             
            SELECT TG.NomG, TG.TypeG FROM groupe TG, appartient TA WHERE TA.IdU = <iduUtilisateurPrincipal> and TA.NomG = TG.NomG
            SELECT NomG, TypeG FROM groupe WHERE TypeG != <typeUtilisateurPrincipal>
        ->pour chaque groupe
          _utilisateurs:
            SELECT TU.IdU, TU.NomU, TU.PrenomU, TU.TypeU FROM utilisateur TU, appartient TA WHERE TA.NomG = <nomGroupe> and TA.IdU = TU.IdU
          _tickets:
            SELECT IdT, TitreT, IdU, DateT FROM ticket WHERE NomG = <nomGroupe>
            #pour les groupes du type opposé:
            SELECT IdT, TitreT, IdU, DateT FROM ticket WHERE NomG = <nomGroupe> and IdU = <iduUtilisateurPrincipal>
            #pour connaitre le nombre de message non lu
            SELECT COUNT(TS.StatusM) FROM statusm TS, message TM WHERE TS.IdU = <iduUtilisateurPrincipal> and TM.IdT = <idTicket> and TM.IdM = TS.IdM and StatusM != 2
            #pour tous: on rajoute a la fin le nombre de message non lu: 0 (pour le moment)
            #mise a jour du status des messages
            UPDATE statusm TS, message TM SET TS.StatusM = 1 WHERE TS.IdU = <idUtilisateur> and TM.IdT = <idTicket> and TS.IdM = TM.IdM and TS.StatusM < 1
          ->pour chaque ticket
            _message:
            SELECT IdM, ContenuM, IdU, CouleurM, DateM FROM message WHERE IdT = <idTicket> ORDER BY DateM DESC LIMIT 1
            
    recoit:
0_login_password
    renvoie:
0_reponse_IdU_NomU_PrenomU_TypeU_[NomG_TypeG_NBUSER_[IdU_NomU_PrenomU_TypeU]_NBTICKET_[IdT_TitreT_IdU_DateT_NbrMessNonLu_IdM_ContenuM_IdU_CouleurM_DateM]]
--------------------------------------------------------------------------------
Communication 1 #Creer ticket/Nouveau message
    String nomGroupe, List<String> ticket, List<String> message, int mode
    Client:  "1_0_nomGroupe_nomTicket_message" (0)
             "1_1_nomGroupe_idTicket_message"  (1)  
             "1_2_idMessage"                   (2)
    Serveur: "1_0_nomGroupe_date_IdU_ticket_message"    
             "1_1_nomGroupe_date_IdU_idTicket_message" 
        _pour (0):
            SELECT max(IdT) FROM ticket
            INSERT INTO ticket (IdT, TitreT, DateT, NomG, IdU) VALUES (<maxIdT+1>,<nomTicket>,<dateTicket>,<nomGroupe>,<idUtilisateur>);
        _pour (0) et (1):
            SELECT max(IdM) FROM message
            INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (<maxIdM+1>,<contenuMessage>,1,<dateMessage>,<idTicket>,<idUtilisateur>);
            INSERT INTO statusm (IdU, IdM, StatusM) VALUES (<idUtilisateurOrigine>,<maxIdM+1>,2), [(<idUtilisateurConcerne>,<maxIdM+1>,0)];
        _pour (2):
            UPDATE statusm SET StatusM = 1 WHERE IdU = <idUtilisateur> and IdM = <idMessage> and StatusM < 1

        _pour savoir les utilisateurs concernés:
            SELECT IdU FROM appartient WHERE NomG = <nomGroupe> and IdU != <idUtlisateur>
            SELECT IdU FROM ticket WHERE IdT = <idTicket> and IdU != <idUtilisateur>
      
    recoit:
1_0_nomGroupe_nomTicket_ContenuM
1_1_nomGroupe_idTicket_ContenuM
1_2_nomGroupe_idTicket_idMessage #un utilisateur nous dit qu'il a bien recu le message
1_3_nomGroupe_idTicket_idMessage #un utilisateur nous dit qu'il a bien vu le message
    renvoie:
1_0_nomGroupe_date_IdU_idTicket_nomTicket_IdM_ContenuM_CouleurM
1_1_nomGroupe_date_IdU_idTicket_IdM_ContenuM_CouleurM
#on dit a l'utilisateur a l'origine du message qu'il a ete recu par le serveur
1_2_nomGroupe_idTicket_idMessage_date #nouveau ticket
1_3_nomGroupe_idTicket_idMessage_date #nouveau message
--------------------------------------------------------------------------------
Communication 2 #Demander le chargement d'un ticket
    String nomGroupe, String idTicket, List<List<String>> messages
    Client:  "2_nomGroupe_idTicket_indice"
    Serveur: "2_nomGroupe_idTicket_messages"
    SELECT IdM, ContenuM, DateM, IdU, CouleurM FROM message WHERE IdT = <idTicket> ORDER BY DateM DESC LIMIT 30
    UPDATE statusm TS, message TM SET TS.StatusM = 2 WHERE TS.IdU = <idUtilisateur> and TM.IdT = <idTicket> and TS.IdM = TM.IdM and TS.StatusM < 2
    
    recoit:
2_nomGroupe_idTicket_indice    
    renvoie:
2_nomGroupe_idTicket_[IdM_ContenuM_IdU_CouleurM_DateM]
--------------------------------------------------------------------------------
Communication 3 #Demander le status d'un message
    String nomGroupe, String idTicket, String idMessage, List<List<String>> status
    Client:  "3_nomGroupe_idTicket_idMessage"
    Serveur: "3_nomGroupe_idTicket_idMessage_(utilisateur;status)+"
    SELECT IdU, StatusM FROM statusm WHERE IdM = <idMessage> and IdU != <idUtilisateur>

    recoit
3_nomGroupe_idTicket_idMessage
    renvoie:
3_nomGroupe_idTicket_idMessage_[IdU_Status]
--------------------------------------------------------------------------------
Communication 4 #Utilisateur en train d'écrire
    Client:  "4_booleen_nomGroupe_idTicket"
    Serveur: "4_utilisateur_booleen_nomGroupe_idTicket"
   
    recoit
4_nomGroupe_idTicket_booleen
    renvoie:
4_nomGroupe_idTicket_IdU_booleen
--------------------------------------------------------------------------------
Communication 5 #Changement de couleur d'un message
    String nomGroupe, String idTicket, String idMessage, String couleurM
    Serveur: "5_nomGroupe_ticket_message"

    renvoie:
5_nomGroupe_idTicket_idMessage_CouleurM
--------------------------------------------------------------------------------
Application Serveur
    #chercher les informations des utilisateurs
        SELECT IdU, NomU, PrenomU, MailU, PasswordU, TypeU FROM utilisateur
        SELECT TG.NomG, TG.TypeG FROM groupe TG, appartient TA WHERE TA.IdU = <idUtilisateur> and TA.NomG = TG.NomG
    #modifier un utilisateur:
        UPDATE utilisateur SET IdU = <>, NomU = <>, PrenomU = <>, MailU = <>, PasswordU = <>, TypeU = <> WHERE IdU = <oldIdUtilisateur>
        
    #chercher les informations des groupes
        SELECT NomG, TypeG FROM groupe
        SELECT TU.IdU, TU.PrenomU, TU.NomU FROM appartient TA, utilisateur TU WHERE TA.NomG = <nomGroupe> and TA.IdU = TU.IdU
    #modifier un groupe:
        UPDATE groupe SET NomG = <nomGroupe>, TypeG = <> WHERE NomG = <oldNomGroupe>
        
    #Pour lier ou delier des utilisateurs et des groupes
        INSERT INTO appartient (IdU, NomG) VALUES (<>, <>)
        DELETE FROM appartient WHERE IdU = <> and NomG = <>