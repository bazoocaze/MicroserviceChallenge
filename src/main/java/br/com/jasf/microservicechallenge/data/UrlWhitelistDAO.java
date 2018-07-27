package br.com.jasf.microservicechallenge.data;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.*;
import org.springframework.util.Assert;

import br.com.jasf.microservicechallenge.utils.BooleanReference;
import br.com.jasf.microservicechallenge.utils.PreConditions;

@Component
public class UrlWhitelistDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertRegex(@Nullable String clientId, @NonNull String regex) throws DataAccessException {
		PreConditions.checkString(regex, "regex");

		String sql = "INSERT INTO url_whitelist (client_id, test_regex) VALUES (?, ?);";

		jdbcTemplate.update(sql, clientId, regex);
	}

	public boolean forEach(@Nullable String clientId, @NonNull Function<UrlWhitelistItem, Boolean> action) {
		PreConditions.checkNotNull(action, "action");

		Boolean found = false;
		UrlWhitelistItem item = new UrlWhitelistItem();
		String sql;

		ResultSetExtractor<Boolean> func = (rs) -> {
			while (rs.next()) {
				UrlWhitelistItemRsAdapter.loadTo(rs, item);
				if (!action.apply(item)) {
					return true;
				}
			}
			return false;
		};

		// busca da lista global
		sql = "SELECT * FROM url_whitelist WHERE client_id = NULL;";
		found = jdbcTemplate.query(sql, func);

		if (!found) {
			// busca da lista do cliente
			sql = "SELECT * FROM url_whitelist WHERE client_id = ?;";
			found = jdbcTemplate.query(sql, new Object[] { clientId }, func);
		}

		return found;
	}
}