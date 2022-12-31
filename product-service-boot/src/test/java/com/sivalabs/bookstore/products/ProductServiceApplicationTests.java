package com.sivalabs.bookstore.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///dbname"})
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Autowired private MockMvc mockMvc;

    @Autowired private ProductRepository repo;

    @Test
    void shouldFetchProducts() throws Exception {
        this.mockMvc.perform(get("/api/products")).andExpect(status().isOk());
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "code": "P999",
                                                    "name": "Product Title",
                                                    "description": "Product description",
                                                    "price": 24.50
                                                }
                                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", equalTo("P999")))
                .andExpect(jsonPath("$.name", equalTo("Product Title")))
                .andExpect(jsonPath("$.description", equalTo("Product description")))
                .andExpect(jsonPath("$.price", is(new BigDecimal("24.5")), BigDecimal.class));
    }

    @Test
    void shouldFailToCreateProductWhenCodeIsNotPresent() throws Exception {
        String payload = "{ \"name\": \"product name\" }";
        this.mockMvc
                .perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product product =
                new Product(
                        null,
                        "P500",
                        "Product Title",
                        "Product description",
                        "https://images.2767052.jpg",
                        new BigDecimal("34.0"));

        Product savedProduct = repo.save(product);
        assertThat(repo.findById(savedProduct.getId())).isPresent();
        this.mockMvc
                .perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedProduct.getId()), Long.class))
                .andExpect(jsonPath("$.code", equalTo("P500")))
                .andExpect(jsonPath("$.name", equalTo("Product Title")))
                .andExpect(jsonPath("$.description", equalTo("Product description")))
                .andExpect(jsonPath("$.imageUrl", equalTo("https://images.2767052.jpg")))
                .andExpect(jsonPath("$.price", is(new BigDecimal("34.0")), BigDecimal.class));
    }
}
