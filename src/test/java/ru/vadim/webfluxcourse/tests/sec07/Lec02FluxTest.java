package ru.vadim.webfluxcourse.tests.sec07;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

import java.time.Duration;

@Slf4j
public class Lec02FluxTest extends AbstractWebClient {
    private final WebClient client = createWebClient();
/*
Вместо указания Thread.sleep мы можем использовать then, который посылает Моно войд когда получает сигнал комплит.
as(StepVerifier::create) - добавляем пустой шаг проверки
expectComplete() - ожидаем сигнал завершения - комплит
Тест будет завершен, когда мы получим полный сигнал.
 */
    @Test
    void streamingResponse() {
        this.client.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
