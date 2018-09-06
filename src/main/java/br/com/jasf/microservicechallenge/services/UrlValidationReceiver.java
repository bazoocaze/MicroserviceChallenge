package br.com.jasf.microservicechallenge.services;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.config.AppConfig;
import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.data.UrlWhitelistItem;
import br.com.jasf.microservicechallenge.messages.UrlValidationRequest;
import br.com.jasf.microservicechallenge.messages.UrlValidationResponse;
import br.com.jasf.microservicechallenge.utils.PreConditions;
	
@Component
public class UrlValidationReceiver {

	private UrlWhitelistDAO urlWhitelistDAO;
	private UrlValidationResponseSender urlValidationResponseSender;

	public UrlValidationReceiver(UrlWhitelistDAO urlWhitelistDAO, UrlValidationResponseSender urlValidationResponseSender) {
		this.urlWhitelistDAO = urlWhitelistDAO;
		this.urlValidationResponseSender = urlValidationResponseSender;
	}

	// ObjectMapper é thread-safe (menos em caso de alteração de configuração)
	// Fonte:
	// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
	private static final ObjectMapper objMapper = new ObjectMapper();

	private static final Log logger = LogFactory.getLog(UrlValidationReceiver.class);

	public void receiveMessage(byte[] message) {
		PreConditions.checkNotNull(message, "message");

		logger.info(String.format("Mensagem recebida: %d bytes", message.length));

		try {
			UrlValidationRequest request = objMapper.readValue(message, UrlValidationRequest.class);

			logger.info(String.format("Requisição recebida: %s", request));

			ProcessRequest(request);
		} catch (Exception ex) {
			logger.warn(String.format("Falha no processamento da requisição: %s", ex));
		}
	}

	private void ProcessRequest(UrlValidationRequest request) {
		PreConditions.checkNotNull(request, "request");

		if (request.getClient() == null) {
			logger.warn(String.format("Requisição recebida inválida (client): %s", request));
			return;
		}

		if (request.getUrl() == null || request.getUrl().isEmpty()) {
			logger.warn(String.format("Requisição recebida inválida (url): %s", request));
			return;
		}

		Function<UrlWhitelistItem, Boolean> func = (item) -> {
			if (request.getUrl().matches(item.getRegex())) {
				// Encontrou um match
				UrlValidationResponse response = new UrlValidationResponse(true, item.getRegex(),
						request.getCorrelationId());
				urlValidationResponseSender.SendResponse(response);
				return true;
			}
			return false;
		};

		if (request.getClient() != null) {
			if (urlWhitelistDAO.forEach(request.getClient(), func)) {
				return;
			}
		}

		if (urlWhitelistDAO.forEach(null, func)) {
			return;
		}

		// Não encontrou
		UrlValidationResponse response = new UrlValidationResponse(false, null, request.getCorrelationId());
		urlValidationResponseSender.SendResponse(response);
	}

}
