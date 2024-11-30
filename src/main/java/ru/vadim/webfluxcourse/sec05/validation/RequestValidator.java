package ru.vadim.webfluxcourse.sec05.validation;

import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec05.dto.CustomerDto;
import ru.vadim.webfluxcourse.sec05.exceptions.ApplicationExceptions;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationExceptions.missingValidEmail());
    }

    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.getName());
    }

    private static Predicate<CustomerDto> hasEmail() {
        return dto -> Objects.nonNull(dto.getEmail()) && dto.getEmail().contains("@");
    }
}
