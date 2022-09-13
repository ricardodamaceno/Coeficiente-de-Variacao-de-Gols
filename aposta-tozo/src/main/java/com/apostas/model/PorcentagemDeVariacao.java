package com.apostas.model;

public class PorcentagemDeVariacao extends PesquisaTime{

	double porcentagemDeVariacao;

	public int getPorcentagemDeVariacao() {
		return super.porcentagemDeVariacao;
	}

	public void setPorcentagemDeVariacao(int porcentagemDeVariacao) {
		super.porcentagemDeVariacao = porcentagemDeVariacao;
	}

	public PorcentagemDeVariacao(int porcentagemDeVariacao) {
		super.porcentagemDeVariacao = porcentagemDeVariacao;
	}
	
	public PorcentagemDeVariacao() {
	}
}
