package br.com.jasf.microservicechallenge.messages;

/*******************
 * Representa uma mensagem de resposta do serviço validation.
 * 
 * @author jose
 *
 */
public class UrlValidationResponse {
	private boolean match;
	private String regex;
	private int correlationId;

	public UrlValidationResponse() {
		this.match = false;
		this.regex = null;
		this.correlationId = 0;
	}

	public UrlValidationResponse(boolean match, String regex, int correlationId) {
		this.match = match;
		this.regex = regex;
		this.correlationId = correlationId;
	}

	public boolean isMatch() {
		return match;
	}

	public String getRegex() {
		return regex;
	}

	public int getCorrelationId() {
		return correlationId;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return String.format("{ match=[%s], regex=[%s], correlatorioId=[%s] }", match, regex, correlationId);
	}
}
