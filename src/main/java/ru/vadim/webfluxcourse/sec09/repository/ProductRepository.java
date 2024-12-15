package ru.vadim.webfluxcourse.sec09.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vadim.webfluxcourse.sec09.entity.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
