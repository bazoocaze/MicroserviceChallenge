package br.com.jasf.microservicechallenge.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.messages.WhitelistInsertRequest;
import br.com.jasf.microservicechallenge.utils.PreConditions;

@Component
public class WhitelistInsertReceiver {
	@Autowired
	private UrlWhitelistDAO urlWhitelistDAO;

	private static final ObjectMapper objMapper = new ObjectMapper();

	public void receiveMessage(String message) {
		System.out.println("WhitelistInsertionReceiver.Received <" + message + ">");
	}

	public void receiveMessage(byte[] message) {
		System.out.println("WhitelistInsertionReceiver.Received[byte] BEGIN");

		try {
			WhitelistInsertRequest request = objMapper.readValue(message, WhitelistInsertRequest.class);
			ProcessRequest(request);
		} catch (IOException ex1) {
			// TODO Auto-generated catch block
			ex1.printStackTrace();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		System.out.println("WhitelistInsertionReceiver.Received[byte] END");
	}

	private void ProcessRequest(WhitelistInsertRequest request) {
		PreConditions.checkNotNull(request, "request");

		if (request.getRegex() == null || request.getRegex().isEmpty()) {
			// TODO: mensagem de falha
			return;
		}

		urlWhitelistDAO.insertRegex(request.getClientId(), request.getRegex());
	}
}
