package a_star;

import java.util.Random;

import util.Etat;
import util.Objet;
import util.Sac;

public class Main {
	public static void main(String[] args) {
		 int nbr_sac = 6;
	       int nbr_objet = 6;

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

	        Etat but = new Etat(nbr_sac, nbr_objet);
	        int[][] matinit = new int[nbr_sac][nbr_objet];
	        for(int i = 0;i<nbr_sac;i++) {
	        	for(int j=0;j<nbr_objet;j++) {
	        		matinit[i][j] = 0;
	        	}
	        }
	        but.setMatrice(matinit);
	        but.setG(0); // poids
//	        for (Sac s:sacs) {
//	        	but.setH(but.getH() + s.getPoids_sac());	        	
//	        }
	        for (Objet o: objets) {
	        	but.setH(but.getH() + o.getVal_obj());	        	
	        }
	        but.setF(but.getG() + but.getH());
	        but = Astar.astar(but, sacs, objets);
	        for(int i = 0;i<nbr_sac;i++) {
	        	for(int j=0;j<nbr_objet;j++) {
	        		System.out.print(but.getMatrice()[i][j] + "\t");
	        	}
      		System.out.println();
	        }
	}
}
