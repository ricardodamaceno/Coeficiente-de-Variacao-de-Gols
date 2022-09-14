package com.apostas.controller;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.apostas.model.CoeficienteDeVariacao;
import com.apostas.model.MediaGol;
import com.apostas.model.PesquisaTime;
import com.apostas.model.PorcentagemDeVariacao;

@Controller
public class ApostaController {

	List<PesquisaTime> pesqTimes = new ArrayList<>();
	
	List<PesquisaTime> cv = new ArrayList<>();
	
	List<PesquisaTime> porcentagem = new ArrayList<>();
	
	List<PesquisaTime> media = new ArrayList<>();
	
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
				
				cv.add((PesquisaTime) new CoeficienteDeVariacao(time.getCoeficienteDeVariacao()));
				porcentagem.add((PesquisaTime) new PorcentagemDeVariacao(time.getPorcentagemDeVariacao()));
				media.add((PesquisaTime) new MediaGol(time.getMediaGol()));
			} catch (NoSuchElementException e) {
				System.out.println("deu erro aqui" + e);
				time.tearDown();
				pesqTimes.remove(pesqTimes.size()-1);
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
		
		mv.addObject("porcentagem", porcentagem);
		
		mv.addObject("media", media);
		
		return mv;
	}
	
	@GetMapping("/voltar")
	public String voltar() {
		
		pesqTimes.removeAll(pesqTimes);
		cv.removeAll(cv);
		porcentagem.removeAll(porcentagem);
		media.removeAll(media);
		return "redirect:/add";
	}
}
