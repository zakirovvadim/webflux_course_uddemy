package ru.vadim.webfluxcourse.tests.sec02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec02.repository.CustomerOrderRepository;

@Slf4j
public class Lec03CustomerOrderRepositoryTest extends AbstractTest {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void getProductOrderByCustomer() {
        customerOrderRepository.findProductOrderByCustomer("mike")
                .doOnNext(el -> log.info(el.toString()))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void orderDetailsByProduct() {
        customerOrderRepository.getOrderDetailsByProduct("iphone 20")
                .doOnNext(el -> log.info(el.toString()))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.getAmount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.getAmount()))
                .expectComplete()
                .verify();
    }
}
