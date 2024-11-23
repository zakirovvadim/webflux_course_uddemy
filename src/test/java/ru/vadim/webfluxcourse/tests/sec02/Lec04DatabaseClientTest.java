package ru.vadim.webfluxcourse.tests.sec02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec02.dto.OrderDetails;

@Slf4j
public class Lec04DatabaseClientTest extends AbstractTest {

    @Autowired
    private DatabaseClient databaseClient;


    @Test
    void orderDetailsByProduct() {
        String query = """
                    SELECT
                    co.order_id,
                    c.name AS customer_name,
                    p.description AS product_name,
                    co.amount,
                    co.order_date
                FROM
                    customer c
                INNER JOIN customer_order co ON c.id = co.customer_id
                INNER JOIN product p ON p.id = co.product_id
                WHERE
                    p.description = :description
                ORDER BY co.amount DESC
                    """;

        this.databaseClient
                .sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(el -> log.info(el.toString()))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.getAmount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.getAmount()))
                .expectComplete()
                .verify();
    }
}
