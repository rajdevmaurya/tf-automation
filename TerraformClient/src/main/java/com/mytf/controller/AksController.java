package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.mytf.model.AksForm;
import com.mytf.service.AksService;

@Controller
public class AksController {

	private final AksService aksService;

	public AksController(AksService aksService) {
		this.aksService = aksService;
	}
	
	@GetMapping("/aksForm")
	public String templateForm(Model model,@ModelAttribute AksForm aksForm) {
		model.addAttribute("aksForm", new AksForm(null, null, null, null, null, null, null, true));
		model.addAttribute("method", "post");
		return "aksForm";
	}
	
	@PostMapping("/aksService")
	public String createStructure(Model model,@ModelAttribute AksForm aksForm) {
		aksService.createAksTemplate(aksForm);
		return "redirect:/template";
	}
}
