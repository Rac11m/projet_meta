package dfs;

import java.util.Random;

import util.Etat;
import util.Objet;
import util.Sac;

public class Main {
    public static void main(String[] args) {
        int nbr_sac = 3;
        int nbr_objet = 2;

        Sac[] sacs = new Sac[nbr_sac];
        Objet[] objets = new Objet[nbr_objet];

        Random rand = new Random();

        for (int i = 0; i < nbr_sac; i++){
            sacs[i] = new Sac(i, (Math.round(rand.nextDouble() * 22)*100.0)/100);
        }

        for (int i = 0; i < nbr_objet; i++){
            objets[i] = new Objet(i, rand.nextInt(3000)+5, (Math.round(rand.nextDouble() * 15)*100.0)/100);
        }

        for (int i = 0; i < nbr_sac; i++){
            System.out.println("sac"+sacs[i].getNum_sac()+": "+ sacs[i].getPoids_sac()+"kg");
        }

        for (int i = 0; i < nbr_objet; i++){
            System.out.println("objet"+objets[i].getNum_obj()+": "+ objets[i].getVal_obj()+"$, "+objets[i].getPoids_obj()+"kg");
        }

        Etat but = new Etat(nbr_sac, nbr_objet);



 		but = DFS.dfs(but, sacs, objets);

         for (int i = 0; i < nbr_sac; i++){
            for (int j = 0; j < nbr_objet; j++) {
                System.out.print(but.getMatrice()[i][j]+" ");
            }
            System.out.println();
        }
    }
}