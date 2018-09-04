package br.com.jasf.microservicechallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jasf.microservicechallenge.config.ResourceConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes=ResourceConfig.class)
public class ResourceConfigTests {
	
	@Autowired
	ResourceConfig resourceConfig;

	@Test
	public void testGetCreateTableResource() throws IOException {
		// Garante que pode carregar o script de Create Table
		String content = resourceConfig.getCreateTableResource();
		
		// Garante que o script contém CREATE TABLE
		assertThat(content).contains("CREATE TABLE");
	}

}
