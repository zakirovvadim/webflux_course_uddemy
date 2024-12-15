package ru.vadim.webfluxcourse.sec08.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec08.dto.ProductDto;
import ru.vadim.webfluxcourse.sec08.mapper.EntityDtoMapper;
import ru.vadim.webfluxcourse.sec08.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<ProductDto> saveProducts(Flux<ProductDto>  flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .as(repository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductCount() {
        return this.repository.count();
    }


    public Flux<ProductDto> findAll() {
        return repository.findAll()
                .map(EntityDtoMapper::toDto);
    }
}
