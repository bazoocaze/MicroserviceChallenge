package br.com.jasf.microservicechallenge;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jasf.microservicechallenge.config.ResourceConfig;
import br.com.jasf.microservicechallenge.data.UrlWhitelistItem;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { EmptyTestConfiguration.class })
public class UrlWhitelistItemTest {

	@Test
	public void testSetRegex_validate_input() {
		UrlWhitelistItem item = new UrlWhitelistItem();

		// Garante que setRegex rejeite a entrada ""
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> item.setRegex(""));

		// Garante que setRegex rejeite a entrada null
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> item.setRegex(null));
	}

}
