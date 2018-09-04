package br.com.jasf.microservicechallenge;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import org.springframework.test.jdbc.JdbcTestUtils;

import br.com.jasf.microservicechallenge.config.ResourceConfig;
import br.com.jasf.microservicechallenge.data.UrlWhitelistDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { UrlWhitelistDAOImpl.class, ResourceConfig.class })
@JdbcTest
public class UrlWhitelistDAOImplTest {

	public static final String tableName = "url_whitelist";

	@Autowired
	public ResourceConfig resourceConfig;

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public UrlWhitelistDAOImpl urlWhitelistDAOImpl;

	@Before
	public void runOnce() throws DataAccessException, IOException {
		jdbcTemplate.execute(resourceConfig.getCreateTableResource());
	}

	@Test
	public void testInsertRegex_count_rows() {
		assertEquals(0, countRowsInTable());

		// Garante que armazena
		urlWhitelistDAOImpl.insertRegex("123", "r.*");
		assertEquals(1, countRowsInTable());

		// Garante que não duplica
		urlWhitelistDAOImpl.insertRegex("123", "r.*");
		assertEquals(1, countRowsInTable());

		// Garante que permite várias regex por cliente
		urlWhitelistDAOImpl.insertRegex("123", "a.*");
		assertEquals(2, countRowsInTable());

		// Garante que armazena na lista global
		urlWhitelistDAOImpl.insertRegex(null, "r.*");
		assertEquals(3, countRowsInTable());

		// Garante que não duplica na lista global
		urlWhitelistDAOImpl.insertRegex(null, "r.*");
		assertEquals(3, countRowsInTable());

		// Garante que permite várias regex na lista global
		urlWhitelistDAOImpl.insertRegex(null, "a.*");
		assertEquals(4, countRowsInTable());

		// Garante que permite vários clientes
		urlWhitelistDAOImpl.insertRegex("111", "r.*");
		assertEquals(5, countRowsInTable());
	}

	@Test
	public void testInsertRegex_return_value() {
		assertEquals(0, countRowsInTable());

		// Garante que retorna true quando armazena
		assertEquals(true, urlWhitelistDAOImpl.insertRegex("123", "r.*"));

		// Garante que retorna false quando duplica
		assertEquals(false, urlWhitelistDAOImpl.insertRegex("123", "r.*"));
	}

	@Test
	public void testInsertRegex_validate_input() {
		assertEquals(0, countRowsInTable());

		// Garante que valida cliente=""
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> urlWhitelistDAOImpl.insertRegex("", "a.*"));

		// Garante que valida regex=""
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> urlWhitelistDAOImpl.insertRegex(null, ""));

		// Garante que valida regex=null
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> urlWhitelistDAOImpl.insertRegex(null, null));
	}

	@Test
	public void testInsertRegex_throw_dataAccess() {
		assertEquals(0, countRowsInTable());

		JdbcTestUtils.dropTables(jdbcTemplate, tableName);

		// Garante que gera DataAccessException pois a tabela foi removida
		assertThatExceptionOfType(DataAccessException.class)
				.isThrownBy(() -> urlWhitelistDAOImpl.insertRegex("123", "a.*"));
	}

	@Test
	public void testForEach_count_items() {
		assertEquals(0, countRowsInTable());

		urlWhitelistDAOImpl.insertRegex("123", "r.*");
		assertEquals(1, countForEach("123"));

		urlWhitelistDAOImpl.insertRegex("123", "a.*");
		urlWhitelistDAOImpl.insertRegex("111", "a.*");
		urlWhitelistDAOImpl.insertRegex(null, "a.*");

		// Garante a contagem de items do foreach conforme o número de entradas na
		// whitelist do cliente
		assertEquals(2, countForEach("123"));
		assertEquals(1, countForEach("111"));

		// Garante a contagem para whitelist global
		assertEquals(1, countForEach(null));

		// Garante contagem 0 para cliente inexistente
		assertEquals(0, countForEach("222"));
	}

	@Test
	public void testForEach_item_regex() {
		assertEquals(0, countRowsInTable());

		urlWhitelistDAOImpl.insertRegex("111", "a.*");
		urlWhitelistDAOImpl.insertRegex("123", "b.*");
		urlWhitelistDAOImpl.insertRegex("123", "c.*");
		urlWhitelistDAOImpl.insertRegex(null, "d.*");

		int[] index = new int[1];
		String[] ret = new String[3];
		index[0] = 0;

		urlWhitelistDAOImpl.forEach("123", item -> {
			// Garante que retorna o códiigo do cliente correto da lista do cliente
			assertEquals("123", item.getClient());
			ret[index[0]++] = item.getRegex();
			return false;
		});

		urlWhitelistDAOImpl.forEach(null, item -> {
			// Garante que retorno cliente==null da lista global
			assertNull(item.getClient());
			ret[index[0]++] = item.getRegex();
			return false;
		});

		// Garanta que retorna regex correta da lista do cliente
		assertEquals("b.*", ret[0]);
		assertEquals("c.*", ret[1]);

		// Garante que retorna regex correta da lista global
		assertEquals("d.*", ret[2]);
	}

	@Test
	public void testForEach_return_value() {
		assertEquals(0, countRowsInTable());

		urlWhitelistDAOImpl.insertRegex("123", "a.*");
		urlWhitelistDAOImpl.insertRegex("123", "b.*");
		urlWhitelistDAOImpl.insertRegex("123", "c.*");

		// Garante o valor de retorno true=lambda retornou true
		assertEquals(true, getReturnForEach("123", 0));
		assertEquals(true, getReturnForEach("123", 1));
		assertEquals(true, getReturnForEach("123", 2));

		// Garante o valor de retorno false=retornou todos os elementos e a lambda não
		// retornou false
		assertEquals(false, getReturnForEach("123", 3));
	}

	private int countForEach(String client) {
		int[] count = new int[1];
		count[0] = 0;
		urlWhitelistDAOImpl.forEach(client, item -> {
			count[0]++;
			return false;
		});
		return count[0];
	}

	private boolean getReturnForEach(String client, int countItems) {
		int[] count = new int[1];
		count[0] = 0;
		return urlWhitelistDAOImpl.forEach(client, item -> {
			if (count[0] == countItems)
				return true;
			count[0]++;
			return false;
		});
	}

	private int countRowsInTable() {
		return JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
	}

}
