package br.com.jasf.microservicechallenge.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.lang.NonNull;

import br.com.jasf.microservicechallenge.utils.PreConditions;

public class UrlWhitelistItemRsAdapter {
	public static void loadTo(@NonNull ResultSet rs, @NonNull UrlWhitelistItem item) throws SQLException
	{
		PreConditions.checkNotNull(rs, "rs");
		PreConditions.checkNotNull(item, "item");
		
		item.setId(rs.getInt("id"));
		item.setClientId(rs.getString("client_id"));
		item.setRegex(rs.getString("test_regex"));
	}
}
