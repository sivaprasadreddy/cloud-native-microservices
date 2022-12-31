package com.sivalabs.bookstore.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProductControllerTest {

    @Inject ProductService productService;

    @Test
    void shouldGetAllProducts() {
        List<Product> products =
                given().when()
                        .get("/api/products")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<>() {});
        assertNotNull(products);
    }

    @Test
    void shouldGetProductById() {
        Product p =
                new Product(
                        null,
                        "P777",
                        "Product P777",
                        "P777 desc",
                        "https://images.com/p111.png",
                        BigDecimal.TEN);
        p = productService.createTodo(p);
        given().when()
                .get("/api/products/" + p.id)
                .then()
                .statusCode(200)
                .body("code", equalTo("P777"))
                .body("name", equalTo("Product P777"))
                .body("description", equalTo("P777 desc"))
                .body("imageUrl", equalTo("https://images.com/p111.png"))
                .body("price", equalTo(10));
    }

    @Test
    void shouldCreateProduct() {
        String body =
                """
                    {
                        "code" : "P333",
                        "name" : "Product P333",
                        "description" : "Product P333 description",
                        "imageUrl": "https://images.com/1234.ping",
                        "price": 23.5
                    }
                    """;
        given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("code", equalTo("P333"))
                .body("name", equalTo("Product P333"))
                .body("description", equalTo("Product P333 description"))
                .body("imageUrl", equalTo("https://images.com/1234.ping"))
                .body("price", equalTo(23.5f));
    }
}
