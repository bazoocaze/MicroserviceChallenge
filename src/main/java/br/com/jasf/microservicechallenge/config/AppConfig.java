package br.com.jasf.microservicechallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	private String insertQueue;
	private String validationQueue;	
	private int numberOfValidationConsumers;
	private String responseExchange;
	private String responseRoutingKey;
	private String jdbcUrl;
	private int numberOfInsertConsumers;

	public AppConfig() {
		System.err.println("AppConfig.ctor");
		
		// TODO: acertar a origem das configurações
		insertQueue = "teste.insert";
		validationQueue = "teste.validation";
		numberOfValidationConsumers = 3;
		responseExchange = "my_teste_response";
		responseRoutingKey = "response.routing.key";
		jdbcUrl = "";
		numberOfInsertConsumers = 3;
	}
	
	@Bean
	public AppConfig appConfig1()
	{
		System.err.println("AppConfig.appConfig()");

		return new AppConfig();
	}
	
	public String getInsertQueue() {
		return insertQueue;
	}

	public String getValidationQueue() {
		return validationQueue;
	}

	public int getNumberOfValidationConsumers() {
		return numberOfValidationConsumers;
	}

	public String getResponseExchange() {
		return responseExchange;
	}

	public String getResponseRoutingKey() {
		return responseRoutingKey;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public int getNumberOfInsertConsumers() {
		return numberOfInsertConsumers;
	}
}
