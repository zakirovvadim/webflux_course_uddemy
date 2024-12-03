package ru.vadim.webfluxcourse.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import ru.vadim.webfluxcourse.tests.sec07.dto.Product;

import java.time.Duration;

public class Lec01Test extends AbstractWebClient {

    private final WebClient client = createWebClient();

    //пример простого гет запроса. не забывай, что в реактивном стиле требуется подписка, для выполнения действия.
    @Test
    public void simpleGet() throws InterruptedException {
        this.client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    /*
    веб-клиент представляет собой просто оболочку вокруг сети реакторов. Сеть реакторов использует один поток на процессор, поэтому в ней нет сотен потоков.
    И этого достаточно, потому что он не блокируется.
    Из логов можно заметить, что один поток под номером 3 (для примера) может делать запрос сразу, не дожидаясь ответа (ответ идет с задержкой 1 секунда), и не сомтря на задержку ответа
    вывод, хоть 100 запросов получается сделать меньше чем за секунду.
    10:53:07.248 [reactor-http-nio-3] INFO ru.vadim.webfluxcourse.tests.sec07.AbstractWebClient -- received: Product[id=1, description=product-1, price=591]
    10:53:07.248 [reactor-http-nio-3] INFO ru.vadim.webfluxcourse.tests.sec07.AbstractWebClient -- received: Product[id=1, description=product-1, price=945]
    10:53:07.248 [reactor-http-nio-4] INFO ru.vadim.webfluxcourse.tests.sec07.AbstractWebClient -- received: Product[id=1, description=product-1, price=286]
    10:53:07.249 [reactor-http-nio-3] INFO ru.vadim.webfluxcourse.tests.sec07.AbstractWebClient -- received: Product[id=1, description=product-1, price=662]
     */
    @Test
    public void concurrentRequest() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            this.client.get()
//                    .uri("/lec01/product/" + i)
                    .uri("/lec01/product/{id}" + i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }

        Thread.sleep(Duration.ofSeconds(2));
    }
}
