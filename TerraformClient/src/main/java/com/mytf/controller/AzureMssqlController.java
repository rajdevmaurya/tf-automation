package com.mytf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.mytf.model.MssqlForm;
import com.mytf.service.AzureMssqlService;

@Controller
public class AzureMssqlController {

	private final AzureMssqlService azureMssqlService;

	public AzureMssqlController(AzureMssqlService azureMssqlService) {
		this.azureMssqlService = azureMssqlService;
	}
	
	@GetMapping("/mssqlForm")
	public String templateForm(Model model,@ModelAttribute MssqlForm mssqlForm) {
		model.addAttribute("mssqlForm", new MssqlForm(null, null, null, null, null, null, null, null, null, null, null, false));
		model.addAttribute("method", "post");
		return "mssqlForm";
	}
	
	@PostMapping("/mssqlService")
	public String createStructure(Model model,@ModelAttribute MssqlForm mssqlForm) {
		azureMssqlService.createMssqlTemplate(mssqlForm);
		model.addAttribute("message", "Create Azure MSSQL Service Request has been submitted successfully!");
		return "azBoard";
	}
}
