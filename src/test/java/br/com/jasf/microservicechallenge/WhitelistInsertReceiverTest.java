package br.com.jasf.microservicechallenge;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;
import br.com.jasf.microservicechallenge.messages.WhitelistInsertRequest;
import br.com.jasf.microservicechallenge.services.WhitelistInsertReceiver;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { WhitelistInsertReceiver.class })
@JdbcTest
public class WhitelistInsertReceiverTest {

	@MockBean
	UrlWhitelistDAO urlWhitelistDAO;

	@Autowired
	WhitelistInsertReceiver whitelistInsertReceiver;

	private static ObjectMapper jsonMapper = new ObjectMapper();

	@Test(expected = IllegalArgumentException.class)
	public void testReceiveMessage_ValidateNullInput() {
		// Garante que throw a entrada null
		whitelistInsertReceiver.receiveMessage(null);
	}

	@Test
	public void testReceiveMessage_ValidateInput() throws JsonProcessingException {
		// Garante que recusa mensagem json inválida
		whitelistInsertReceiver.receiveMessage(new byte[] { 0, 1 });
		verify(urlWhitelistDAO, times(0)).insertRegex(anyString(), anyString());

		// Garante que recusa cliente=""
		callReceiveMessage("", "a.*");
		verify(urlWhitelistDAO, times(0)).insertRegex(anyString(), anyString());

		// Garante que recusa regex=""
		callReceiveMessage("123", "");
		verify(urlWhitelistDAO, times(0)).insertRegex(anyString(), BDDMockito.anyString());

		// Garante que recusa regex=null
		callReceiveMessage("123", "");
		verify(urlWhitelistDAO, times(0)).insertRegex(anyString(), anyString());

		// Garante que aceita para cliente normal
		callReceiveMessage("123", "a.*");
		verify(urlWhitelistDAO, times(1)).insertRegex("123", "a.*");

		// Garante que aceita para cliente=null (whitelist global)
		callReceiveMessage(null, "b.*");
		verify(urlWhitelistDAO, times(1)).insertRegex(null, "b.*");
	}

	private void callReceiveMessage(WhitelistInsertRequest request) throws JsonProcessingException {
		whitelistInsertReceiver.receiveMessage(jsonMapper.writeValueAsBytes(request));
	}

	private void callReceiveMessage(String client, String regex) throws JsonProcessingException {
		WhitelistInsertRequest request = new WhitelistInsertRequest();
		request.setClient(client);
		request.setRegex(regex);
		callReceiveMessage(request);
	}

}
