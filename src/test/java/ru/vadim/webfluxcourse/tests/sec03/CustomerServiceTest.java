package ru.vadim.webfluxcourse.tests.sec03;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.vadim.webfluxcourse.sec03.dto.CustomerDto;

import java.util.Objects;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllCustomersTest() {
        this.webTestClient
                .get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(val -> log.info("{}", val))
                .hasSize(10);
    }

    @Test
    public void getPaginatedCustomersTest() {
        this.webTestClient
                .get()
                .uri("/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$.[0].id").isEqualTo(6)
                .jsonPath("$.[1].id").isEqualTo(7);
    }

    @Test
    public void customerById() {
        this.webTestClient
                .get()
                .uri("/customers/3")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.name").isEqualTo("jake")
                .jsonPath("$.email").isEqualTo("jake@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        var dto = new CustomerDto(1, "nastya", "nastya@gmail.com");
        this.webTestClient
                .post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", Objects.requireNonNull(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("nastya")
                .jsonPath("$.email").isEqualTo("nastya@gmail.com");

        this.webTestClient
                .delete()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    void updateCustomer() {
        var dto = new CustomerDto(null, "vadim", "appolon@gmail.com");
        this.webTestClient
                .put()
                .uri("/customers/4")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", Objects.requireNonNull(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(4)
                .jsonPath("$.name").isEqualTo("vadim")
                .jsonPath("$.email").isEqualTo("appolon@gmail.com");
    }

    @Test
    void customerNotFound() {
        this.webTestClient
                .get()
                .uri(("/customers/120"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        this.webTestClient
                .delete()
                .uri(("/customers/120"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        var dto = new CustomerDto(null, "vadim", "appolon@gmail.com");
        this.webTestClient
                .put()
                .uri(("/customers/120"))
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();
    }
}
