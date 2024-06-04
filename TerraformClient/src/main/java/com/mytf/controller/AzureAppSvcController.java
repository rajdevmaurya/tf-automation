package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mytf.model.AppsvcForm;
import com.mytf.service.AzureAppService;

@Controller
public class AzureAppSvcController {

	private final AzureAppService azureAppService;

	public AzureAppSvcController(AzureAppService azureAppService) {
		this.azureAppService = azureAppService;
	}

	@GetMapping("/template")
	public String getTemplate() {
		return "template";
	}
	
	@GetMapping("/appSvcForm")
	public String templateForm(Model model,@ModelAttribute AppsvcForm structure) {
		model.addAttribute("appSvcForm", new AppsvcForm(null, null, null, null, null, null, null, true));
		model.addAttribute("method", "post");
		return "appSvcForm";
	}
	
	@PostMapping("/azAppService")
	public String createStructure(Model model,@ModelAttribute AppsvcForm appsvcForm) {
		azureAppService.createAzAppServiceTemplate(appsvcForm);
		return "redirect:/template";
	}
}
