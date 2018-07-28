package br.com.jasf.microservicechallenge.messages;

import com.fasterxml.jackson.annotation.JsonSetter;

/************************
 * Representa uma mensagem de requisição do serviço validation.
 * 
 * @author jose
 *
 */
public class UrlValidationRequest {
	private String clienteId;
	private String url;
	private int correlationId;

	public String getClienteId() {
		return clienteId;
	}

	public String getUrl() {
		return url;
	}

	public int getCorrelationId() {
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
	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return String.format("{ clientId=[%s], url=[%s], correlationId=[%s] }", clienteId, url, correlationId);
	}
}
