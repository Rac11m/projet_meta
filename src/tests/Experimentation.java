package tests;

import a_star.Astar;
import bfs.BFS;
import dfs.DFS;
import util.Etat;
import util.Objet;
import util.Sac;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Experimentation {
	
	public static void main(String[] args) {
		for (int nbr_sac = 0; nbr_sac < 5; nbr_sac++) {
			for (int nbr_objet= 0; nbr_objet < 5; nbr_objet++) {
				genererDonneesTest(nbr_sac, nbr_objet, "tests/sacs.csv", "tests/objets.csv");
			}	
		}

        Sac[] sacs = readSacCSV("tests/sacs.csv");

        for (int i = 0; i < sacs.length; i++){
            System.out.println("sac"+sacs[i].getNum_sac()+": "+ sacs[i].getPoids_sac()+"kg");
        }

        Objet[] objets = readObjetCSV("tests/objets.csv");

        for (int i = 0; i < objets.length; i++){
            System.out.println("objet"+objets[i].getNum_obj()+": "+ objets[i].getVal_obj()+"$, "+objets[i].getPoids_obj()+"kg");
        }

        Etat but = new Etat(sacs.length, objets.length);

        writeResults(but, sacs, objets, "tests/results.csv", "tests/val_results.csv");
	}

    public static void genererDonneesTest(int nbr_sac, int nbr_objet, String sacCSV, String objetCSV) {

        Sac[] sacs = new Sac[nbr_sac];
        Objet[] objets = new Objet[nbr_objet];

        Random rand = new Random();

        for (int i = 0; i < nbr_sac; i++){
            sacs[i] = new Sac(i, ((Math.round(rand.nextDouble() * 22)*100.0)/100)+1);
        }

        for (int i = 0; i < nbr_objet; i++){
            objets[i] = new Objet(i, rand.nextInt(3000)+5, ((Math.round(rand.nextDouble() * 15)*100.0)/100)+1);
        }
        
        try (PrintWriter sacWriter = new PrintWriter(new File(sacCSV));
        		PrintWriter objetWriter = new PrintWriter(new File(objetCSV))) {
            
            for (Sac sac : sacs) {
            	sacWriter.println(sac.getNum_sac() + "," + sac.getPoids_sac());
            } 
            
            for (Objet objet : objets) {
            	objetWriter.println(objet.getNum_obj() + "," + objet.getPoids_obj() + "," + objet.getVal_obj());
            }            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static Sac[] readSacCSV(String sacCSV) {
        Vector<Sac> sacsV = new Vector<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sacCSV))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                int num_sac = Integer.parseInt(elements[0]);
                double poids_sac = Double.parseDouble(elements[1]);
                Sac sac = new Sac(num_sac, poids_sac);
                sacsV.add(sac);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sac[] sacs = new Sac[sacsV.size()];
        sacsV.toArray(sacs);
        return sacs;
    }

    public static Objet[] readObjetCSV(String objetCSV) {
        Vector<Objet> objetsV = new Vector<>();
        try (BufferedReader br = new BufferedReader(new FileReader(objetCSV))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                int num_objet = Integer.parseInt(elements[0]);
                double poids_objet = Double.parseDouble(elements[1]);
                int val_objet = Integer.parseInt(elements[2]);
                Objet objet = new Objet(num_objet, val_objet, poids_objet);
                objetsV.add(objet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objet[] objets = new Objet[objetsV.size()];
        objetsV.toArray(objets);
        return objets;
    }

    public static void writeResults (Etat but, Sac[] sacs, Objet[] objets, String resultatCSV, String valresultatCSV) {
        List tempsBFS = new ArrayList<>();
        int valBFS = 0;
        List tempsDFS = new ArrayList<>();
        int valDFS = 0;
        List tempsAStar = new ArrayList<>();
        int valAstar = 0;

        // Exécuter et mesurer le temps pour BFS
        long tempsDebutBFS = System.nanoTime();
        // Votre logique pour exécuter BFS
        Etat r = BFS.bfs(but, sacs, objets);
        valBFS = BFS.calcul_val(r, sacs, objets);
        long tempsFinBFS = System.nanoTime();
        long tempsExecutionBFS = tempsFinBFS - tempsDebutBFS;
        tempsBFS.add(tempsExecutionBFS);

        // Exécuter et mesurer le temps pour DFS
        long tempsDebutDFS = System.nanoTime();
        // Votre logique pour exécuter DFS
        r = DFS.dfs(but, sacs, objets);
        valDFS = DFS.calcul_val(r, sacs, objets);
        long tempsFinDFS = System.nanoTime();
        long tempsExecutionDFS = tempsFinDFS - tempsDebutDFS;
        tempsDFS.add(tempsExecutionDFS);

        // Exécuter et mesurer le temps pour A*
        long tempsDebutAStar = System.nanoTime();
        // Votre logique pour exécuter A*
        r = Astar.astar(but, sacs, objets);
        valAstar = Astar.calcul_val(r, sacs, objets);
        long tempsFinAStar = System.nanoTime();
        long tempsExecutionAStar = tempsFinAStar - tempsDebutAStar;
        tempsAStar.add(tempsExecutionAStar);

        // Écrire les résultats dans le fichier CSV
        try (PrintWriter writerResult = new PrintWriter(new File(resultatCSV));
             PrintWriter writerValResult = new PrintWriter(new File(valresultatCSV))
        ) {
            writerResult.println("NbSacs,NbObjets,BFS,DFS,A*");
            writerResult.println(sacs.length + "," + objets.length + "," + tempsBFS.get(0) + "," + tempsDFS.get(0) + "," + tempsAStar.get(0));
            writerValResult.println("NbSacs,NbObjets,BFS,DFS,A*");
            writerValResult.println(sacs.length + "," + objets.length + "," + valBFS + "," + valDFS + "," + valAstar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
