package br.com.jasf.microservicechallenge.messages;


public class UrlValidationResponse {
	private boolean match;
	private String regex;
	private String correlationId;
	
	public UrlValidationResponse()
	{
		this.match = false;
		this.regex = null;
		this.correlationId = null;
	}

	public UrlValidationResponse(boolean match, String regex, String correlationId) {
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

	public String getCorrelationId() {
		return correlationId;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public String toString() {
		return String.format("{ match=[%s], correlatorioId=[%s], regex=[%s] }", match, correlationId, regex);
	}
}
