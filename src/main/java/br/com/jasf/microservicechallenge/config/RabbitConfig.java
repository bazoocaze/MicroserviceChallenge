package br.com.jasf.microservicechallenge.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

import br.com.jasf.microservicechallenge.utils.Util;

@Configuration
public class RabbitConfig {
	@Autowired
	private AppConfig appConfig;

	@Bean
	public MessageConverter jsonMessageConverter() {
		System.err.println("RabbitConfig.jsonMessageConverter");
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	Queue urlValidationQueue() {
		System.err.println("RabbitConfig.validationQueue");
		return new Queue(appConfig.getValidationQueue(), true, false, false);
	}

	@Bean
	Queue whitelistInsertQueue() {
		System.err.println("RabbitConfig.insertQueue");
		return new Queue(appConfig.getInsertQueue(), true, false, false);
	}

	@Bean
	TopicExchange urlValidationExchange() {
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
		updateStrucutres(admin);

		return template;
	}

	private void updateStrucutres(RabbitAdmin admin) {
		int retries = 12;
		int delay = 5;
		Log logger = LogFactory.getLog(RabbitConfig.class);
		
		while(retries-- > 0)
		{
			try
			{
				declareStructures(logger, admin);
				// Operação OK
				return;				
			}
			catch (Exception ex) { 
				logger.warn(String.format("Não foi possível concluir a operação: %s\n", ex.getMessage()));
			}
			logger.warn(String.format("Tentando novamente em %d segundos\n", delay));
			Util.delay(delay * 1000);
		}
		
		// desiste depois das tentativas
		try
		{
			declareStructures(logger, admin);
		}
		catch (Exception ex) {
			logger.fatal(String.format("Impossível preparar o serviço Rabbit depois de várias tentantivas"), ex);
			throw ex;
		}
	}

	private void declareStructures(Log logger, RabbitAdmin admin) {
		logger.info("Atualizando a estrutura do serviço Rabbit");
		admin.declareQueue(whitelistInsertQueue());
		admin.declareQueue(urlValidationQueue());
		admin.declareExchange(urlValidationExchange());
		logger.info("Operação concluída");		
	}
}
