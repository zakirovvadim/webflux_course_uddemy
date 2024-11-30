package ru.vadim.webfluxcourse.tests.sec05;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.vadim.webfluxcourse.sec03.dto.CustomerDto;
/*
Тесты сделал сам, примерные, то что было в курсе не переписывал сюда, поэтому если надо посомтреть как сделал автор Винот
пересмотри видос */
@AutoConfigureWebTestClient
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private WebTestClient client;


    @Test
    public void getAllCustomersTestIsSuccess() {
        this.client
                .get()
                .uri("/customers")
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    public void getAllCustomersTestNo_token() {
        this.client
                .get()
                .uri("/customers")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void getAllCustomersTestPutForbidden() {
        var dto1 = new CustomerDto(null, "vadim", "appolon@gmail.com");
        this.client
                .post()
                .uri("/customers")
                .bodyValue(dto1)
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isForbidden();

        var dto = new CustomerDto(2, "vadim", "appolon@gmail.com");
        this.client
                .put()
                .uri("/customers")
                .bodyValue(dto)
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isForbidden();

        this.client
                .delete()
                .uri(("/customers/120"))
                .header("auth-token", "secret123")
                .exchange()
                .expectStatus().isForbidden();
    }

}
