package br.com.jasf.microservicechallenge.config;

import java.io.IOException;

import javax.print.DocFlavor.STRING;

import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.jasf.microservicechallenge.utils.Util;

@Configuration
public class DatabaseConfig {
	private Log logger;
	private JdbcTemplate jdbcTemplate;
	private ResourceConfig resourceConfig;

	@Autowired
	private void prepareConnection(JdbcTemplate jdbcTemplate, ResourceConfig resourceConfig) throws IOException {
		this.logger = LogFactory.getLog(DatabaseConfig.class);
		this.jdbcTemplate = jdbcTemplate;
		this.resourceConfig = resourceConfig;

		// TODO: tornar em propriedades
		int tries = 12;
		int delay = 5;
		while (tries-- > 0) {
			String msg;
			try {
				tryCreateTables();
				// Preparação ok
				return;
			} catch (IOException ioEx) {
				msg = ioEx.getMessage();
			} catch (DataAccessException daEx) {
				msg = daEx.getMessage();
			}
			logger.warn(String.format("A operação com o banco de dados falhou falhou: %s\n", msg));
			logger.warn(String.format("Tentando novamente em %d segundos\n", delay));
			Util.delay(delay * 1000);
		}

		// desiste depois das tentativas
		try {
			tryCreateTables();
		} catch (Exception ex) {
			logger.fatal(
					String.format("Impossível preparar a conexão com o banco de dados depois de várias tentantivas"),
					ex);
			throw ex;
		}
	}

	private void tryCreateTables() throws IOException, DataAccessException {
		logger.info("Criando tabelas do banco de dados (caso necessário)");
		String sql = resourceConfig.getCreateTableResource();
		logger.debug(String.format("SQL: %s\n", sql));
		jdbcTemplate.execute(sql);
		logger.info("Operação concluída");
	}
}
