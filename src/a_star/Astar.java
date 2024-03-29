package a_star;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import util.Etat;
import util.Objet;
import util.Sac;

public class Astar {
		
	public static Etat astar(Etat r, Sac[] sacs, Objet[] objets) {		
		PriorityQueue<Etat> ouverte = new PriorityQueue<>(Comparator.comparingDouble(etat -> etat.getF()));
		Set<String> fermee = new HashSet<>();
			
		ouverte.add(r);		
		fermee.add(Arrays.deepToString(r.getMatrice()));
				
		while(!ouverte.isEmpty() ) {
			Etat e = ouverte.poll();				
			
			if (e.getF() <= r.getF()) {
				r.cloneMatrice(e.getMatrice());
				r.setH(e.getH());
				r.setG(e.getG());
				r.setF(e.getF());
				for(Sac s: sacs) {
					for(Objet o: objets) {
						if(!estVisite(e, o.getNum_obj())) {
							Etat f = new Etat(sacs.length, objets.length);
							f.cloneMatrice(e.getMatrice());
							f.updateMatrice(1, s.getNum_sac(), o.getNum_obj());
								
							if(calcul_poids(f, s, objets) <= s.getPoids_sac()) {
								if (!existDans(f, fermee)) {
									for(int i=0; i < sacs.length; i++) {
										for(int j=0; j < objets.length; j++) {
											System.out.print(f.getMatrice()[i][j]);
										}
										System.out.println();
									}
									System.out.println("---------------");
									
									f.setG(e.getG() + o.getPoids_obj());
									f.setH(e.getH() - o.getVal_obj());
									f.setF(f.getG() + f.getH());
									
									if(!fermee.contains(Arrays.deepToString(f.getMatrice()))) {
										ouverte.add(f);																				
										fermee.add(Arrays.deepToString(f.getMatrice()));
									}
								}
							}
						}
					}
				}	
			}
		}
		return r;
	}		
			private static boolean existDans(Etat e, Set<String> fermee) {
				int compteur = 0;
				for (String etat : fermee) {
					if(etat.equals(Arrays.deepToString(e.getMatrice()))) {
						compteur += 1;
					}
				}
				if (compteur == 0) {
					return false;			
				}
				return true;
			}
			private static boolean estVisite(Etat e, int objet){
		        for (int i = 0; i < e.getMatrice().length; i++) {
		            if (e.getMatrice()[i][objet] == 1){
		                return true;
		            }
		        }
		        return false;
		    }	
			public static double calcul_poids(Etat e, Sac sac, Objet[] objets){
		        double poids = 0;
		        for (int i = 0; i < e.getMatrice()[sac.getNum_sac()].length; i++) {
		            if (e.getMatrice()[sac.getNum_sac()][i] == 1){
		                poids += objets[i].getPoids_obj();
		            }
		        }
		        return poids;
		    }
		    public static int calcul_val(Etat e, Sac[] sacs, Objet[] objets){
		        int val = 0;
		        for (Sac sac: sacs){
		            for (int i = 0; i < e.getMatrice()[sac.getNum_sac()].length; i++) {
		                if (e.getMatrice()[sac.getNum_sac()][i] == 1){
		                    val += objets[i].getVal_obj();
		                }
		            }
		        }
		        return val;
		    }
	}

