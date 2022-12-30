package com.sivalabs.bookstore.orderservice.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.bookstore.orderservice.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.model.Address;
import com.sivalabs.bookstore.orderservice.orders.domain.model.Customer;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderItem;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetOrderApiTests extends AbstractIntegrationTest {

    @Autowired private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {

        Customer customer = new Customer("Siva", "siva@gmail.com", "99999999999");

        Address address =
                new Address(
                        "addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");

        Set<OrderItem> items = Set.of(new OrderItem("P100", "Product 1", BigDecimal.TEN, 1));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(items, customer, address);
        OrderConfirmationDTO orderConfirmationDTO = orderService.createOrder(createOrderRequest);

        OrderDTO orderDTO =
                given().when()
                        .get("/api/orders/{orderId}", orderConfirmationDTO.getOrderId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(OrderDTO.class);

        assertThat(orderDTO.orderId()).isEqualTo(orderConfirmationDTO.getOrderId());
        assertThat(orderDTO.items()).hasSize(1);
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdNotExist() {
        given().when().get("/api/orders/{orderId}", "non-existing-order-id").then().statusCode(404);
    }
}
