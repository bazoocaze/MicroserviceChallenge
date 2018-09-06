package br.com.jasf.microservicechallenge.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class EmptyTestConfiguration {

}
