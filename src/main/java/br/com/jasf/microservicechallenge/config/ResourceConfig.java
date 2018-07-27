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
	
	public String getCreateTableResource() throws IOException
	{
		try(InputStream stream = createTableResource.getInputStream())
		{
			return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		}
	}

}
