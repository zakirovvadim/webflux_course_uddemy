package ru.vadim.webfluxcourse.sec02.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxcourse.sec02.entity.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    Flux<Product> findByPriceBetween(Integer priceFrom, Integer priceTo);

    Flux<Product> findBy (Pageable pageable);
}
