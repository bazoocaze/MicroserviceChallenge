package br.com.jasf.microservicechallenge;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.config.ResourceConfig;
import br.com.jasf.microservicechallenge.context.SimpleUrlValidationResponseSender;
import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.data.UrlWhitelistDAOImpl;
import br.com.jasf.microservicechallenge.messages.UrlValidationRequest;
import br.com.jasf.microservicechallenge.services.UrlValidationReceiver;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { UrlWhitelistDAOImpl.class, UrlValidationReceiver.class,
		SimpleUrlValidationResponseSender.class, ResourceConfig.class })
@JdbcTest
public class UrlValidationReceiverTest {

	public static final String tableName = "url_whitelist";

	@Autowired
	ResourceConfig resourceConfig;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UrlWhitelistDAO urlWhitelistDAO;

	@Autowired
	SimpleUrlValidationResponseSender simpleSender;

	@Autowired
	UrlValidationReceiver receiver;

	@Before
	public void runOnce() throws DataAccessException, IOException {
		jdbcTemplate.execute(resourceConfig.getCreateTableResource());
	}

	public static ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	public void testReceiveMessage_validate_input() throws JsonProcessingException {

		// garante que recusa entrada null
		assertThatIllegalArgumentException().isThrownBy(() -> receiver.receiveMessage(null));

		// garante que não envia resposta para uma entrada json inválida
		assertFalse(testHasSentResponse(new byte[] { 0, 1 }));

		UrlValidationRequest request = new UrlValidationRequest();
		request.setCorrelationId(1);
		request.setUrl("http://www.test.com");

		// garante que rejeita request com cliente=null
		request.setCliente(null);
		assertFalse(testHasSentResponse(request));

		// garante que rejeita request com url=null
		request.setCliente("123");
		request.setUrl(null);
		assertFalse(testHasSentResponse(request));

		// garante que rejeita request com url=""
		request.setUrl("");
		assertFalse(testHasSentResponse(request));
	}

	@Test
	public void testReceiveMessage_validate_response() throws JsonProcessingException {

		UrlValidationRequest request = new UrlValidationRequest();
		request.setCliente("123");
		request.setCorrelationId(1000);
		request.setUrl("http://www.test.com");

		// garante que enviou uma resposta
		simpleSender.lastResponse = null;
		receiver.receiveMessage(jsonMapper.writeValueAsBytes(request));

		// Garante que enviou uma resposta (mesmo sem match)
		assertNotNull(simpleSender.lastResponse);

		// Garante que o CorrelationId é o mesmo da request
		assertEquals(simpleSender.lastResponse.getCorrelationId(), request.getCorrelationId());

		// Garante regex null quando não deu match
		assertNull(simpleSender.lastResponse.getRegex());

		// Garante que não deu match (whitelist vazia)
		assertFalse(simpleSender.lastResponse.isMatch());

		insertWhitelist("123", "a.*");

		// Garante match na lista do cliente
		assertTrue(isResponseMatch("123", "aaa"));

		// Garante que retorna a regex que deu match (lista do cliente)
		assertEquals(simpleSender.lastResponse.getRegex(), "a.*");

		// Garante não match
		assertFalse(isResponseMatch("123", "bbb"));

		// Garante não match / outro cliente
		assertFalse(isResponseMatch("111", "aaa"));

		// Garante não match
		assertFalse(isResponseMatch("111", "bbb"));

		insertWhitelist(null, "b.*");

		// Garante match na lista do cliente
		assertTrue(isResponseMatch("123", "aaa"));

		// Garante match via lista global
		assertTrue(isResponseMatch("123", "bbb"));

		// Garante que retorna a regex que deu match (lista global)
		assertEquals(simpleSender.lastResponse.getRegex(), "b.*");

		// Garante não match / outro cliente
		assertFalse(isResponseMatch("111", "aaa"));

		// Garante match via lista global
		assertTrue(isResponseMatch("111", "bbb"));

	}

	private boolean testHasSentResponse(byte[] message) {
		simpleSender.sendResponseCount = 0;
		receiver.receiveMessage(message);
		return (simpleSender.sendResponseCount == 1);
	}

	private boolean testHasSentResponse(UrlValidationRequest request) throws JsonProcessingException {
		simpleSender.sendResponseCount = 0;
		receiver.receiveMessage(jsonMapper.writeValueAsBytes(request));
		return (simpleSender.sendResponseCount == 1);
	}

	private boolean isResponseMatch(UrlValidationRequest request) throws JsonProcessingException {
		simpleSender.lastResponse = null;
		receiver.receiveMessage(jsonMapper.writeValueAsBytes(request));

		// Esta chamada espera que haja uma resposta
		assertNotNull(simpleSender.lastResponse);

		// Garante que o correlationId da response seja o mesmo da request
		assertEquals(request.getCorrelationId(), simpleSender.lastResponse.getCorrelationId());

		return simpleSender.lastResponse.isMatch();
	}

	private void insertWhitelist(String client, String regex) {
		urlWhitelistDAO.insertRegex(client, regex);
	}

	private boolean isResponseMatch(String client, String url) throws JsonProcessingException {
		UrlValidationRequest request = new UrlValidationRequest();
		request.setCliente(client);
		request.setCorrelationId(1212);
		request.setUrl(url);
		return isResponseMatch(request);
	}

}
