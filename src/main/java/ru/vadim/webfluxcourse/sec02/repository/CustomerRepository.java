package ru.vadim.webfluxcourse.sec02.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxcourse.sec02.entity.Customer;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Flux<Customer> findByName(String name);
    Flux<Customer> findByEmailLike(String str);
}
