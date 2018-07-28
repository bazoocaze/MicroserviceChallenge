package br.com.jasf.microservicechallenge.data;

import br.com.jasf.microservicechallenge.utils.PreConditions;

/*********************
 * Representa um item da whitelist.
 * @author jose
 */
public class UrlWhitelistItem {
	private int id;
	private String clientId;
	private String regex;

	public String getClientId() {
		return clientId;
	}

	public String getRegex() {
		return regex;
	}

	public int getId() {
		return id;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setRegex(String regex) {
		PreConditions.checkString(regex, "regex");

		this.regex = regex;
	}

	public void setId(int id) {
		this.id = id;
	}
}
