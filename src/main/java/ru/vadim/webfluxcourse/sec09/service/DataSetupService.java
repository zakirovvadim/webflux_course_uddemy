package ru.vadim.webfluxcourse.sec09.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec09.dto.ProductDto;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSetupService implements CommandLineRunner {

    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1, 1000)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> {
                    ProductDto productDto = new ProductDto(null, "product-" + i, ThreadLocalRandom.current().nextInt(1, 100));
                    return productDto;
                })
                .map(dto -> this.productService.saveProducts(Mono.just(dto)))
                .subscribe();
    }



}
