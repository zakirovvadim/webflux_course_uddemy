package ru.vadim.webfluxcourse.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.CalculatorResponse;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

import java.io.Serializable;
import java.util.Map;

public class Lec06QueryParamsTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    /*
    квери можно строить через билдер
    еси убрать значение из билд метода, билдер не поймет что ты хочешь и для этого, скорее лучше использовать мапу, так как
    более внятная ошибка
     */
    @Test
    void uriBuilderVariables() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        this.client.get()
                .uri(builder -> builder.path(path).query(query).build(10, "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    /*
    также можно использовать мапу
    Если в ключе мапы будет ошибка, тополучим значимую ошибку, говорящую о том, что карта не имеет значения для второго.
    По сути, он пытается построить URL. Он не содержит переменную, так как не видит переменную для значения во второй раз.
     */
    @Test
    void uriBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        Map<String, ? extends Serializable> map = Map.of(
                "first", 10,
                "second", 20,
                "operation", "+"
        );
        this.client.get()
                .uri(builder -> builder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
