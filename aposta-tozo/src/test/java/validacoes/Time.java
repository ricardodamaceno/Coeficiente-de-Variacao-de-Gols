package validacoes;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
// Generated by Selenium IDE
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.apostas.Desvio;
import com.apostas.Gols;

public class Time {
	// JavascriptExecutor js; 

	private WebDriver driver;

	private String time = "Corinthians";

	private int contador = 0;

	private List<Gols> gol = new ArrayList<Gols>();

	private double somaGol = 0;

	private double mediaGol = 0;

	private int golsEstipulados = 0;

	private int tamanho = 0;

	private int tamanhoMenorQueDez = 0;

	private double varianciaAoQuadrado = 0;

	private List<Desvio> desvio = new ArrayList<Desvio>();

	@Before
	public void setUp() {

		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver/chromedriver");
		driver = new ChromeDriver();
		// js = (JavascriptExecutor) driver;

	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void adicionaGols() {
		// abre o navegador e faz a busca pelo time
		driver.get("https://fbref.com/pt/search/search.fcgi?search=" + time);
		String[] s = time.trim().split(" ");
		
//		for(String ss: s) {
//			System.out.println(s);
//		}
		if (driver.getCurrentUrl().endsWith(s[s.length-1])) {
			// isso é pras situações em que há mais de um opção de time pra escolher
			//o gatilho é ele ver se a url termina com o nome do time  
			//então ele pega primeira ocorrencia que aparecer o nome do time e clica
			System.out.println(s[s.length-1]);
//			String timeUpperCase = s[0].substring(0, 1).toUpperCase().concat(s[0].substring(1));
			//aqui era pra transformar a primeira letra em maiúscula
			driver.findElement(By.linkText(time)).click();
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
				// como eu preciso pegar os 10 ultimos resultados então foi preciso fazer um for
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
		double coeficienteDeVariacao = (desvioPadrao / mediaGol);
		System.out.println(gol);
		System.out.println(mediaGol);
		System.out.println(varianciaAoQuadrado);
		System.out.println(variancia);
		System.out.println(desvioPadrao);
		System.out.println("A variação é de " + coeficienteDeVariacao 
				+ " gols em relção à médiia de " + mediaGol + " gols");

	}
}
