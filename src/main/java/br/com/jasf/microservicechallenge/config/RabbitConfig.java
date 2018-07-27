package br.com.jasf.microservicechallenge.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {
	@Autowired
	private AppConfig appConfig;

	@Bean
	public MessageConverter jsonMessageConverter() {
		System.err.println("RabbitConfig.jsonMessageConverter");
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean Queue urlValidationQueue()
	{
		System.err.println("RabbitConfig.validationQueue");
		return new Queue(appConfig.getValidationQueue(), true, false, false);
	}
	
	@Bean Queue whitelistInsertQueue()
	{
		System.err.println("RabbitConfig.insertQueue");
		return new Queue(appConfig.getInsertQueue(), true, false, false);		
	}
	
	@Bean TopicExchange urlValidationExchange()
	{
		System.err.println("RabbitConfig.validationExchange");
		return new TopicExchange(appConfig.getResponseExchange(), true, false);				
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		System.err.println("RabbitConfig.rabbitTemplate");
		
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setRoutingKey(appConfig.getResponseRoutingKey());
		template.setExchange(appConfig.getResponseExchange());
		template.setMessageConverter(jsonMessageConverter());
		
		RabbitAdmin admin = new RabbitAdmin(template);
		admin.declareQueue(whitelistInsertQueue());
		admin.declareQueue(urlValidationQueue());
		admin.declareExchange(urlValidationExchange());
		
		return template;
	}
}
