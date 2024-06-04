package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mytf.model.Structure;
import com.mytf.service.TempLateGenService;

@Controller
public class TempLateController {

	private final TempLateGenService tempLateGenService;

	public TempLateController(TempLateGenService tempLateGenService) {
		this.tempLateGenService = tempLateGenService;
	}

	@GetMapping("/template")
	public String getTemplate() {
		return "template";
	}
	
	@GetMapping("/structure")
	public String templateForm(Model model,@ModelAttribute Structure structure) {
		model.addAttribute("structure", new Structure(null, null, null, null, null, null, null, true));
		model.addAttribute("method", "post");
		return "structure";
	}
	
	@PostMapping("/createStructure")
	public String createStructure(Model model,@ModelAttribute Structure structure) {
		tempLateGenService.genrateTerraform(structure);
		return "redirect:/template";
	}
}
