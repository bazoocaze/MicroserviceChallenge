package br.com.jasf.microservicechallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import br.com.jasf.microservicechallenge.config.ResourceConfig;

@Component
public class Teste implements CommandLineRunner {

	@Autowired
	ResourceConfig res;
	
	@Override
	public void run(String... args) throws Exception {
		System.err.println("-----");
		System.err.println(res.getCreateTableResource());
		System.err.println("-----");
	}

}
