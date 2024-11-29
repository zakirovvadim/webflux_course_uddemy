package ru.vadim.webfluxcourse.sec05.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec05.dto.CustomerDto;
import ru.vadim.webfluxcourse.sec05.mapper.EntityDtoMapper;
import ru.vadim.webfluxcourse.sec05.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .map(EntityDtoMapper::map);
    }

    public Flux<CustomerDto> getAllCustomers(Integer page, Integer size) {
        return customerRepository.findBy(PageRequest.of(page - 1, size))
                .map(EntityDtoMapper::map);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return this.customerRepository.findById(id)
                .map(EntityDtoMapper::map);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> mono) {
        return mono.map(EntityDtoMapper::map)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::map);
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> mono) {
        return this.customerRepository.findById(id)
                .flatMap(entity -> mono)
                .map(EntityDtoMapper::map)
                .doOnNext(c -> c.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::map);
    }

    public Mono<Boolean> deleteCustomerById(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }
}
