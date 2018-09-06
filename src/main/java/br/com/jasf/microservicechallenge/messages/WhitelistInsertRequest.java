package br.com.jasf.microservicechallenge.messages;

/******************
 * Representa uma mensagem de requisição do serviço insert.
 * 
 * @author jose
 *
 */
public class WhitelistInsertRequest {

	private String client;
	private String regex;

	public String getClient() {
		return client;
	}

	public String getRegex() {
		return regex;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String toString() {
		return String.format("{ client=[%s], regex=[%s] }", client, regex);
	}

}
