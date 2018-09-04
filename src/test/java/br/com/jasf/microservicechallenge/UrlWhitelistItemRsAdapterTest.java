package br.com.jasf.microservicechallenge;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jasf.microservicechallenge.data.UrlWhitelistItem;
import br.com.jasf.microservicechallenge.data.UrlWhitelistItemRsAdapter;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { EmptyTestConfiguration.class })
public class UrlWhitelistItemRsAdapterTest {

	@Test
	public void testLoadTo() throws SQLException {
		UrlWhitelistItem item = new UrlWhitelistItem();
		ResultSet rs = mock(ResultSet.class);

		given(rs.getInt("id")).willReturn(10);
		given(rs.getString("client_id")).willReturn("123");
		given(rs.getString("test_regex")).willReturn("a.*");

		UrlWhitelistItemRsAdapter.loadTo(rs, item);

		// Garante que carregou o campo id
		assertEquals(item.getId(), 10);

		// Garante que carregou o campo client
		assertEquals(item.getClient(), "123");

		// Garante que carregou o campo regex
		assertEquals(item.getRegex(), "a.*");
	}

}
