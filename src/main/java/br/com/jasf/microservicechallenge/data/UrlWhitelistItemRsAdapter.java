package br.com.jasf.microservicechallenge.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.lang.NonNull;

import br.com.jasf.microservicechallenge.utils.PreConditions;

/*********************
 * Carga de {@link ResultSet} para {@link UrlWhitelistItem}
 * 
 * @author jose
 *
 */
public class UrlWhitelistItemRsAdapter {
	/************************
	 * Carrega o {@link UrlWhitelistItem} a partir da linha corrente do
	 * {@link ResultSet}.
	 * 
	 * @param fromRs {@link ResultSet} de origem
	 * @param toItem {@link UrlWhitelistItem} a ser carregado
	 * @throws SQLException
	 */
	public static void loadTo(@NonNull ResultSet fromRs, @NonNull UrlWhitelistItem toItem) throws SQLException {
		PreConditions.checkNotNull(fromRs, "rs");
		PreConditions.checkNotNull(toItem, "item");

		toItem.setId(fromRs.getInt("id"));
		toItem.setClientId(fromRs.getString("client_id"));
		toItem.setRegex(fromRs.getString("test_regex"));
	}
}
