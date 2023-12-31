package com.example.provider.controller;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.example.model.Product;
import com.example.provider.ProviderApplication;
import com.example.provider.repository.ProductRepository;
import org.apache.http.HttpRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.ConfigurableWebApplicationContext;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Provider("ProductService")
@PactFolder("pacts")
@PactBroker(
		host = "localhost",
		port = "8002",
		authentication = @PactBrokerAuth(username = "pact", password = "pact")
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductPactProviderTest {

	@LocalServerPort
	int port;

	@MockBean
	private ProductRepository productRepository;
	private static ConfigurableWebApplicationContext application;

	@BeforeAll
	public static void start() {
		application = (ConfigurableWebApplicationContext) SpringApplication.run(ProviderApplication.class);
	}

	@BeforeEach
	void setUp(PactVerificationContext context) {
		context.setTarget(new HttpTestTarget("localhost", port));
	}

	@TestTemplate
	@ExtendWith(PactVerificationInvocationContextProvider.class)
	void verifyPact(PactVerificationContext context, HttpRequest request) {
		replaceAuthHeader(request);
		context.verifyInteraction();
	}

	private void replaceAuthHeader(HttpRequest request) {
		if (request.containsHeader("Authorization")) {
			String header = "Bearer " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
			request.removeHeaders("Authorization");
			request.addHeader("Authorization", header);
		}
	}

	@State("products exist")
	void toProductsExistState() {
		when(productRepository.fetchAll()).thenReturn(
				Arrays.asList(new Product("09", "CREDIT_CARD", "Gem Visa", "v1"),
						new Product("10", "CREDIT_CARD", "28 Degrees", "v1")));
	}

	@State({
			"no products exist",
			"product with ID 11 does not exist"
	})
	void toNoProductsExistState() {
		when(productRepository.fetchAll()).thenReturn(Collections.emptyList());
	}

	@State("product with ID 10 exists")
	void toProductWithIdTenExistsState() {
		when(productRepository.getById("10")).thenReturn(Optional.of(new Product("10", "CREDIT_CARD", "28 Degrees", "v1")));
	}

}