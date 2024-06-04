package com.mytf.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuthRestClientInterceptor implements ClientHttpRequestInterceptor {
	
	private final OAuth2AuthorizedClientManager authorizedClientManager;
	private final ClientRegistration clientRegistration;
	
	public OAuthRestClientInterceptor(OAuth2AuthorizedClientManager authorizedClientManager, ClientRegistration clientRegistration) {
		this.authorizedClientManager = authorizedClientManager;
		this.clientRegistration = clientRegistration;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();	
		
		OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId(clientRegistration.getRegistrationId())
				.principal(authentication)
				.build();
		OAuth2AuthorizedClient client;
		try {
			client = authorizedClientManager.authorize(oAuth2AuthorizeRequest);
		} catch (Exception e) {
			throw new OAuth2AuthenticationException(e.getLocalizedMessage());
		}
		request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
		return execution.execute(request, body);
	}

}
