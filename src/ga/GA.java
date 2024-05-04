package ga;

import util.Objet;
import util.Sac;
import java.util.*;


public class GA {
    public static int[][] ga(Sac[] sacs, Objet[] objets, int population_size, double p_crossover, double p_mutation) {

        List<int[][]> population = genPopulationInit(population_size, sacs, objets);
        int val_SGbest = Integer.MIN_VALUE;
        int[][] SGbest = new int[sacs.length][objets.length];
        for (int[][] p: population) {
            if (calcul_val(p, sacs,objets) >= val_SGbest) {
                cloneMatrice(SGbest,p);
                val_SGbest = calcul_val(SGbest, sacs,objets);
            }
        }


        for (int generation =0; generation < stopCondition(); generation++) {

                // calcul des fitness des individus de la population

                List<Double> les_fitness = population_fitness(population, sacs, objets);
                System.out.println(les_fitness);
                // iteration de plusieurs parents a ajouté
                Couple[] parents = selectParents(population, sacs, objets, population_size/2, les_fitness);
                List<Children> childrens = new ArrayList<Children>();
                for (Couple parent: parents) {
                    Children children = crossover(parent,p_crossover, sacs.length, objets.length);
                    children.child1 = mutate(children.child1,p_mutation, sacs, objets);
                    children.child2 = mutate(children.child2, p_mutation, sacs, objets);
                    childrens.add(children);
                }
                population = remplacement(population, childrens, population_size, sacs, objets);
                for (int[][] p: population) {
                    if (calcul_val(p, sacs,objets) >= val_SGbest) {
                        val_SGbest = calcul_val(SGbest, sacs,objets);
                        cloneMatrice(SGbest,p);
                    }
                }
        }

        return SGbest;
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
    public static List<int[][]> genPopulationInit(int Population_size, Sac[] sacs, Objet[] objets) {
        List<int[][]> population = new ArrayList<>(Population_size);
        for (int i=0; i< Population_size; i++) {
            population.add(genSolInit(sacs, objets));
        }
        return population;
    }
    public static Couple[] selectParents(List<int[][]> population, Sac[] sacs, Objet[] objets, int selection_size, List<Double> les_fitness) {
        PriorityQueue<int[][]> elite_population = new PriorityQueue<>(Collections.reverseOrder(Comparator.comparingDouble(individu -> calcul_fitness(individu, sacs, objets))));

        elite_population.addAll(population);
        Couple[] parents = new Couple[selection_size];
        for (int i=0; i<selection_size; i++) {
            int[][] parent1 = elite_population.poll();
            int[][] parent2 = elite_population.poll();
            parents[i] = new Couple(parent1, parent2);
        }
        return parents;
    }
    public static Children crossover(Couple couple, double P_crossover, int nbr_sac, int nbr_obj) {
        // Uni-point

        if (Math.random() > P_crossover) {
            return new Children(couple.parent1, couple.parent2);
        }

        int[][] child1 = new int[nbr_sac][nbr_obj];
        int[][] child2 = new int[nbr_sac][nbr_obj];

        int crossoverPoint = new Random().nextInt(nbr_obj);
        for (int j=0; j< nbr_sac; j++) {
            for (int k=0; k< nbr_obj; k++) {
                if (k < crossoverPoint) {
                    child1[j][k] = couple.parent1[j][k];
                    child2[j][k] = couple.parent2[j][k];
                } else {
                    child1[j][k] = couple.parent2[j][k];
                    child2[j][k] = couple.parent1[j][k];
                }
            }
        }
        return new Children(child1, child2);
    }
    public static int[][] mutate(int[][] sol, double P_mutation, Sac[] sacs, Objet[] objets) {
        if (Math.random() > P_mutation) {

            Random rand = new Random();

            for(int i=0; i< sacs.length; i++) {
                for(int j=0; j < objets.length; j++) {
                    sol[i][j] = rand.nextInt(2);
                }
            }

            if(!sol_valide(sol, sacs, objets)) {
                cloneMatrice(sol, genSolInit(sacs, objets));
            }
        }
        return sol;
    }
    public static List<int[][]> remplacement(List<int[][]> population, List<Children> children, int Population_size, Sac[] sacs, Objet[] objets) {
        PriorityQueue<int[][]> elite_children = new PriorityQueue<>(Collections.reverseOrder(Comparator.comparingDouble(individu -> calcul_fitness(individu, sacs, objets))));
        List<int[][]> intermediaire = new ArrayList<>();
        for(Children child: children) {
            intermediaire.add(child.child1);
            intermediaire.add(child.child2);
        }
        elite_children.addAll(population);
        elite_children.addAll(intermediaire);

        List<int[][]> new_population = new ArrayList<>();

        for(int i=0; i < Population_size; i++) {
            new_population.add(elite_children.poll());
        }
        return new_population;
    }
    public static int stopCondition() {
        return 1000;
    }
    public static List<Double> population_fitness(List<int[][]> population, Sac[] sacs, Objet[] objets) {
        List<Double> les_fitness = new ArrayList<>();
        for(int[][] individu : population) {
            les_fitness.add(calcul_fitness(individu, sacs, objets));
        }
        return les_fitness;
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
}
