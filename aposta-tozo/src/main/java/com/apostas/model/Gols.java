package com.apostas.model;

public class Gols implements Comparable<Gols>{

	private int gol;
	
	private String quantidade = "";

	public Gols(int gol) {
		this.gol = gol;
	}


	public int getGol() {
		return gol;
	}
	
	@Override
	public String toString() {
		return this.quantidade + gol;
	}


	@Override
	public int compareTo(Gols gols) {
		return getGol();
	}
	
}
