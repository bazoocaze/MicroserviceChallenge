package br.com.jasf.microservicechallenge.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import br.com.jasf.microservicechallenge.config.AppConfig;

/*****************
 * Serviço de validação de url a partir da whitelist.
 * 
 * @author jose
 *
 */
@Service
public class UrlValidationService {

	private AppConfig appConfig;
	private RabbitTemplate rabbitTemplate;
	private Queue urlValidationQueue;

	public UrlValidationService(AppConfig appConfig, RabbitTemplate rabbitTemplate, Queue urlValidationQueue) {
		this.appConfig = appConfig;
		this.rabbitTemplate = rabbitTemplate;
		this.urlValidationQueue = urlValidationQueue;
	}

	@Bean
	SimpleMessageListenerContainer urlValidationContainer(UrlValidationReceiver receiver) {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		container.addQueues(urlValidationQueue);
		container.setMessageListener(new MessageListenerAdapter(receiver, "receiveMessage"));
		container.setConcurrentConsumers(appConfig.getNumberOfValidationConsumers());

		return container;
	}
}
