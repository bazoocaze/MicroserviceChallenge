package br.com.jasf.microservicechallenge.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

@Configuration
public class ResourceConfig {

	@Value("classpath:create_tables.sql")
	private Resource createTableResource;

	@Value("classpath:create_tables_h2.sql")
	private Resource createTableResourceH2;

	/*********************
	 * Obtem o DDL de criação de tabelas a partir dos recursos do programa.
	 * 
	 * @return O conteúdo do arquivo contendo os comandos DDL.
	 * @throws IOException
	 */
	public String getCreateTableResource() throws IOException {
		try (InputStream stream = createTableResource.getInputStream()) {
			return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		}
	}

	/*********************
	 * Obtem o DDL de criação de tabelas a partir dos recursos do programa (banco H2 de testes).
	 * 
	 * @return O conteúdo do arquivo contendo os comandos DDL.
	 * @throws IOException
	 */
	public String getCreateTableResourceH2() throws IOException {
		try (InputStream stream = createTableResourceH2.getInputStream()) {
			return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		}
	}

}
