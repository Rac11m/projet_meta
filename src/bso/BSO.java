package bso;

import util.Objet;
import util.Sac;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSO {
    public static int[][] bso(Sac[] sacs, Objet[] objets, int beeColony_size, int max_chances, int flip) {
        int[][] Sref = genSolInit(sacs,objets); // BEE INIT
        List<int[][]> tabooList = new ArrayList<>();
        List<int[][]> DanseTable = new ArrayList<>();
        List<int[][]> searchArea;
        int[][] Sbest = Sref;
        int nb_chances = 3;
        for(int generation=0; generation < stopCondition(); generation++) {
            tabooList.add(Sref.clone());
            searchArea = searchPoints(Sref, beeColony_size, flip, sacs, objets);

            for(int[][] s: searchArea) {
                int[][] localBest = localSearch(s, sacs, objets, 1000);
                DanseTable.add(localBest);
            }
            if (DanseTable.isEmpty()) {

            } else {
            Sbest = DanseTable.get(0);
            for(int[][] sol: DanseTable) {
                if (calcul_fitness(sol, sacs, objets) > calcul_fitness(Sbest, sacs, objets)) {
                    Sbest = sol;
                }
            }
            }
            Sref = updateSref(Sref, Sbest, tabooList, DanseTable, nb_chances, max_chances, sacs, objets);

        }
        return Sref;
    }

    public static int[][] genSolInit(Sac[] sacs, Objet[] objets) {
        int[][] Sol = new int[sacs.length][objets.length];
        for(int j=0; j< objets.length; j++) {
            Random rand = new Random();
            int i =rand.nextInt(sacs.length);

            if(objets[j].getPoids_obj() + calcul_poids(Sol, sacs[i], objets) <= sacs[i].getPoids_sac()) {
                Sol[i][j] = 1;
            } else {
                Sol[i][j] = 0;
            }
        }
        return Sol;
    }

    public static List<int[][]> searchPoints(int[][] sref, int beeColony_size, int flip, Sac[] sacs, Objet[] objets) {
        List<int[][]> searchArea = new ArrayList<>();
        int h =0;

        while (searchArea.size() < beeColony_size && h < flip) {
            int[][] s = new int[sref.length][sref[0].length];
            cloneMatrice(s, sref);
            int p = 0;
            do {
                invert(s, flip*p+h);
                p++;
            } while (flip*p+h <= sref[0].length);

            if(sol_valide(s, sacs, objets)) {
                boolean is_unique = true;
                for (int[][] sa: searchArea) {
                    if(difference(sa, s) == 0) {
                        is_unique = false;
                        break;
                    }
                }
                if(is_unique) {
                    searchArea.add(s);
                }
            }
            h++;
        }
        return searchArea;
    }

    public static void invert(int[][] s, int f) {
        Random rand = new Random();
        int sac_rand = rand.nextInt(s.length);
        int[] sac = s[sac_rand].clone();

        for(int i=0; i < s.length; i++) {
            if(f >= 0 && f < sac.length) {
                s[i][f] = sac[i];
            }
        }
    }

    public static int[][] localSearch(int[][] sol, Sac[] sacs, Objet[] objets, int max_iter) {
        int[][] currentSol = new int[sol.length][sol[0].length];
        cloneMatrice(currentSol, sol);
        double f_currentSol = calcul_fitness(currentSol, sacs, objets);

        boolean meilleur = false;
        int i =0;
        while (!meilleur && i < max_iter) {

            for(int j=0; j < currentSol.length; j++) {
                for(int k=0; k < currentSol[0].length; k++) {
                    int[][] sol_voisin = new int[currentSol.length][currentSol[0].length];
                    cloneMatrice(sol_voisin, currentSol);

                    sol_voisin[j][k] = (sol_voisin[j][k] == 0) ? 1 : 0;
                    if (sol_valide(sol_voisin,sacs,objets)) {
                        double f_sol_voisin = calcul_fitness(sol_voisin, sacs, objets);
                        if (f_sol_voisin > f_currentSol) {
                            cloneMatrice(currentSol, sol_voisin);
                            f_currentSol = f_sol_voisin;
                            meilleur = true;
                        }
                    }
                }

            }
            i++;
        }
        return currentSol;
    }

    public static int[][] updateSref(int[][] Sref, int[][] Sbest, List<int[][]> tabooList, List<int[][]> DanseTable, int nb_chances, int max_chances, Sac[] sacs, Objet[] objets) {
        if (!DanseTable.isEmpty()) {
            int[][] new_sref = new int[Sref.length][Sref[0].length];
            double delta_f = calcul_fitness(Sbest, sacs, objets) - calcul_fitness(Sref, sacs, objets);

            if (delta_f > 0) {
                cloneMatrice(new_sref, DanseTable.get(0));
                for (int[][] d : DanseTable) {
                    if (calcul_fitness(d, sacs, objets) > calcul_fitness(new_sref, sacs, objets)) {
                        cloneMatrice(new_sref, d);
                    }
                }

                if (nb_chances < max_chances) {
                    nb_chances = max_chances;
                }
            } else {
                nb_chances--;
                if (nb_chances > 0) {
                    cloneMatrice(new_sref, DanseTable.get(0));
                    for (int[][] d : DanseTable) {
                        if (calcul_fitness(d, sacs, objets) > calcul_fitness(new_sref, sacs, objets)) {
                            cloneMatrice(new_sref, d);
                        }
                    }
                } else {
                    cloneMatrice(new_sref, DanseTable.get(0));
                    for (int[][] d : DanseTable) {
                        if (calcul_diversite(d, sacs, objets, tabooList) > calcul_diversite(new_sref, sacs, objets, tabooList)) {
                            cloneMatrice(new_sref, d);
                        }
                    }
                    nb_chances = max_chances;
                }
            }
            return new_sref;
        } else {
            return Sref;
        }
    }

    public static int stopCondition() {
        return 1000;
    }

    public static double calcul_fitness(int[][] sol, Sac[] sacs, Objet[] objets) {
        int somme_val_obj = 0;
        int somme_poids_obj = 0;

        for(int i=0; i< sacs.length; i++) {
            for(int j=0; j< objets.length; j++) {
                if (sol[i][j] == 1) {
                    somme_val_obj += objets[j].getVal_obj();
                    somme_poids_obj += objets[j].getPoids_obj();
                }
            }
        }
        double fitness = (1.0/(somme_poids_obj) + somme_val_obj);
        return fitness;
    }
    public static int calcul_val(int[][] e, Sac[] sacs, Objet[] objets){
        int val = 0;
        for (Sac sac: sacs){
            for (int i = 0; i < e[sac.getNum_sac()].length; i++) {
                if (e[sac.getNum_sac()][i] == 1){
                    val += objets[i].getVal_obj();
                }
            }
        }
        return val;
    }
    public static double calcul_poids(int[][] e, Sac sac, Objet[] objets){
        double poids = 0;
        for (int i = 0; i < e[sac.getNum_sac()].length; i++) {
            if (e[sac.getNum_sac()][i] == 1){
                poids += objets[i].getPoids_obj();
            }
        }
        return poids;
    }
    public static void cloneMatrice(int[][]mat, int[][] matrice) {
        for(int i = 0; i < matrice.length; i++) {
            for(int j = 0; j < matrice[i].length; j++) {
                mat[i][j] = matrice[i][j];
            }
        }
    }
    public static boolean sol_valide(int[][] sol, Sac[] sacs, Objet[] objets) {
        boolean v = true;
        int j = 0;
        int i = 0;
        while (i < objets.length && v) {
            boolean exist = false;
            j = 0;
            int compte = 0;
            while (!exist && j < sacs.length) {
                if (sol[j][i] == 1) {
                    compte++;
                }
                if (compte > 1) {
                    exist = true;
                } else {
                    j++;
                }
            }
            if (exist) {
                v = false;
            } else {
                i++;
            }
        }
        if (v) {
            for (Sac s : sacs) {
                if (calcul_poids(sol, s, objets) > s.getPoids_sac()) {
                    v = false;
                    break;
                }
            }
        }
        return v;
    }

    public static int difference(int[][] m1, int[][] m2) {
        int d=0;
        for(int i=0; i< m1.length; i++) {
            for(int j=0; j < m1[0].length; j++) {
                if (m1[i][j] != m2[i][j]) {
                    d++;
                }
            }
        }
        return d;
    }
    public static int calcul_diversite(int[][] sref, Sac[] sacs, Objet[] objets, List<int[][]> TabooList) {
        int minDifference = difference(sref, TabooList.get(0));
        int difference;

        for(int[][] t: TabooList) {
            difference = difference(sref, t);
            if (difference < minDifference) {
                minDifference = difference;
            }
        }
    return minDifference;
    }
}
