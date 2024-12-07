package ru.vadim.webfluxcourse.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;
/*
Для атворизации используется передача в заголовки, например для базоваой, или для бирер h.setBearer()
 */
public class Lec07BasicAuthTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.defaultHeaders(h -> h.setBasicAuth("java", "secret")));

    @Test
    void defaultHeader() {
        this.client.get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
