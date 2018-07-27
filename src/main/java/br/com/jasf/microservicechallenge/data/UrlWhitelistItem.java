package br.com.jasf.microservicechallenge.data;

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

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
