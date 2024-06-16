package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.mytf.model.FnAppForm;
import com.mytf.service.AzureFnAppService;

@Controller
public class AzureFnAppController {

	private final AzureFnAppService azureFnAppService;

	public AzureFnAppController(AzureFnAppService azureFnAppService) {
		this.azureFnAppService = azureFnAppService;
	}
	
	@GetMapping("/fnAppForm")
	public String templateForm(Model model,@ModelAttribute FnAppForm fnAppForm) {
		model.addAttribute("fnAppForm", new FnAppForm(null, null, null, null, null, null, null, null, false));
		model.addAttribute("method", "post");
		return "fnAppForm";
	}
	
	@PostMapping("/fnAppService")
	public String createStructure(Model model,@ModelAttribute FnAppForm fnAppForm) {
		azureFnAppService.createFnAppTemplate(fnAppForm);
		model.addAttribute("message", "Function App Request has been submitted successfully!");
		return "azBoard";
	}
}
