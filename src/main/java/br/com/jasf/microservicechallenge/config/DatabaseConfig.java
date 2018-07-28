package br.com.jasf.microservicechallenge.config;

import java.io.IOException;

import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.jasf.microservicechallenge.utils.Util;

/****************
 * Configuração/preparação do banco de dados.
 * 
 * @author jose
 *
 */
@Configuration
public class DatabaseConfig {
	private Log logger;
	private JdbcTemplate jdbcTemplate;
	private ResourceConfig resourceConfig;

	/*********************
	 * Prepara o banco de dados para uso, criando a tabela da whitelist caso não
	 * exista.
	 * 
	 * @param appConfig
	 * @param jdbcTemplate
	 * @param resourceConfig
	 * @throws IOException
	 */
	@Autowired
	private void prepareConnection(AppConfig appConfig, JdbcTemplate jdbcTemplate, ResourceConfig resourceConfig)
			throws IOException {
		this.logger = LogFactory.getLog(DatabaseConfig.class);
		this.jdbcTemplate = jdbcTemplate;
		this.resourceConfig = resourceConfig;

		// TODO: tornar em propriedades
		int tries = appConfig.getFailureRetries();
		int delay = appConfig.getFailureInterval();
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

	/***********************
	 * Tenta criar as tabelas no banco a partir do resource .sql de definição das
	 * tabelas.
	 * 
	 * @throws IOException
	 * @throws DataAccessException
	 */
	private void tryCreateTables() throws IOException, DataAccessException {
		logger.info("Criando tabelas do banco de dados (caso necessário)");
		String sql = resourceConfig.getCreateTableResource();
		logger.debug(String.format("SQL: %s\n", sql));
		jdbcTemplate.execute(sql);
		logger.info("Operação concluída");
	}
}
