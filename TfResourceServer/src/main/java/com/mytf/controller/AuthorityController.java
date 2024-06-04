package com.mytf.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytf.model.Authority;
import com.mytf.service.AuthorityService;

@RestController
public class AuthorityController {

	private final AuthorityService authorityService;

	public AuthorityController(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	
	@GetMapping("/authority")
	public List<Authority> getAllAuthorities() {
		return authorityService.findAllAuthorities();
	}
}
