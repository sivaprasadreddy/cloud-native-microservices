package com.sivalabs.bookstore.orderservice;

import com.sivalabs.bookstore.orderservice.clients.catalog.Product;
import com.sivalabs.bookstore.orderservice.orders.api.CreateOrderRequest;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderErrorEvent;
import java.util.HashSet;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class NativeRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.serialization().registerType(Product.class);
        hints.serialization().registerType(CreateOrderRequest.class);
        hints.serialization().registerType(HashSet.class);
        hints.serialization().registerType(OrderCreatedEvent.class);
        hints.serialization().registerType(OrderCancelledEvent.class);
        hints.serialization().registerType(OrderDeliveredEvent.class);
        hints.serialization().registerType(OrderErrorEvent.class);
    }
}
