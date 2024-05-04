package util;

public class Objet {
	// attributs
	private int num_obj;
	private int val_obj;
	private double poids_obj;
	
	// constructor
	public Objet(int num_obj, int val_obj, double poids_obj) {
		this.num_obj = num_obj;
		this.val_obj = val_obj;
		this.poids_obj = poids_obj;
	}
	
	// Setters and Getters
	public int getNum_obj() {
		return num_obj;
	}

	public void setNum_obj(int num_obj) {
		this.num_obj = num_obj;
	}

	public int getVal_obj() {
		return val_obj;
	}

	public void setVal_obj(int val_obj) {
		this.val_obj = val_obj;
	}

	public double getPoids_obj() {
		return poids_obj;
	}

	public void setPoids_obj(double poids_obj) {
		this.poids_obj = poids_obj;
	}
	
}
