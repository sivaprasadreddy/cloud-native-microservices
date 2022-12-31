package com.sivalabs.bookstore.orderservice.common;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Header;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine");
    protected static final RabbitMQContainer rabbitmq =
            new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11.5-alpine"));
    protected static final MockServerContainer mockServer =
            new MockServerContainer(
                    DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.13.2"));

    protected static MockServerClient mockServerClient;

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres, rabbitmq, mockServer).join();
    }

    @LocalServerPort private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        mockServerClient.reset();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);
        registry.add("app.product-service-url", mockServer::getEndpoint);
        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
    }

    protected static void mockGetProductByCode(
            String code, String name, BigDecimal price, BigDecimal discount, BigDecimal salePrice) {
        mockServerClient
                .when(request().withMethod("GET").withPath("/api/products/" + code))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header(
                                                "Content-Type", "application/json; charset=utf-8"))
                                .withBody(
                                        json(
                                                """
                                            {
                                                "code": "%s",
                                                "name": "%s",
                                                "price": %f,
                                                "discount": %f,
                                                "salePrice": %f
                                            }
                                        """
                                                        .formatted(
                                                                code,
                                                                name,
                                                                price.doubleValue(),
                                                                discount.doubleValue(),
                                                                salePrice.doubleValue()))));
    }
}
