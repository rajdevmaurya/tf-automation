package com.mytf.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import com.mytf.model.MssqlForm;
import com.mytf.service.AzureMssqlService;

@Controller
public class FormController {
	
	private final AzureMssqlService azureMssqlService;

	public FormController(AzureMssqlService azureMssqlService) {
		this.azureMssqlService = azureMssqlService;
	}

	@GetMapping("/mssqlFormStep1")
	public String showStep1(Model model) {
		if (!model.containsAttribute("mssqlForm")) {
			model.addAttribute("mssqlForm", new MssqlForm(null, null, null, null, null, null, null, null, null, null, null, null));
		}
		return "mssqlFormStep1";
	}

	@PostMapping("/mssqlFormStep1")
	public String processStep1(Model model, @ModelAttribute("mssqlForm") MssqlForm mssqlForm, BindingResult result,@RequestParam(value = "action", required = true) String action) {
		if (result.hasErrors()) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep1";
		}
		if ("next".equals(action)) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep2";
		}
		return "mssqlFormStep1";
	}

	@GetMapping("/mssqlFormStep2")
	public String showStep2(Model model) {
		if (!model.containsAttribute("mssqlForm")) {
			// model.addAttribute("mssqlForm", createForm());
		}
		return "mssqlFormStep2";
	}

	@PostMapping("/mssqlFormStep2")
	public String processStep2(Model model,@ModelAttribute("mssqlForm") MssqlForm mssqlForm, BindingResult result,@RequestParam(value = "action") String action) {
		if (result.hasErrors()) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep2";
		}
		if ("previous".equals(action)) {
			return "redirect:/mssqlFormStep1";
		} else if ("next".equals(action)) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep3";
		}
		return "mssqlFormStep2";
	}

	@GetMapping("/mssqlFormStep3")
	public String showStep3(Model model, @ModelAttribute("mssqlForm") MssqlForm mssqlForm) {
		if (!model.containsAttribute("mssqlForm")) {
			model.addAttribute("mssqlForm", mssqlForm);
		}
		return "mssqlFormStep3";
	}

	@PostMapping("/mssqlFormStep3")
	public String processStep3(Model model, @ModelAttribute("mssqlForm") MssqlForm mssqlForm, BindingResult result,
			@RequestParam(value = "action", required = true) String action, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep3";
		}
		if ("previous".equals(action)) {
			model.addAttribute("mssqlForm", mssqlForm);
			return "mssqlFormStep2";
		} else if ("submit".equals(action)) {
			System.out.println(mssqlForm);
			azureMssqlService.createMssqlTemplate(mssqlForm);
			status.setComplete(); 
			return "redirect:/azBoard";
		}
		return "mssqlFormStep3";
	}
}
