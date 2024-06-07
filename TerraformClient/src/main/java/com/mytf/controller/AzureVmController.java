package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.mytf.model.VmForm;
import com.mytf.service.AzureVmService;

@Controller
public class AzureVmController {

	private final AzureVmService azureVmService;

	public AzureVmController(AzureVmService azureVmService) {
		this.azureVmService = azureVmService;
	}
	
	@GetMapping("/vmForm")
	public String templateForm(Model model,@ModelAttribute VmForm vmForm) {
		model.addAttribute("vmForm", new VmForm(null, null, null, null, null, null, null, false));
		model.addAttribute("method", "post");
		return "vmForm";
	}
	
	@PostMapping("/azVmService")
	public String createStructure(Model model,@ModelAttribute VmForm vmForm) {
		azureVmService.createAzVmTemplate(vmForm);
		return "redirect:/azBoard";
	}
}
