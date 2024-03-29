package util;

public class Sac {
	// attributs
	private int num_sac;
	private double poids_sac;
	
	
	// Constructor
	public Sac(int num_sac, double poids_sac) {
		this.num_sac = num_sac;
		this.poids_sac = poids_sac;
	}
	
	
	// Setters and Getters
	public int getNum_sac() {
		return num_sac;
	}
	
	public void setNum_sac(int num_sac) {
		this.num_sac = num_sac;
	}
	
	public double getPoids_sac() {
		return poids_sac;
	}
	public void setPoids_sac(double poids_sac) {
		this.poids_sac = poids_sac;
	}
	
	
}
