package com.apostas.model;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PesquisaTime{
	// JavascriptExecutor js; 

	private WebDriver driver;
	
	private String nomeTime;

	public String getNomeTime() {
		return nomeTime;
	}

	public void setNomeTime(String nomeTime) {
		this.nomeTime = nomeTime;
	}

	public double coeficienteDeVariacao;
	
	public double getCoeficienteDeVariacao() {
		return coeficienteDeVariacao;
	}
	
	public void setCoeficienteDeVariacao(double coeficienteDeVariacao) {
		this.coeficienteDeVariacao = coeficienteDeVariacao;
	}
	
	public int porcentagemDeVariacao;

	public int getPorcentagemDeVariacao() {
		return porcentagemDeVariacao;
	}

	public void setPorcentagemDeVariacao(int porcentagemDeVariacao) {
		this.porcentagemDeVariacao = porcentagemDeVariacao;
	}
	
	public double mediaGol;

	public double getMediaGol() {
		return mediaGol;
	}

	public void setMediaGol(double mediaGol) {
		this.mediaGol = mediaGol;
	}

	private int contador = 0;

	private List<Gols> gol = new ArrayList<Gols>();

	private double somaGol;

	private int golsEstipulados;

	private int tamanho;

	private int tamanhoMenorQueDez = 0;

	private double varianciaAoQuadrado;
	
	private List<Desvio> desvio = new ArrayList<Desvio>();
	
	

	public PesquisaTime(String nome) {
		this.nomeTime = nome;
	}
	
//	public CoeficienteDeVariacao(double coeficienteDeVariacao) {
//		this.coeficienteDeVariacao = coeficienteDeVariacao;
//	}
	
	public PesquisaTime() {
	}
	
	
	public void setUp() {

//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver/chromedriver");
//		driver = new ChromeDriver();

		System.setProperty("webdriver.gecko.driver", "drivers/geckodriver/geckodriver");
		driver = new FirefoxDriver();
	}

	public void tearDown() {
		driver.quit();
	}
	
	
	public void adicionaGols() {
		// abre o navegador e faz a busca pelo time
//		setNomeTime("Psg");
		driver.get("https://fbref.com/pt/search/search.fcgi?search=" + nomeTime);
		String[] s = nomeTime.trim().split(" ");
		//esse split é pra times com nomes compostos, quando entrar no "if" ele vai buscar 
		//pelo último nome do time
		
		if (driver.getCurrentUrl().endsWith(s[s.length-1])) {
			// isso é pras situações em que há mais de um opção de time pra escolher
			//o gatilho é ele ver se a url termina com o nome do time  
			//então ele pega primeira ocorrencia que aparecer o nome do time e clica
			System.out.println(s[s.length-1]);
//			String timeUpperCase = s[0].substring(0, 1).toUpperCase().concat(s[0].substring(1));
			//aqui era pra transformar a primeira letra em maiúscula
			driver.findElement(By.linkText(nomeTime)).click();
		}
		for (int i = 1; i <= 100; i++) {
			try {
				// aqui vai procurando os gols feitos pelo time. O "i" vai sendo alterado pra
				// pegar o valor do gol na linha debaixo da tabela no site
				String textGol = driver
						.findElement(By.cssSelector("#matchlogs_for tr:nth-child(" + i + ") > .right:nth-child(8)"))
						.getText();
//				("#matchlogs_for tr:nth-child(" + i + ") = linha 
//				.right:nth-child(8) = coluna
				if (!textGol.isEmpty()) {
					// aqui eu verifico se o valor for diferente de vazio eu vou adicionado a
					// ArrayList
					int numeroDeGols = Integer.parseInt(textGol);
					// aqui foi preciso transformar em int para adicionar ao somaGol
					somaGol += numeroDeGols;
					gol.add(new Gols(numeroDeGols));
					// aqui adiciono os gols a uma lista
					contador++;
					System.out.println(contador);
				}
			} catch (NoSuchElementException | NumberFormatException n) {

			}

		}

//		    calcula a media 
		mediaGol = (double) (somaGol / gol.size());
		golsEstipulados = gol.size() - 1;
		tamanho = gol.size() - 10; // quantidade de jogos que meu cliente quer que compare, sendo que ele gostaria
									// de apenas os últimos 10 jogos

		
		try {
			// isso é por conta de alguns times terem menos de 10 jogos
			for (int a = golsEstipulados; a >= tamanho; a--) {

				// inicia o calculo do coeficiente de variação
				double fazDesvio = gol.get(a).getGol() - mediaGol;
				desvio.add(new Desvio(fazDesvio));
				// como eu preciso pegar os 10 ultimos resultados então foi preciso fazer um "for"
				// reverso
				int index0 = golsEstipulados - a; //
				double quadrado = Math.pow(desvio.get(index0).getDesvio(), 2.0);
				varianciaAoQuadrado += quadrado;

			}
		} catch (IndexOutOfBoundsException e) {
			//aqui eu zero a varianciaAoQuadrado e a lista de desvio
			varianciaAoQuadrado = 0;
			desvio.removeAll(desvio);
			//nesse caso a exceção acima foi o gatilho pra usar esse for para times com menos de 10 gols
			for (int a = golsEstipulados; a >= tamanhoMenorQueDez; a--) {
				double fazDesvio = gol.get(a).getGol() - mediaGol;
				desvio.add(new Desvio(fazDesvio));
				int index0 = golsEstipulados - a; //
				double quadrado = Math.pow(desvio.get(index0).getDesvio(), 2.0); 
				varianciaAoQuadrado += quadrado;
			}
		}
//		aqui é a plicação das fórmulas
		double variancia = varianciaAoQuadrado / desvio.size();
		double desvioPadrao = Math.sqrt(variancia);
		coeficienteDeVariacao = (desvioPadrao / mediaGol);//
		porcentagemDeVariacao = (int) ((coeficienteDeVariacao * 100) / mediaGol);//
		
		coeficienteDeVariacao = (double) (Math.round(coeficienteDeVariacao*100.0)/100.0);
		mediaGol = (double) (Math.round(mediaGol*100.0)/100.0);
		//isso arrendondo o doble pra duas casas decimais
		System.out.println(gol);
		System.out.println(mediaGol);//
		System.out.println(varianciaAoQuadrado);
		System.out.println(variancia);
		System.out.println(desvioPadrao);
		System.out.println("A variação é de " + coeficienteDeVariacao 
				+ " gols em relção à médiia de " + mediaGol + " gols");
		System.out.println("A porcentagem de variação é de: " + porcentagemDeVariacao + "%");
		System.out.println(nomeTime);
	}

}