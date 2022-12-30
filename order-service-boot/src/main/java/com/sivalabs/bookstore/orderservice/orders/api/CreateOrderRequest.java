package com.sivalabs.bookstore.orderservice.orders.api;

import com.sivalabs.bookstore.orderservice.orders.domain.model.Address;
import com.sivalabs.bookstore.orderservice.orders.domain.model.Customer;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

public record CreateOrderRequest(
        @NotEmpty(message = "Items cannot be empty.") Set<OrderItem> items,
        @Valid Customer customer,
        @Valid Address deliveryAddress)
        implements Serializable {}
