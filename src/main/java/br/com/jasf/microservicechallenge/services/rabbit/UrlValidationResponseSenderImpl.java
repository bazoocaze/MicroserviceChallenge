package br.com.jasf.microservicechallenge.services.rabbit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.jasf.microservicechallenge.config.AppConfig;
import br.com.jasf.microservicechallenge.messages.UrlValidationResponse;
import br.com.jasf.microservicechallenge.services.UrlValidationResponseSender;
import br.com.jasf.microservicechallenge.utils.PreConditions;

@Service
public class UrlValidationResponseSenderImpl implements UrlValidationResponseSender {

	private AppConfig appConfig;
	private RabbitTemplate rabbitTemplate;
	private TopicExchange urlValidationExchange;

	private static final Log logger = LogFactory.getLog(UrlValidationResponseSenderImpl.class);

	public UrlValidationResponseSenderImpl(AppConfig appConfig, RabbitTemplate rabbitTemplate,
			TopicExchange urlValidationExchange) {
		this.appConfig = appConfig;
		this.rabbitTemplate = rabbitTemplate;
		this.urlValidationExchange = urlValidationExchange;
	}

	@Override
	public void sendResponse(UrlValidationResponse response) {
		PreConditions.checkNotNull(response, "response");

		logger.info(String.format("Enviando resposta: %s", response));

		rabbitTemplate.convertAndSend(urlValidationExchange.getName(), appConfig.getResponseRoutingKey(), response);
	}

}
