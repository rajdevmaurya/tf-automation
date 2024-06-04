package com.mytf.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mytf.model.Authority;
import com.mytf.service.AuthorityService;

@Controller
public class AuthorityController {

private final AuthorityService authorityService;
	
	public AuthorityController(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	@GetMapping("/authority")
	public String getAuthorities(Model model) {
		List<Authority> authority = authorityService.getAuthorities();
		model.addAttribute("authorities", authority);
		return "authority";
	}
}
