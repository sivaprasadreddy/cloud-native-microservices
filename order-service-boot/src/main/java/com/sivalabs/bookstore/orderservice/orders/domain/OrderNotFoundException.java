package com.sivalabs.bookstore.orderservice.orders.domain;

import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class OrderNotFoundException extends ErrorResponseException {

    public OrderNotFoundException(String orderId) {
        super(
                HttpStatus.NOT_FOUND,
                asProblemDetail("Order with orderId " + orderId + " not found"),
                null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle("Order Not Found");
        problemDetail.setType(URI.create("https://api.sivalabs-bookstore.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
