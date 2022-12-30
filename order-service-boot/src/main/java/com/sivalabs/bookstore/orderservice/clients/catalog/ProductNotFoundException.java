package com.sivalabs.bookstore.orderservice.clients.catalog;

import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ProductNotFoundException extends ErrorResponseException {

    public ProductNotFoundException(String code) {
        super(
                HttpStatus.NOT_FOUND,
                asProblemDetail("Product with code: " + code + " not found"),
                null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(URI.create("https://api.sivalabs-bookstore.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
