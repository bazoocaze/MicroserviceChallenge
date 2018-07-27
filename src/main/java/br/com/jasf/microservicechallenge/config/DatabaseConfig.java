package br.com.jasf.microservicechallenge.config;

import java.io.IOException;

import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

	
	@Autowired
	private void CreateTables(JdbcTemplate jdbcTemplate, ResourceConfig resourceConfig) throws IOException {
		Log log = LogFactory.getLog(DatabaseConfig.class);
		log.info("Creating database tables (if needed)");
		String sql = resourceConfig.getCreateTableResource();
		log.debug(String.format("SQL: %s\n", sql));
		jdbcTemplate.execute(sql);
		log.info("Database tables created");
	}
}
