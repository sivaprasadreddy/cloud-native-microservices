package com.sivalabs.bookstore.orderservice.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.sivalabs.bookstore.orderservice.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orderservice.orders.domain.OrderService;
import com.sivalabs.bookstore.orderservice.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.orderservice.orders.domain.model.OrderDTO;
import io.restassured.http.ContentType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateOrderApiTests extends AbstractIntegrationTest {

    @Autowired private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        OrderConfirmationDTO orderConfirmationDTO =
                given().contentType(ContentType.JSON)
                        .body(
                                """
                                {
                                    "customer" : {
                                        "name": "Siva",
                                        "email": "siva@gmail.com",
                                        "phone": "999999999"
                                    },
                                    "deliveryAddress" : {
                                        "addressLine1": "Birkelweg",
                                        "addressLine2": "Hans-Edenhofer-Straße 23",
                                        "city": "Berlin",
                                        "state": "Berlin",
                                        "zipCode": "94258",
                                        "country": "Germany"
                                    },
                                    "items": [
                                        {
                                            "code": "P100",
                                            "name": "Product 1",
                                            "price": 25.50,
                                            "quantity": 1
                                        }
                                    ]
                                }
                                """)
                        .when()
                        .post("/api/orders")
                        .then()
                        .statusCode(202)
                        .body("orderId", notNullValue())
                        .body("status", is("NEW"))
                        .extract()
                        .body()
                        .as(OrderConfirmationDTO.class);

        Optional<OrderDTO> orderOptional =
                orderService.findOrderByOrderId(orderConfirmationDTO.getOrderId());
        assertThat(orderOptional).isPresent();
        assertThat(orderOptional.get().status()).isEqualTo(OrderStatus.NEW);
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "customer" : {
                                "name": "Siva",

                                "phone": "999999999"
                            },
                            "deliveryAddress" : {
                                "addressLine1": "Birkelweg",
                                "addressLine2": "Hans-Edenhofer-Straße 23",
                                "city": "Berlin",
                                "state": "Berlin",
                                "zipCode": "94258",
                                "country": "Germany"
                            },
                            "items": [
                                {
                                    "code": "P100",
                                    "name": "Product 1",
                                    "price": 25.50,
                                    "quantity": 1
                                }
                            ]
                        }
                        """)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400);
    }
}
