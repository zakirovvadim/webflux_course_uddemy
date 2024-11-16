package ru.vadim.webfluxcourse.sec02.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxcourse.sec02.entity.Product;
import ru.vadim.webfluxcourse.sec02.repository.ProductRepository;

@RestController
@RequiredArgsConstructor
public class PoductController {

    private final ProductRepository productRepository;

    @GetMapping("/product_between_prices")
    public Flux<Product> getByEndOfEmailLike(@RequestParam(value = "priceFrom") Integer priceFrom,
                                             @RequestParam(value = "priceTo") Integer priceTo) {
        return productRepository.findByPriceBetween(priceFrom, priceTo);
    }
}
