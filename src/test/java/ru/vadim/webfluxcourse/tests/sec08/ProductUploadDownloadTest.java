package ru.vadim.webfluxcourse.tests.sec08;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec08.dto.ProductDto;

import java.time.Duration;

@Slf4j
public class ProductUploadDownloadTest {

    private final ProductClient productClient = new ProductClient();

    @Test
    void upload() {
        var flux = Flux.just(new ProductDto(null, "iphone", 1000))
                .delayElements(Duration.ofSeconds(10));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
