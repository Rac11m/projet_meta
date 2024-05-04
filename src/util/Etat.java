package util;

public class Etat {
	// attributs
	private int[][] matrice;
//	private int g = 0; // valeurTotale pour l'instant
	private double g = 0;
	private double h = 0; // poids restant (decrementation)
	private double f = 0; // g+h
	
	// constructor
	public Etat(int nbr_sac, int nbr_obj) {
		this.setMatrice(new int[nbr_sac][nbr_obj]);
	}

	// Setters and Getters
	public int[][] getMatrice() {
		return matrice;
	}

	public void setMatrice(int[][] matrice) {
		this.matrice = matrice;
	}
	
//	public int getG() {
	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	
	// methods
	public void updateMatrice(int element, int i, int j) {
		this.matrice[i][j] = element;
	}
	
	public void cloneMatrice(int[][] matrice) {
		for(int i = 0; i < matrice.length; i++) {
			for(int j = 0; j < matrice[i].length; j++) {
				this.matrice[i][j] = matrice[i][j];
			}
		}
	}
}
