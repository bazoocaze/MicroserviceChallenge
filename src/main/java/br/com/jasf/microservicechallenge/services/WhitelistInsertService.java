package br.com.jasf.microservicechallenge.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class WhitelistInsertService {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Queue whitelistInsertQueue;

	@Bean WhitelistInsertService getInsertionService()
	{
		return new WhitelistInsertService();
	}

	@Bean
	SimpleMessageListenerContainer whitelistInsertContainer(WhitelistInsertReceiver receiver) {
		
		System.err.println("WhitelistInsertService.container BEBIN");
		
		Queue queue = whitelistInsertQueue;

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		container.setQueueNames(queue.getName());
		container.setMessageListener(new MessageListenerAdapter(receiver, "receiveMessage"));
		container.setConcurrentConsumers(5);

		System.err.println("WhitelistInsertService.container END");

		return container;
	}
}
