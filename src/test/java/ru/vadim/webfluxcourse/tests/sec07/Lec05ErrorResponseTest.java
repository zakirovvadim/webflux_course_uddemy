package ru.vadim.webfluxcourse.tests.sec07;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec07.dto.CalculatorResponse;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

// помни, что сигнал об ошибке идет нисходящим потоком
@Slf4j
public class Lec05ErrorResponseTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    void defaultHeader() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnError(WebClientResponseException.class, ex -> log.info("{}", ex.getResponseBodyAs(ProblemDetail.class))) // если хотим оплучить детали ответа, но помните, что в случае ошибки мы предполагаем, что удаленный сервер предоставит нам подробную информацию о проблеме в формате ProblemDetail.
                //.onErrorReturn(new CalculatorResponse(0,0, null, 0.0)) // можно вернуть дефолтное значение в случае ошибки
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0,0, null,0.0)) // если нам нужно специализировать ошибки, тогда можно использовать этот вариант, но если ошибка будет с другим кодом, чем тот что указан, оператор ее не поймает.
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0,0, null,-1.0)) //
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void exchange() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "+")
                .exchangeToMono(this::decode)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
// для примера
    private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
        //clientResponse.cookies();
        //clientResponse.headers();
        log.info("status code: {}", clientResponse.statusCode());
        if (clientResponse.statusCode().is4xxClientError()) { //Так что если вы хотите обработать только ошибку 400, которую мы можем обработать здесь, или любую ошибку, мы также можем обработать ее - isError()
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(problemDetail -> log.info("{}", problemDetail))
                    .then(Mono.empty());
        }
         return clientResponse.bodyToMono(CalculatorResponse.class);
    }
}
