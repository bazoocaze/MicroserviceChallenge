package br.com.jasf.microservicechallenge.services;

import java.io.IOException;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.*;

import br.com.jasf.microservicechallenge.config.AppConfig;
import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.messages.*;

@Component
public class UrlValidationReceiver {
	@Autowired
	private AppConfig appConfig;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private TopicExchange urlValidationExchange;

	@Autowired
	private UrlWhitelistDAO urlWhitelistDAO;

	// ObjectMapper é thread-safe (menos em caso de alteração de configuração)
	// Fonte:
	// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
	private static final ObjectMapper objMapper = new ObjectMapper();

	public void receiveMessage(byte[] message) {
		Assert.notNull(message, String.format("Argument 'message' cannot be null"));

		try {
			UrlValidationRequest request = objMapper.readValue(message, UrlValidationRequest.class);
			ProcessRequest(request);

		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return;
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex1) {
			// TODO Auto-generated catch block
			ex1.printStackTrace();
		}

		System.out.println("UrlValidationReceive.Received[byte] END");
	}

	private void ProcessRequest(UrlValidationRequest request) {

		urlWhitelistDAO.forEach(request.getClienteId(), (item) -> {
			if (request.getUrl().matches(item.getRegex())) {
				// Encontrou um match
				UrlValidationResponse response = new UrlValidationResponse(true, item.getRegex(),
						request.getCorrelationId());
				SendResponse(response);
				return true;
			}
			return false;
		});

		// Não encontrou
		UrlValidationResponse response = new UrlValidationResponse(false, null, request.getCorrelationId());
		SendResponse(response);
	}

	private void SendResponse(UrlValidationResponse response) {
		rabbitTemplate.convertAndSend(urlValidationExchange.getName(), appConfig.getResponseRoutingKey(), response);
	}
}
