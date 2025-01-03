package ru.vadim.webfluxcourse.tests.sec10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;
import ru.vadim.webfluxcourse.tests.sec10.dto.Product;

/*
linux mac
 netstat -an| grep -w 127.0.0.1.7070
watch 'netstat -an| grep -w 127.0.0.1.7070'
windows powershell
netstat -an | findstr "127.0.0.1:7070"
while ($true) { netstat -an | findstr "127.0.0.1:7070"; Start-Sleep -Seconds 2 }

Заметка размера пула
То, что мы можем изменить размер этого пула и т. д., на самом деле не означает, что каждый должен пойти и сделат это.
В реальной жизни, вам, возможно, вообще не придется это настраивать.
Например, в нашем примере.
Удалённое обслуживание очень медленное, секунд пять.
Это своего рода имитация.
И мы отправляем 500 одновременных запросов и т.д.
Но в реальной жизни ваше приложение может вызывать другую службу.
Эта служба может отреагировать в течение 100 миллисекунд, если служба ответит в течение 100 миллисекунд,
таким образом, используя одно соединение, вы можете отправить десять запросов за одну секунду.
А используя 500-подключений, вы сможете обрабатывать 5000 запросов в секунду.
На самом деле это очень много.
Поэтому зачастую вам вообще не придется ничего настраивать.
Так что только если это действительно необходимо.
И в зависимости от пропускной способности вам, возможно, придется вносить коррективы.


При слишком большом количестве коннекшнов будет выпдать SocketException: No buffer space available (maximum connections reached?): connect
т.е. это не проблема вебфлакса, а операционной системы, которая не может выделить больше сокетов. Поэтому важно настраивать по необходимости сжатие
чтобы отвечать быстрее и переиспользовать коннекшн, а не осздавать их.
 */
public class Lec01HttpConnectionPoolingTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> {
        var poolSize = 501; // не забудь настрйоку размера пула, если хочешь чтобы на строчке 39 макс запросов было обработано одновременно, т.е. 501 шт.
        var provider = ConnectionProvider.builder("vadim") // для настройки пула соединений, который будет использовать последнее освобожденное соединение. Рекомендуется использовать лифо, вместо фифо
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5) // Это значит, например, мы создадим 500 подключений. Но если мы собираемся получить, скажем, 501 или 1000 запросов, нам излишек нужно положить в очередь и это размер этой очереди.
                .build();
        var httpClient = HttpClient.create(provider)
                .compress(true) // так как мы создаем вебклиент кастомно, а не через билдер, нам нужно явно указывать сжатие или keepAlive
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() {
        var max = 501;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();

    }

    private Mono<Product> getProduct(int id) {
        return this.client
                .get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
