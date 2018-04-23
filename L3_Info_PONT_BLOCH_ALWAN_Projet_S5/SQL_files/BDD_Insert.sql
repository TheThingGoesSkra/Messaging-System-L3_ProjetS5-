INSERT INTO utilisateur (IdU, NomU, PrenomU, TypeU, PasswordU, MailU, ListeIpU, BloqueU) VALUES ("jean","Hubert","Jean",0,"hubert","jeanhubert@mail.com","",0);
INSERT INTO utilisateur (IdU, NomU, PrenomU, TypeU, PasswordU, MailU, ListeIpU, BloqueU) VALUES ("antoine","France","Antoine",0,"france","antoinefrance@mail.com","",0);
INSERT INTO utilisateur (IdU, NomU, PrenomU, TypeU, PasswordU, MailU, ListeIpU, BloqueU) VALUES ("yvain","Sacrebleu","Yvain",1,"sacrebleu","yvainsacrebleu@mail.com","",0);

INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest1",1);
INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest0",0);
INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest11",1);
INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest01",0);
INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest12",1);
INSERT INTO groupe (NomG, TypeG) VALUES ("GroupeTest02",0);

INSERT INTO ticket (IdT, TitreT, DateT, NomG, IdU) VALUES (0,"TicketTest",'2018-01-03 2:18:20',"GroupeTest1","jean");

INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (0,"Bonjour ceci est un test",3,'2018-01-03 2:18:20',0,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",0,2), ("antoine",0,2), ("yvain",0,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (1,"Bonjour ceci est le dernier message",3,'2018-01-03 3:18:20',0,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",1,2), ("antoine",1,2), ("yvain",1,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (2,"Bonjour ceci n'est pas le dernier message",3,'2018-01-03 2:18:50',0,"yvain");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",2,2), ("antoine",2,2), ("yvain",2,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (3,"test",3,'2018-01-03 3:18:30',0,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",3,2), ("antoine",3,2), ("yvain",3,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (4,"test2",3,'2018-01-03 3:18:40',0,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",4,2), ("antoine",4,2), ("yvain",4,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (5,"test3",2,'2018-01-03 3:18:50',0,"yvain");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",5,1), ("antoine",5,1), ("yvain",5,2);

INSERT INTO ticket (IdT, TitreT, DateT, NomG, IdU) VALUES (1,"TicketTest2",'2018-01-03 2:18:20',"GroupeTest0","yvain");

INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (6,"2test",3,'2018-01-03 13:18:30',1,"yvain");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",6,2), ("antoine",6,2), ("yvain",6,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (7,"2test2",3,'2018-01-03 13:18:40',1,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",7,2), ("antoine",7,2), ("yvain",7,2);
INSERT INTO message (IdM, ContenuM, CouleurM, DateM, IdT, IdU) VALUES (8,"2test3",3,'2018-01-03 13:18:50',1,"jean");
INSERT INTO statusm (IdU, IdM, StatusM) VALUES ("jean",8,2), ("antoine",8,2), ("yvain",8,2);

INSERT INTO appartient (IdU, NomG) VALUES ("yvain","GroupeTest1");
INSERT INTO appartient (IdU, NomG) VALUES ("jean","GroupeTest0");
INSERT INTO appartient (IdU, NomG) VALUES ("antoine","GroupeTest0");