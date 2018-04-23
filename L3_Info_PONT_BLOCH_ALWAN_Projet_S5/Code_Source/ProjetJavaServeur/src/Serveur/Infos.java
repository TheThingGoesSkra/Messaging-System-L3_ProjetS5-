package Serveur;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Infos {

        public String message;
        public List<String> clientsConcernes;

        public Infos(String message, List<String> clientsConcernes) {
            this.message = message;
            this.clientsConcernes = clientsConcernes;
        }

}
