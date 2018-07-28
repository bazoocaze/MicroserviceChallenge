package br.com.jasf.microservicechallenge.services;

import java.io.IOException;

import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.data.*;
import br.com.jasf.microservicechallenge.messages.*;
import br.com.jasf.microservicechallenge.utils.*;

/*********************
 * Recebimento e processamento de mensagens do serviço de inserção de regex na
 * whitelist.
 * 
 * @author jose
 *
 */
@Component
public class WhitelistInsertReceiver {
	@Autowired
	private UrlWhitelistDAO urlWhitelistDAO;

	// ObjectMapper é thread-safe (menos em caso de alteração de configuração)
	// Fonte:
	// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
	private static final ObjectMapper objMapper = new ObjectMapper();

	private static final Log logger = LogFactory.getLog(WhitelistInsertReceiver.class);

	public void receiveMessage(byte[] message) {
		PreConditions.checkNotNull(message, "message");

		logger.info(String.format("Mensagem recebida: %d bytes", message.length));

		try {
			WhitelistInsertRequest request = objMapper.readValue(message, WhitelistInsertRequest.class);
			logger.info(String.format("Requisição recebida: %s", request));
			ProcessRequest(request);
		} catch (IOException ex) {
			logger.warn(String.format("Falha no processamento da requisição: %s", ex));
		}
	}

	private void ProcessRequest(WhitelistInsertRequest request) {
		PreConditions.checkNotNull(request, "request");

		if (request.getRegex() == null || request.getRegex().isEmpty()) {
			logger.warn(String.format("Requisição recebida inválida: %s", request));
			return;
		}

		if (urlWhitelistDAO.insertRegex(request.getClientId(), request.getRegex())) {
			logger.info("Regex cadastrada com sucesso");
		} else {
			logger.info("Regex já cadastrada");
		}
	}
}
