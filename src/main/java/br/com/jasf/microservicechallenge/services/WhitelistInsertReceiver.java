package br.com.jasf.microservicechallenge.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.messages.WhitelistInsertRequest;
import br.com.jasf.microservicechallenge.utils.PreConditions;

/*********************
 * Recebimento e processamento de mensagens do serviço de inserção de regex na
 * whitelist.
 * 
 * @author jose
 *
 */
@Component
public class WhitelistInsertReceiver {

	private UrlWhitelistDAO urlWhitelistDAO;

	// ObjectMapper é thread-safe (menos em caso de alteração de configuração)
	// Fonte:
	// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
	private static final ObjectMapper objMapper = new ObjectMapper();

	private static final Log logger = LogFactory.getLog(WhitelistInsertReceiver.class);

	public WhitelistInsertReceiver(UrlWhitelistDAO urlWhitelistDAO) {
		this.urlWhitelistDAO = urlWhitelistDAO;
	}

	public void receiveMessage(byte[] message) {
		PreConditions.checkNotNull(message, "message");

		logger.info(String.format("Mensagem recebida: %d bytes", message.length));

		try {
			WhitelistInsertRequest request = objMapper.readValue(message, WhitelistInsertRequest.class);
			logger.info(String.format("Requisição recebida: %s", request));
			ProcessRequest(request);
		} catch (Exception ex) {
			logger.warn(String.format("Falha no processamento da requisição: %s", ex));
		}
	}

	private void ProcessRequest(WhitelistInsertRequest request) {
		PreConditions.checkNotNull(request, "request");

		if (request.getRegex() == null || request.getRegex().isEmpty()) {
			logger.warn(String.format("Requisição recebida inválida (regex): %s", request));
			return;
		}

		if (urlWhitelistDAO.insertRegex(request.getClient(), request.getRegex())) {
			logger.info("Regex cadastrada com sucesso");
		} else {
			logger.info("Regex já cadastrada");
		}
	}
}
