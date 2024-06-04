package com.mytf.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytf.model.Role;
import com.mytf.service.RoleService;

@RestController
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping("/role")
	public List<Role> getAllRoles() {
		return roleService.findAllRoles();
	}
}
