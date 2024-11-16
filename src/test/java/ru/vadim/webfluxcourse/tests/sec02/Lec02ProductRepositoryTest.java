package ru.vadim.webfluxcourse.tests.sec02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec02.repository.ProductRepository;

@Slf4j
public class Lec02ProductRepositoryTest extends AbstractTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getProductsWithPriceRange() {
        this.productRepository.findByPriceBetween(0, 400)
                .doOnNext(el -> log.info(el.toString()))
                .as(StepVerifier::create)
                .expectNextCount(4)
                .expectComplete()
                .verify();

    }

    @Test
    public void getProductsWithPageable() {
        this.productRepository.findBy(PageRequest.of(0, 3).withSort(Sort.by("price").ascending()))
                .doOnNext(el -> log.info(el.toString()))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();

    }
}
