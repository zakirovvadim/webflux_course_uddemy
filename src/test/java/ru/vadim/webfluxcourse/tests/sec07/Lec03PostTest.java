package ru.vadim.webfluxcourse.tests.sec07;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

import java.time.Duration;

@Slf4j
public class Lec03PostTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    void postBodyValue() {
        Product product = new Product(null, "iphone", 1500);
        this.client.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    /* Если объект нужно откуда то получить, например из репозитория, то нам следует использовать не метод bodyValue,
    а body, который принимает паблишер
     */

    @Test
    void postBody() {
        // var mono = this.productRepository.findById(1);
        Mono<Product> iphoneMono = Mono.fromSupplier(() -> new Product(null, "iphone", 1500));
//                .delayElement(Duration.ofSeconds(1));
        this.client.post()
                .uri("/lec03/product")
                .body(iphoneMono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
