package br.com.jasf.microservicechallenge.data;

import br.com.jasf.microservicechallenge.utils.PreConditions;

/*********************
 * Representa um item da whitelist.
 * @author jose
 */
public class UrlWhitelistItem {
	private int id;
	private String client;
	private String regex;

	public String getClient() {
		return client;
	}

	public String getRegex() {
		return regex;
	}

	public int getId() {
		return id;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public void setRegex(String regex) {
		PreConditions.checkString(regex, "regex");

		this.regex = regex;
	}

	public void setId(int id) {
		this.id = id;
	}
}
