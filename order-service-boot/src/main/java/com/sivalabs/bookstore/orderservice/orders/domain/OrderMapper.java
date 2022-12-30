package com.sivalabs.bookstore.orderservice.orders.domain;

import com.sivalabs.bookstore.orderservice.orders.api.CreateOrderRequest;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.Order;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderItem;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order convertToEntity(CreateOrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomerName(orderRequest.customer().name());
        newOrder.setCustomerEmail(orderRequest.customer().email());
        newOrder.setCustomerPhone(orderRequest.customer().phone());
        newOrder.setDeliveryAddressLine1(orderRequest.deliveryAddress().addressLine1());
        newOrder.setDeliveryAddressLine2(orderRequest.deliveryAddress().addressLine2());
        newOrder.setDeliveryAddressCity(orderRequest.deliveryAddress().city());
        newOrder.setDeliveryAddressState(orderRequest.deliveryAddress().state());
        newOrder.setDeliveryAddressZipCode(orderRequest.deliveryAddress().zipCode());
        newOrder.setDeliveryAddressCountry(orderRequest.deliveryAddress().country());

        Set<OrderItem> orderItems = new HashSet<>();
        for (com.sivalabs.bookstore.orderservice.orders.domain.model.OrderItem item :
                orderRequest.items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }
}
