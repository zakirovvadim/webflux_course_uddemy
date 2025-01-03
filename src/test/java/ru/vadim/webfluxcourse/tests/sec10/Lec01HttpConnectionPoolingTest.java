package ru.vadim.webfluxcourse.tests.sec10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec10.dto.Product;

import java.time.Duration;

/*
linux mac
 netstat -an| grep -w 127.0.0.1.7070
watch 'netstat -an| grep -w 127.0.0.1.7070'

windows powershell
netstat -an | findstr "127.0.0.1:7070"
while ($true) { netstat -an | findstr "127.0.0.1:7070"; Start-Sleep -Seconds 2 }

 */
public class Lec01HttpConnectionPoolingTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void concurrentRequests() throws InterruptedException {
        var max = 3;
        Flux.range(1, max)
                .flatMap(this::getProduct)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();

        Thread.sleep(Duration.ofMinutes(1));
    }

    private Mono<Product> getProduct(int id) {
        return this.client
                .get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
