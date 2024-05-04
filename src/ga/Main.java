package ga;

import util.Etat;
import util.Objet;
import util.Sac;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int nbr_sac = 500;
        int nbr_objet = 1500;

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

        int population_size = 100;
        double p_crossover = 0.7;
        double p_mutation = 0.3;

        int[][] but = GA.ga(sacs, objets, population_size, p_crossover, p_mutation);
        for(int i = 0;i<nbr_sac;i++) {
            for(int j=0;j<nbr_objet;j++) {
                System.out.print(but[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
