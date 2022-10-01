package com.apostas.controller;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.apostas.model.DesvioPadrao;
import com.apostas.model.MediaGol;
import com.apostas.model.MediaGolCasa;
import com.apostas.model.MediaGolFora;
import com.apostas.model.MediaGolsTomados;
import com.apostas.model.PesquisaTime;

@Controller
public class ApostaController {

	List<PesquisaTime> pesqTimes = new ArrayList<>();
	
	List<PesquisaTime> cv = new ArrayList<>();
	
	List<PesquisaTime> desvio = new ArrayList<>();
	
	List<PesquisaTime> media = new ArrayList<>();
	
	List<PesquisaTime> mediaGolCasa = new ArrayList<>();
	
	List<PesquisaTime> mediaGolFora = new ArrayList<>();
	
	List<PesquisaTime> mediaTomados = new ArrayList<>();
	
	List<PesquisaTime> jogosAnalisados = new ArrayList<>();
	
	@GetMapping("/add-erro")
	public String adicionaAposErro() {

		return "add-erro";
	}
	
	@GetMapping("/add")
	public String adicionaNome() {

		return "add";
	}

	@PostMapping("/add")
	public String add(PesquisaTime time) {
		System.out.println("está funcionando add " + time.getNomeTime());
		
		if(! time.getNomeTime().isEmpty()) {
			//isso é pra não rodar a página logo que carrega, pra dar tempo de colocar o nome do time
			pesqTimes.add(new PesquisaTime(time.getNomeTime()));
			
			time.setUp();

			try {
				time.adicionaGols();
				
				cv.add(new PesquisaTime(time.getCoeficienteDeVariacao()));
				desvio.add((PesquisaTime) new DesvioPadrao(time.getDesvioPadrao()));
				media.add((PesquisaTime) new MediaGol(time.getMediaGol()));
				mediaGolCasa.add((PesquisaTime) new MediaGolCasa(time.getMediaGolCasa()));
				mediaGolFora.add((PesquisaTime) new MediaGolFora(time.getMediaGolFora()));
				mediaTomados.add((PesquisaTime) new MediaGolsTomados(time.getMediaGolsTomados()));
				jogosAnalisados.add(new PesquisaTime(time.getJogosAnalisados()));
			} catch (NoSuchElementException e) {
				//isso é pro nome inválido
				System.out.println("deu erro aqui" + e);
				time.tearDown();
				pesqTimes.remove(pesqTimes.size()-1);
//				jogosAnalisados.remove(jogosAnalisados.size()-1);
				return "redirect:/add-erro";
			}
			
			time.tearDown();
		}
		
		return "redirect:/resposta";
	}

	@GetMapping("/resposta")
	public ModelAndView resposta(PesquisaTime time) {

		ModelAndView mv = new ModelAndView("resposta");

		mv.addObject("pesqTimes", pesqTimes);
		
		mv.addObject("cv", cv);
		
		mv.addObject("desvio", desvio);
		
		mv.addObject("media", media);
		
		mv.addObject("mediaGolCasa", mediaGolCasa);
		
		mv.addObject("mediaGolFora", mediaGolFora);
		
		mv.addObject("mediaTomados", mediaTomados);
		
		mv.addObject("jogosAnalisados", jogosAnalisados);
		
		return mv;
	}
	
	@GetMapping("/voltar")
	public String voltar() {
		
		pesqTimes.removeAll(pesqTimes);
		cv.removeAll(cv);
		desvio.removeAll(desvio);
		media.removeAll(media);
		mediaGolCasa.removeAll(mediaGolCasa);
		mediaGolFora.removeAll(mediaGolFora);
		mediaTomados.removeAll(mediaTomados);
		jogosAnalisados.removeAll(jogosAnalisados);
		return "redirect:/add";
	}
}
