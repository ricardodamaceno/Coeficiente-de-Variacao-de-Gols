package com.apostas;

public class Desvio {

	private double desvio;

	private String quantidade = "";
	
	public double getDesvio() {
		return desvio;
	}
	
	public Desvio(double desvio) {
		this.desvio = desvio;
	}
	
	@Override
	public String toString() {
		return this.quantidade + desvio;
	}
}
