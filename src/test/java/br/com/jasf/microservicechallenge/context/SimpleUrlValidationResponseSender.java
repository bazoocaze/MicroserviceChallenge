package br.com.jasf.microservicechallenge.context;

import org.springframework.stereotype.Component;

import br.com.jasf.microservicechallenge.messages.UrlValidationResponse;
import br.com.jasf.microservicechallenge.services.UrlValidationResponseSender;

@Component
public class SimpleUrlValidationResponseSender implements UrlValidationResponseSender {
	public UrlValidationResponse lastResponse = null;
	public int sendResponseCount = 0;

	@Override
	public void SendResponse(UrlValidationResponse response) {
		sendResponseCount++;
		lastResponse = response;
	}
}
