package br.com.jasf.microservicechallenge.messages;

import com.fasterxml.jackson.annotation.JsonSetter;

/******************
 * Representa uma mensagem de requisição do serviço insert.
 * 
 * @author jose
 *
 */
public class WhitelistInsertRequest {
	private String clientId;
	private String regex;

	public String getClientId() {
		return clientId;
	}

	public String getRegex() {
		return regex;
	}

	@JsonSetter("client_id")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String toString() {
		return String.format("{ clientId=[%s], regex=[%s] }", clientId, regex);
	}
}
