package com.mytf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mytf.client.UserClient;
import com.mytf.model.Authority;

@Service
public class AuthorityService {

private final UserClient userClient;
	
	public AuthorityService(UserClient userClient) {
		this.userClient = userClient;
	}

	public List<Authority> getAuthorities() {
		return this.userClient.getAuthorities();
	}
}
