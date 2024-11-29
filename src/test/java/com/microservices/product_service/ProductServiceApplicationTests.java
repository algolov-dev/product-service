package com.microservices.product_service;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBode = """
				{
				    "name": "Кроссовки утепленные мужские GSD ONE 2 MID WTR",
				    "description": "Теплые и мягкие кроссовки ONE 2 MID WTR незаменимы для долгих прогулок или насыщенного дня.",
				    "price": 2499
				}
				""";


		RestAssured.given()
				.contentType("application/json")
				.body(requestBode)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Кроссовки утепленные мужские GSD ONE 2 MID WTR"))
				.body("description", Matchers.equalTo("Теплые и мягкие кроссовки ONE 2 MID WTR незаменимы для долгих прогулок или насыщенного дня."))
				.body("price", Matchers.equalTo(2499));
	}

}
