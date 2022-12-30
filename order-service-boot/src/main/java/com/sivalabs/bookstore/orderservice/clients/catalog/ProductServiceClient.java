package com.sivalabs.bookstore.orderservice.clients.catalog;

import com.sivalabs.bookstore.orderservice.ApplicationProperties;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductServiceClient {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestTemplate restTemplate;
    private final ApplicationProperties properties;

    public ProductServiceClient(RestTemplate restTemplate, ApplicationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product for code: {}", code);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            String url = properties.productServiceUrl() + "/api/products/" + code;
            ResponseEntity<Product> response =
                    restTemplate.exchange(url, HttpMethod.GET, httpEntity, Product.class);
            return Optional.ofNullable(response.getBody());
        } catch (RuntimeException e) {
            log.error("Error while fetching product for code: " + code, e);
            return Optional.empty();
        }
    }
}
