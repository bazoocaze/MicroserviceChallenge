package br.com.jasf.microservicechallenge.config;

import org.apache.commons.logging.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

import br.com.jasf.microservicechallenge.utils.Util;

/********************
 * Configuração do serviço Rabbit.
 * 
 * @author jose
 *
 */
@Configuration
public class RabbitConfig {
	@Autowired
	private AppConfig appConfig;

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	Queue urlValidationQueue() {
		return new Queue(appConfig.getValidationQueue(), true, false, false);
	}

	@Bean
	Queue whitelistInsertQueue() {
		return new Queue(appConfig.getInsertionQueue(), true, false, false);
	}

	@Bean
	TopicExchange urlValidationExchange() {
		return new TopicExchange(appConfig.getResponseExchange(), true, false);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setRoutingKey(appConfig.getResponseRoutingKey());
		template.setExchange(appConfig.getResponseExchange());
		template.setMessageConverter(jsonMessageConverter());

		RabbitAdmin admin = new RabbitAdmin(template);
		updateStrucutres(admin);

		return template;
	}

	/**************************
	 * 8 Prepara o serviço Rabbit para uso, criando filas e exchanges conforme
	 * necessário.
	 * 
	 * @param admin
	 */
	private void updateStrucutres(RabbitAdmin admin) {
		int retries = appConfig.getFailureRetries();
		int delay = appConfig.getFailureInterval();
		Log logger = LogFactory.getLog(RabbitConfig.class);

		while (retries-- > 0) {
			try {
				declareStructures(logger, admin);
				// Operação OK
				return;
			} catch (Exception ex) {
				logger.warn(String.format("Não foi possível concluir a operação: %s\n", ex.getMessage()));
			}
			logger.warn(String.format("Tentando novamente em %d segundos\n", delay));
			Util.delay(delay * 1000);
		}

		// desiste depois das tentativas
		try {
			declareStructures(logger, admin);
		} catch (Exception ex) {
			logger.fatal(String.format("Impossível preparar o serviço Rabbit depois de várias tentantivas"), ex);
			throw ex;
		}
	}

	/********************
	 * Tenta criar as queues e exchanges no serviço Rabbit.
	 * 
	 * @param logger
	 * @param admin
	 */
	private void declareStructures(Log logger, RabbitAdmin admin) {
		logger.info("Atualizando a estrutura do serviço Rabbit");
		admin.declareQueue(whitelistInsertQueue());
		admin.declareQueue(urlValidationQueue());
		admin.declareExchange(urlValidationExchange());
		logger.info("Operação concluída");
	}
}
