package ru.vadim.webfluxcourse.tests.sec02;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec02.entity.Customer;
import ru.vadim.webfluxcourse.sec02.repository.CustomerRepository;

@Slf4j
public class Lec01CustomerRepositoryTest extends AbstractTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        this.customerRepository.findAll()
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void findById() {
        this.customerRepository
                .findById(2)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByName() {
        this.customerRepository
                .findByName("mike")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByEmailEndsOf() {
        this.customerRepository
                .findByEmailLike("%" + "ia@example.com")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void insertAndDeleteConsumer() {
        Customer customer = new Customer();
        customer.setName("nastya");
        customer.setEmail("nastya@gmail.com");

        this.customerRepository
                .save(customer)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))
                .expectComplete()
                .verify();

        this.customerRepository
                .findById(11)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();

        this.customerRepository
                .deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateCustomer() {
        this.customerRepository
                .findByName("ethan")
                .doOnNext(c -> c.setName("ilsur"))
                .flatMap(customerRepository::save)
                .doOnNext(c -> log.info("saved :: {}", c.getName()))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals(c.getName(), "ilsur"))
                .expectComplete()
                .verify();
    }
}
