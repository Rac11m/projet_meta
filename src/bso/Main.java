package bso;

import ga.GA;
import util.Objet;
import util.Sac;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int nbr_sac = 5;
        int nbr_objet = 10;

        Sac[] sacs = new Sac[nbr_sac];
        Objet[] objets = new Objet[nbr_objet];

        Random rand = new Random();

        for (int i = 0; i < nbr_sac; i++){
            sacs[i] = new Sac(i, rand.nextInt(2000)+100);
        }

        for (int i = 0; i < nbr_objet; i++){
            objets[i] = new Objet(i, rand.nextInt(300)+5, rand.nextInt(100)+20);
        }

        for (int i = 0; i < nbr_sac; i++){
            System.out.println("sac"+sacs[i].getNum_sac()+": "+ sacs[i].getPoids_sac()+"g");
        }

        for (int i = 0; i < nbr_objet; i++){
            System.out.println("objet"+objets[i].getNum_obj()+": "+ objets[i].getVal_obj()+"$, "+objets[i].getPoids_obj()+"g");
        }

        int beeColony_size = 100;
        int max_chances = 3;
        int flip = 2;

        int[][] but = BSO.bso(sacs, objets, beeColony_size, max_chances, flip);
        for(int i = 0;i<nbr_sac;i++) {
            for(int j=0;j<nbr_objet;j++) {
                System.out.print(but[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
