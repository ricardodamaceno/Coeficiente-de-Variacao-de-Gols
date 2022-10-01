package com.apostas.model;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class PesquisaTime {
	// JavascriptExecutor js;

	private WebDriver driver;

	private String nomeTime;

	private double coeficienteDeVariacao;

	private int jogosAnalisados;

	public double desvioPadrao;

	public double mediaGol;
	
	public double mediaGolCasa;
	
	public double mediaGolFora;

	public double mediaGolsTomados;

	private List<Gols> gol = new ArrayList<Gols>();
	private List<Gols> golCasa = new ArrayList<Gols>();
	private List<Gols> golFora = new ArrayList<Gols>();
	private List<Gols> golsTomados = new ArrayList<Gols>();
	private List<Desvio> desvio = new ArrayList<Desvio>();

	private double mediaCincoGolsFeitos;
	
	private double mediaCincoGolsFeitosCasa;
	
	private double mediaCincoGolsFeitosFora;

	private double mediaCincoGolsTomados;

	private int contador = 0;

	private int quantidadeDeJogos;

	private int tamanho;

	private double varianciaAoQuadrado;

	public PesquisaTime() {
	}

	public PesquisaTime(String nome) {
		this.nomeTime = nome;
	}

	public PesquisaTime(double coeficienteDeVariacao) {
		this.coeficienteDeVariacao = coeficienteDeVariacao;
	}

	public PesquisaTime(int jogosAnalisados) {
		this.jogosAnalisados = jogosAnalisados;
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
		
		pesquisaNomeTime();
		
		colocaGolsNaLista();

		adicionaMediaGolCasaEFora();
		
		calculaMediaGolsCasa();
		
		calculaMediaGolsFora();

		calculaMedia();

		mediaPassandoValorDosJogosAcimaDoTotal();


		calculaVariancia();
		
		calculaCoeficienteDeVariacao();

		arredondar();

		todosOsJogos();
		
		System.out.println(golCasa.size());
		System.out.println(mediaGolCasa);
		System.out.println(golCasa);
		System.out.println(golFora);
		System.out.println(golFora.size());
		System.out.println(mediaGolFora);
		System.out.println(gol);
		System.out.println(mediaGol);//
		System.out.println(mediaGolsTomados);//
		System.out.println(varianciaAoQuadrado);
		System.out.println(desvioPadrao);
		System.out.println(quantidadeDeJogos);
		System.out.println(
				"A variação é de " + coeficienteDeVariacao + " gols em relção à médiia de " + mediaGol + " gols");
//		System.out.println("A porcentagem de variação é de: " + porcentagemDeVariacao + "%");
		System.out.println(nomeTime);
	}
	
	private void pesquisaNomeTime() {
		// abre o navegador e faz a busca pelo time
//		setNomeTime("Psg");
		driver.get("https://fbref.com/pt/search/search.fcgi?search=" + nomeTime);
		String[] s = nomeTime.trim().split(" ");
		// esse split é pra times com nomes compostos, quando entrar no "if" ele vai
		// buscar
		// pelo último nome do time

		if (driver.getCurrentUrl().endsWith(s[s.length - 1])) {
			// isso é pras situações em que há mais de um opção de time pra escolher
			// o gatilho é ele ver se a url termina com o nome do time
			// se terminar então ele entra nessa condição
			// ele pega primeira ocorrencia que aparecer o nome do time e clica
			System.out.println(s[s.length - 1]);
			String timeUpperCase = s[s.length - 1].substring(0, 1).toUpperCase().concat(s[s.length - 1].substring(1));
			String primeiroNome = s[0].substring(0, 1).toUpperCase().concat(s[0].substring(1));
			System.out.println(nomeTime);
			if (s.length > 1) {
				nomeTime = primeiroNome + " " + timeUpperCase;
			} else {
				nomeTime = timeUpperCase;
			}
			// aqui é pra transformar a primeira letra em maiúscula, caso não for colocada
			// na pesquisa
			driver.findElement(By.linkText(nomeTime)).click();
			// aqui ele acho a primeira ocorrência e clicou
		}

	}
	
	private void colocaGolsNaLista() {
		
		for (int i = 1; i <= 100; i++) {
			try {
				// aqui vai procurando os gols feitos pelo time. O "i" vai sendo alterado pra
				// pegar o valor do gol na linha debaixo da tabela no site
				// foi utlizado o "try catch" pq estava dando uma excepion em algumas pesquisas
				// e
				// impossibilitando o "for" de continuar
				String textGol = driver
						.findElement(By.cssSelector("#matchlogs_for tr:nth-child(" + i + ") > .right:nth-child(8)"))
						.getText();
				String textGolTomado = driver
						.findElement(By.cssSelector("#matchlogs_for tr:nth-child(" + i + ") > .right:nth-child(9)"))
						.getText();

//				("#matchlogs_for tr:nth-child(" + i + ") = linha 
//				.right:nth-child(8) = coluna
				if (!textGol.isEmpty()) {
					// aqui eu verifico se o valor for diferente de vazio eu vou adicionado a
					// ArrayList, e se for vazio significa que acabou o laço
					System.out.println("gol tomado " + textGolTomado);
					int numeroDeGols = Integer.parseInt(textGol);
					int numeroDeGolsTomados = Integer.parseInt(textGolTomado);
					// aqui foi preciso transformar em int para adicionar ao somaGol
					gol.add(new Gols(numeroDeGols));
					golsTomados.add(new Gols(numeroDeGolsTomados));
					// aqui adiciono os gols a uma lista
					contador++;
					System.out.println(contador);
				}
			} catch (NoSuchElementException | NumberFormatException n) {

			}

		}
		
//		String golcasa = driver
//				.findElement(By.cssSelector("#matchlogs_for > tbody > tr:nth-child(1) > td:nth-child(6)"))
//				.getText();
//		System.out.println(golcasa);

	}
	
	private void adicionaMediaGolCasaEFora() {
		for (int i = 1; i <= 100; i++) {
			try {
				String casa = driver
						.findElement(By.cssSelector("#matchlogs_for > tbody > tr:nth-child(" + i + ") > td:nth-child(6)"))
						.getText();					//#matchlogs_for > tbody > tr:nth-child(1) > td:nth-child(6)
				System.out.println(casa);
				String gols = driver
						.findElement(By.cssSelector("#matchlogs_for tr:nth-child(" + i + ") > .right:nth-child(8)"))
						.getText();
				
				if (casa.length() == 7) {
					int numeroDeGols = Integer.parseInt(gols);
					golCasa.add(new Gols(numeroDeGols));
				} else {
					int numeroDeGols = Integer.parseInt(gols);
					golFora.add(new Gols(numeroDeGols));
				}
			} catch (NoSuchElementException | NumberFormatException n) {

			}

		}

	}
	
	private void calculaMediaGolsCasa() {
		int tamanhoCasa = golCasa.size() - 1;
		int espacoCasa;
		
		if (jogosAnalisados <= golFora.size()) {
			espacoCasa = golCasa.size() - jogosAnalisados;
		} else {
			espacoCasa = 0;
		}
		
		for(int a = tamanhoCasa; a >= espacoCasa; a-- ) {
			mediaCincoGolsFeitosCasa += golCasa.get(a).getGol();
		}
		System.out.println("gols em casa " + mediaCincoGolsFeitosCasa);
		if (jogosAnalisados <= golCasa.size()) {
			mediaGolCasa = mediaCincoGolsFeitosCasa / jogosAnalisados;
		} else {
			mediaGolCasa = mediaCincoGolsFeitosCasa / golCasa.size();
		}

	}
	
	private void calculaMediaGolsFora() {
		int espacoFora;
		int tamanhoFora = golFora.size() - 1;
		
		if (jogosAnalisados <= golFora.size()) {
			espacoFora = golFora.size() - jogosAnalisados;
		} else {
			espacoFora = 0;
		}
		
		for(int a = tamanhoFora; a >= espacoFora; a-- ) {
			mediaCincoGolsFeitosFora += golFora.get(a).getGol();
		}
		System.out.println("gols fora " + mediaCincoGolsFeitosFora);
		if (jogosAnalisados <= golFora.size()) {
			mediaGolFora = mediaCincoGolsFeitosFora / jogosAnalisados;
		} else {
			mediaGolFora = mediaCincoGolsFeitosFora / golFora.size();
		}

	}

	
	
	private void calculaMedia() {
		quantidadeDeJogos = gol.size() - 1;
		if (jogosAnalisados <= gol.size()) {
			tamanho = gol.size() - jogosAnalisados;
		} else {
			tamanho = 0;
			System.out.println("muito fora");
		}

		try {

			for (int a = quantidadeDeJogos; a >= tamanho; a--) {
				mediaCincoGolsFeitos += gol.get(a).getGol();
				mediaCincoGolsTomados += golsTomados.get(a).getGol();
			}
		} catch (IndexOutOfBoundsException e) {
			// isso é pro quando o time tiver menos de 5 jogos
			mediaCincoGolsFeitos = 0;
			mediaCincoGolsTomados = 0;
			for (int a = quantidadeDeJogos; a >= 0; a--) {
				mediaCincoGolsFeitos = gol.get(a).getGol();
				mediaCincoGolsTomados += golsTomados.get(a).getGol();
			}
		}

	}
	
	private void mediaPassandoValorDosJogosAcimaDoTotal() {
//		isso é pra não fazer um calculo fora da quantidade de jogos
		if (jogosAnalisados <= gol.size()) {
			mediaGol = mediaCincoGolsFeitos / jogosAnalisados;
//			mediaGolCasa = mediaCincoGolsFeitosCasa / jogosAnalisados;
//			mediaGolFora = mediaCincoGolsFeitosFora / jogosAnalisados;
			mediaGolsTomados = mediaCincoGolsTomados / jogosAnalisados;
		} else {
			mediaGol = mediaCincoGolsFeitos / gol.size();
//			mediaGolCasa = mediaCincoGolsFeitosCasa / quantidadeDeJogos;
//			mediaGolFora = mediaCincoGolsFeitosFora / quantidadeDeJogos;
			mediaGolsTomados = mediaCincoGolsTomados / gol.size();
		}

	}
	
	private void calculaVariancia() {
		try {

			for (int a = quantidadeDeJogos; a >= tamanho; a--) {
				// esse "for reverso" foi para pegar os 5 últimos gols da lista"

				// inicio do cálculo do coeficiente de variação
				double fazDesvio = gol.get(a).getGol() - mediaGol;
//				System.out.println(fazDesvio);
				desvio.add(new Desvio(fazDesvio));
				int index0 = quantidadeDeJogos - a; //
				double quadrado = Math.pow(desvio.get(index0).getDesvio(), 2.0);
				varianciaAoQuadrado += quadrado;

			}
		} catch (IndexOutOfBoundsException e) {
			// caso dê essa exception é sinal que o time tem menos de 5 jogos
			// então eu preciso zerar os atributos e limpar a lista para começar
			// um novo laço para pegar o jogos

			varianciaAoQuadrado = 0;
			desvio.removeAll(desvio);

			for (int a = quantidadeDeJogos; a >= 0; a--) {
				double fazDesvio = gol.get(a).getGol() - mediaGol;
				desvio.add(new Desvio(fazDesvio));
				int index0 = quantidadeDeJogos - a; //
				double quadrado = Math.pow(desvio.get(index0).getDesvio(), 2.0);
				varianciaAoQuadrado += quadrado;
			}
		}

	}

	private void calculaCoeficienteDeVariacao() {
//		aqui é a plicação das fórmulas
		double variancia = varianciaAoQuadrado / desvio.size();
		desvioPadrao = Math.sqrt(variancia);

		coeficienteDeVariacao = (desvioPadrao / mediaGol) * 100;//
//		porcentagemDeVariacao = (int) ((coeficienteDeVariacao * 100) / mediaGol);//

	}

	private void todosOsJogos() {
		if (jogosAnalisados > gol.size()) {
			// isso é pra exibir o máximo de jogos quando os jogosAnalisados for superior à
			// quantidade de jogos reais
			jogosAnalisados = 0;
			jogosAnalisados = gol.size();
		}

	}

	private void arredondar() {
		coeficienteDeVariacao = (double) (Math.round(coeficienteDeVariacao * 100.0) / 100.0);
		mediaGol = (double) (Math.round(mediaGol * 100.0) / 100.0);
		mediaGolCasa = (double) (Math.round(mediaGolCasa * 100.0) / 100.0);
		mediaGolFora = (double) (Math.round(mediaGolFora * 100.0) / 100.0);
		mediaGolsTomados = (double) (Math.round(mediaGolsTomados * 100.0) / 100.0);
		desvioPadrao = (double) (Math.round(desvioPadrao * 100.0) / 100.0);
		// isso arrendondo o doble pra duas casas decimais

	}

	public String getNomeTime() {
		String[] s = nomeTime.trim().split(" ");
		String timeUpperCase = s[s.length - 1].substring(0, 1).toUpperCase().concat(s[s.length - 1].substring(1));
		String primeiroNome = s[0].substring(0, 1).toUpperCase().concat(s[0].substring(1));
		if (s.length > 1) {
			nomeTime = primeiroNome + " " + timeUpperCase;
		} else {
			nomeTime = timeUpperCase;
		}
		return nomeTime;
	}

	public void setNomeTime(String nomeTime) {
		this.nomeTime = nomeTime;
	}

	public double getCoeficienteDeVariacao() {
		return coeficienteDeVariacao;
	}

	public void setCoeficienteDeVariacao(double coeficienteDeVariacao) {
		this.coeficienteDeVariacao = coeficienteDeVariacao;
	}

	public double getDesvioPadrao() {
		return desvioPadrao;
	}

	public void setDesvioPadrao(double desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}

	public double getMediaGol() {
		return mediaGol;
	}

	public void setMediaGol(double mediaGol) {
		this.mediaGol = mediaGol;
	}

	public double getMediaGolsTomados() {
		return mediaGolsTomados;
	}

	public void setMediaGolsTomados(double mediaGolsTomados) {
		this.mediaGolsTomados = mediaGolsTomados;
	}

	public int getJogosAnalisados() {
		return jogosAnalisados;
	}

	public void setJogosAnalisados(int jogosAnalisados) {
		this.jogosAnalisados = jogosAnalisados;
	}
	
	public double getMediaGolCasa() {
		return mediaGolCasa;
	}

	public void setMediaGolCasa(double mediaGolCasa) {
		this.mediaGolCasa = mediaGolCasa;
	}

	public double getMediaGolFora() {
		return mediaGolFora;
	}

	public void setMediaGolFora(double mediaGolFora) {
		this.mediaGolFora = mediaGolFora;
	}
}
