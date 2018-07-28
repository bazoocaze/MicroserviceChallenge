package br.com.jasf.microservicechallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/*******************************
 * Contém os principais itens de configuração geral do programa.
 * 
 * @author jose
 *
 */
@Configuration
public class AppConfig {
	@Value("${br.com.jasf.microservicechallenge.insertion-queue}")
	private String insertionQueue;

	@Value("${br.com.jasf.microservicechallenge.validation-queue}")
	private String validationQueue;

	@Value("${br.com.jasf.microservicechallenge.validation-consumers}")
	private int numberOfValidationConsumers;

	@Value("${br.com.jasf.microservicechallenge.exchange}")
	private String responseExchange;

	@Value("${br.com.jasf.microservicechallenge.routing-key}")
	private String responseRoutingKey;

	@Value("${br.com.jasf.microservicechallenge.insertion-consumers}")
	private int numberOfInsertConsumers;

	@Value("${br.com.jasf.microservicechallenge.failure.num-retries}")
	private int failureRetries;

	@Value("${br.com.jasf.microservicechallenge.failure.interval}")
	private int failureInterval;

	/**************
	 * @return Nome da fila insertion
	 */
	public String getInsertionQueue() {
		return insertionQueue;
	}

	/**************
	 * @return Nome da fila validation
	 */
	public String getValidationQueue() {
		return validationQueue;
	}

	/**************
	 * @return Número e consumidores paralelos na fila validation
	 */
	public int getNumberOfValidationConsumers() {
		return numberOfValidationConsumers;
	}

	/**************
	 * @return Nome do exchange de resposta da fila de validation
	 */
	public String getResponseExchange() {
		return responseExchange;
	}

	/**************
	 * @return Routing key para o exchange de resposta da fila de validation
	 */
	public String getResponseRoutingKey() {
		return responseRoutingKey;
	}

	/**************
	 * @return Número de consumidores paralelos na fila de insertion
	 */
	public int getNumberOfInsertionConsumers() {
		return numberOfInsertConsumers;
	}

	/**************
	 * @return Número de tentativas em caso de falha de conexão inicial (Rabbit e
	 *         Database)
	 */
	public int getFailureRetries() {
		return failureRetries;
	}

	/**************
	 * @return Intervalo entre tentativas em caso de falha de conexão inicial
	 *         (Rabbit e Database)
	 */
	public int getFailureInterval() {
		return failureInterval;
	}
}
