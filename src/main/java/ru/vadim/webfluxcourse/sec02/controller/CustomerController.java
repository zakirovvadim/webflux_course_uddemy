package ru.vadim.webfluxcourse.sec02.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxcourse.sec02.entity.Customer;
import ru.vadim.webfluxcourse.sec02.repository.CustomerRepository;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/endOfEmailLike")
    public Flux<Customer> getByEndOfEmailLike(@RequestParam(value = "endOfEmail") String str) {
        return customerRepository.findByEmailLike("%" + str);
    }

}
