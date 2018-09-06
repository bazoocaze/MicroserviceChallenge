package br.com.jasf.microservicechallenge.services;

import br.com.jasf.microservicechallenge.messages.UrlValidationResponse;

public interface UrlValidationResponseSender {

	void sendResponse(UrlValidationResponse response);
	
}
