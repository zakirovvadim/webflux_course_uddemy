package ru.vadim.webfluxcourse.tests.sec08;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.sec08.dto.ProductDto;

import java.nio.file.Path;
import java.time.Duration;

@Slf4j
public class ProductUploadDownloadTest {

    private final ProductClient productClient = new ProductClient();

    @Test
    void upload() {
        var flux = Flux.range(1, 1_000_000)
                .map(i -> new ProductDto(null, "iphone-" + i, i));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    //415 Unsupported Media Type from GET пока хз почему, вроде по аналогии сделал с уроком
    @Test
    void download() {
        this.productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

}
