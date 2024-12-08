package ru.vadim.webfluxcourse.tests.sec07;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;
/*
Здесь мы вынесли логику генерации токенов в отдельный метод (для примера), для того, чтобы генерирвоать токен на каждый запрос
Т.е. ExchangeFilter нужен для исходящих запросов, чтобы чтонибдь с ними сделать.
 */
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class Lec09ExchangeFilterTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator()).filter(logRequests()));

    @Test
    void defaultHeader() {
        for (int i = 1; i < 5; i++) {
            this.client.get()
                    .uri("/lec09/product/{id}", 1)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .then()
                    .as(StepVerifier::create)
                    .expectComplete()
                    .verify();
        }
    }

    private Consumer<List<ExchangeFilterFunction>> getExchangeFilters() {
        ExchangeFilterFunction tokenGeneratorExFilter = tokenGenerator();
        ExchangeFilterFunction logRequestsExFilter = logRequests();
        return list -> List.of(tokenGeneratorExFilter);
    }

    private ExchangeFilterFunction tokenGenerator() {
        return ((request, next) -> { // reqeust переменная тут имммутабельна, поэтому мы не сможем унее вызвать сетБирер токена, для этого создаем новые реквест на основе этой перемненой через билдер.
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("generated token : {}", token);
            ClientRequest modifiedRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        });
    }

    private ExchangeFilterFunction logRequests() {
        return ((request, next) -> {
            log.info("request path: {}", request.url());
            return next.exchange(request);
        });
    }
}
