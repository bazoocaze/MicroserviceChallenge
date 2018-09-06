package br.com.jasf.microservicechallenge.rabbit;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jasf.microservicechallenge.config.AppConfig;
import br.com.jasf.microservicechallenge.messages.UrlValidationResponse;
import br.com.jasf.microservicechallenge.services.rabbit.UrlValidationResponseSenderImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { AppConfig.class })
public class UrlValidationResponseSenderImplTest {

	@Autowired
	AppConfig appConfig;

	@MockBean
	RabbitTemplate rabbitTemplate;

	TopicExchange urlValidationExchange;
	UrlValidationResponseSenderImpl senderImpl;

	@Before
	public void runOnce() {
		urlValidationExchange = new TopicExchange(appConfig.getResponseExchange(), true, false);
		senderImpl = new UrlValidationResponseSenderImpl(appConfig, rabbitTemplate, urlValidationExchange);
	}

	@Test
	public void testSendResponse_ValidateInput() {

		assertThatIllegalArgumentException().isThrownBy(() -> senderImpl.sendResponse(null));

	}

	@Test
	public void testSendResponse_ValidateSend() {
		UrlValidationResponse response = new UrlValidationResponse();
		String responseExchange = appConfig.getResponseExchange();
		String responseroutingKey = appConfig.getResponseRoutingKey();

		BDDMockito.doNothing().when(rabbitTemplate).convertAndSend(responseExchange, responseroutingKey, response);

		senderImpl.sendResponse(response);

		// Garante que executa chamada rabbitTemplate.convertAndSend + parâmetros
		verify(rabbitTemplate, times(1)).convertAndSend(responseExchange, responseroutingKey, response);
	}

}
