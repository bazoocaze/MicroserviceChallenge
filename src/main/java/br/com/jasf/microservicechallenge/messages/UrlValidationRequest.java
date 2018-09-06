package br.com.jasf.microservicechallenge.messages;

/************************
 * Representa uma mensagem de requisição do serviço validation.
 * 
 * @author jose
 *
 */
public class UrlValidationRequest {

	private String client;
	private String url;
	private int correlationId;

	public String getClient() {
		return client;
	}

	public String getUrl() {
		return url;
	}

	public int getCorrelationId() {
		return correlationId;
	}

	public void setCliente(String cliente) {
		this.client = cliente;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return String.format("{ client=[%s], url=[%s], correlationId=[%s] }", client, url, correlationId);
	}

}
