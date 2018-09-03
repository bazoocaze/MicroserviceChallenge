package br.com.jasf.microservicechallenge.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import br.com.jasf.microservicechallenge.config.AppConfig;

/********************
 * Serviço de inserção de regex na whitelist de url.
 * 
 * @author jose
 *
 */
@Service
public class WhitelistInsertService {

	private AppConfig appConfig;
	private RabbitTemplate rabbitTemplate;
	private Queue whitelistInsertQueue;

	public WhitelistInsertService(AppConfig appConfig, RabbitTemplate rabbitTemplate, Queue whitelistInsertQueue) {
		this.appConfig = appConfig;
		this.rabbitTemplate = rabbitTemplate;
		this.whitelistInsertQueue = whitelistInsertQueue;
	}

	@Bean
	SimpleMessageListenerContainer whitelistInsertContainer(WhitelistInsertReceiver receiver) {

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
		container.addQueues(whitelistInsertQueue);
		container.setMessageListener(new MessageListenerAdapter(receiver, "receiveMessage"));
		container.setConcurrentConsumers(appConfig.getNumberOfInsertionConsumers());

		return container;
	}
}
