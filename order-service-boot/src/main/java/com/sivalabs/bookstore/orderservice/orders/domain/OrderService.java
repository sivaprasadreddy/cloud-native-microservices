package com.sivalabs.bookstore.orderservice.orders.domain;

import com.sivalabs.bookstore.orderservice.orders.api.CreateOrderRequest;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.Order;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderConfirmationDTO createOrder(CreateOrderRequest orderRequest) {
        Order newOrder = orderMapper.convertToEntity(orderRequest);
        Order savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderId=" + savedOrder.getOrderId());
        return new OrderConfirmationDTO(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    public Optional<OrderDTO> findOrderByOrderId(String orderId) {
        return this.orderRepository.findByOrderId(orderId).map(OrderDTO::from);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void updateOrderStatus(String orderId, OrderStatus status, String comments) {
        Order order =
                orderRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        order.setComments(comments);
        orderRepository.save(order);
    }
}
