package br.com.jasf.microservicechallenge.data;

import java.util.function.Function;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import br.com.jasf.microservicechallenge.utils.PreConditions;

/********************
 * Implementação da interface de acesso a whitelist no banco de dados.
 * 
 * @author jose
 *
 */
@Component
public class UrlWhitelistDAOImpl implements UrlWhitelistDAO {

	private JdbcTemplate jdbcTemplate;

	public UrlWhitelistDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*****************************
	 * Insere o regex na whitelist para o cliente informado (null para lista
	 * global). Não duplica caso já exista a regex para o cliente informado.
	 * 
	 * @param client Identificação do cliente (ou null para lista global)
	 * @param regex  Regex a ser inserida (ignora caso regex já cadastrada).
	 * @return True/false se a regex foi inserida.
	 * @throws DataAccessException Em caso de erro de sql/banco de dados
	 */
	@Override
	public boolean insertRegex(@Nullable String client, @NonNull String regex) throws DataAccessException {
		PreConditions.checkEmpty(client, "client");
		PreConditions.checkString(regex, "regex");

		String sql;

		if (client == null) {
			sql = "SELECT count(*) FROM url_whitelist WHERE client_id is ? AND test_regex = ?";
		} else {
			sql = "SELECT count(*) FROM url_whitelist WHERE client_id = ? AND test_regex = ?";
		}

		// Verifica se já existe, só cadastra se não existir
		if (jdbcTemplate.queryForObject(sql, new Object[] { client, regex }, Integer.class) == 0) {
			sql = "INSERT INTO url_whitelist (client_id, test_regex) VALUES (?, ?);";
			jdbcTemplate.update(sql, client, regex);
			return true;
		}
		return false;
	}

	/*****************************
	 * Executa o delegate para cada entrada da whitelist do cliente informado (null
	 * para lista global).
	 * 
	 * @param client Identificação do cliente (ou null para lista global)
	 * @param action Delegate a ser executado para cada item da whitelist. A busca
	 *               termina quando o delegate retornar true.
	 * @return Retorna true se o delegate encontrou a informação (retornou true) ou
	 *         false caso contrário.
	 */
	@Override
	public boolean forEach(@Nullable String client, @NonNull Function<UrlWhitelistItem, Boolean> action) {
		PreConditions.checkNotNull(action, "action");

		UrlWhitelistItem item = new UrlWhitelistItem();
		String sql;

		ResultSetExtractor<Boolean> func = (rs) -> {
			while (rs.next()) {
				UrlWhitelistItemRsAdapter.loadTo(rs, item);
				if (action.apply(item)) {
					return true;
				}
			}
			return false;
		};

		if (client == null) {
			sql = "SELECT * FROM url_whitelist WHERE client_id is ?;";
		} else {
			sql = "SELECT * FROM url_whitelist WHERE client_id = ?;";
		}
		return jdbcTemplate.query(sql, new Object[] { client }, func);
	}

}
