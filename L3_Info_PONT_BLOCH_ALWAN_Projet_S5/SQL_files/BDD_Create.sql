CREATE TABLE Ticket (
    IdT int(32) NOT NULL,
    TitreT varchar(64) NOT NULL,
    DateT timestamp NOT NULL,
    NomG varchar(64) NOT NULL,
    IdU varchar(64) NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY(IdT),
    CONSTRAINT fk_ticket_utilisateur FOREIGN KEY(IdU)
        REFERENCES Utilisateur(IdU)
);

CREATE TABLE Groupe (
    NomG varchar(64) NOT NULL,
    TypeG varchar(64) NOT NULL,
    CONSTRAINT pk_groupe PRIMARY KEY(NomG)
);

CREATE TABLE Message (
    IdM int(32) NOT NULL,
    ContenuM varchar(8000) NOT NULL,
    CouleurM int(4) NOT NULL,
    DateM timestamp NOT NULL,
    IdT int(32) NOT NULL,
    IdU varchar(64) NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY(IdM),
    CONSTRAINT fk_message_ticket FOREIGN KEY(IdT)
        REFERENCES ticket(IdT),
    CONSTRAINT fk_message_utilisateur FOREIGN KEY(IdU)
        REFERENCES Utilisateur(IdU)
);

CREATE TABLE Appartient (
    IdU varchar(64) NOT NULL,
    NomG varchar(64) NOT NULL,
    CONSTRAINT pk_appartient PRIMARY KEY(IdU, NomG),
    CONSTRAINT fk_appartient_utilisateur FOREIGN KEY(IdU)
        REFERENCES Utilisateur(IdU),    
    CONSTRAINT fk_appartient_groupe FOREIGN KEY(NomG)
        REFERENCES Groupe(NomG)
);

CREATE TABLE StatusM (
    IdU varchar(64) NOT NULL,
    IdM int(32) NOT NULL,
    StatusM int(4) NOT NULL,
    CONSTRAINT pk_statusm PRIMARY KEY(IdU, IdM),
    CONSTRAINT fk_statusm_utilisateur FOREIGN KEY(IdU)
        REFERENCES Utilisateur(IdU),    
    CONSTRAINT fk_statusm_message FOREIGN KEY(IdM)
        REFERENCES Message(IdM)
);

CREATE TABLE Utilisateur (
    IdU varchar(64) NOT NULL,
    NomU varchar(64) NOT NULL,
    PrenomU varchar(64) NOT NULL,
    TypeU int(4) NOT NULL,
    MailU varchar(64) NOT NULL,
    PasswordU varchar(64) NOT NULL,
    ListeIpU varchar(64) NOT NULL,
    BloqueU int(4) NOT NULL,
    CONSTRAINT pk_utilisateur PRIMARY KEY(IdU)
);