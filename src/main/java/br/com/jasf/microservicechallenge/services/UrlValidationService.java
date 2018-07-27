package br.com.jasf.microservicechallenge.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import br.com.jasf.microservicechallenge.config.AppConfig;

@Service
public class UrlValidationService {
	@Autowired
	private AppConfig appConfig;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Queue urlValidationQueue;

	@Bean
	SimpleMessageListenerContainer urlValidationContainer(UrlValidationReceiver receiver) {

		System.err.println("UrlValidationService.container BEBIN");
		
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		container.addQueues(urlValidationQueue);
		container.setMessageListener(new MessageListenerAdapter(receiver, "receiveMessage"));
		container.setConcurrentConsumers(appConfig.getNumberOfValidationConsumers());

		System.err.println("UrlValidationService.container END");

		return container;
	}
}
