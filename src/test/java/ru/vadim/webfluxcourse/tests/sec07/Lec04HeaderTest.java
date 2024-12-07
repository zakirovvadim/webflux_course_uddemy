package ru.vadim.webfluxcourse.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

import java.util.Map;

public class Lec04HeaderTest extends AbstractWebClient {

    // можно установить хидер в запрос
    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));


    @Test
    void defaultHeader() {
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
    /*
Иногда вам может потребоваться отправить некий заголовок по умолчанию для каждого запроса. Те. перезаписать заколовок по умолчанию
     */

    @Test
    void overrideHeader() {
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "vadim_service")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    /*
    Если вам необходимо задать несколько заголовков, вы также можете использовать map.
     */
    @Test
    void headersWithMap() {
        Map<String, String> headersMap = Map.of("caller-id", "vadim", "some-key", "some-value");
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(headersMap))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
