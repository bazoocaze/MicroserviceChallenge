package br.com.jasf.microservicechallenge.messages;

import com.fasterxml.jackson.annotation.JsonSetter;

public class UrlValidationRequest {
	private String clienteId;
	private String url;
	private String correlationId;

	public String getClienteId() {
		return clienteId;
	}

	public String getUrl() {
		return url;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	@JsonSetter("client_id")
	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonSetter("correlation_id")
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	@Override
	public String toString() {
		return String.format("{ clientId=[%s], correlationId=[%s], url=[%s] }",
				clienteId, correlationId, url);
	}
}
