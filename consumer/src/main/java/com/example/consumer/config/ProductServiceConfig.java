package com.example.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductServiceConfig {
	@Bean
	RestTemplate productRestTemplate(@Value("${PROVIDER_PORT:9084}") int port) {
		return new RestTemplateBuilder().rootUri(String.format("http://localhost:%d", port)).build();
	}
}
