package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {

	private Player p1;
	private double delta;
	
	public GiocatoreMigliore(Player p1, double delta) {
		super();
		this.p1 = p1;
		this.delta = delta;
	}

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	
	
	
}
