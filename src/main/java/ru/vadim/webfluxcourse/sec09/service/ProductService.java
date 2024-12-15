package ru.vadim.webfluxcourse.sec09.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import ru.vadim.webfluxcourse.sec09.dto.ProductDto;
import ru.vadim.webfluxcourse.sec09.mapper.EntityDtoMapper;
import ru.vadim.webfluxcourse.sec09.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final Sinks.Many<ProductDto> sink;

    public Mono<ProductDto> saveProducts(Mono<ProductDto>  mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(repository::save)
                .map(EntityDtoMapper::toDto)
                .doOnError(err ->  log.error(err.getMessage()))
                .doOnNext(this.sink::tryEmitNext); //Итак, всякий раз, когда мы добавляем новый продукт через эту синхронизацию, мы передаем его в синк

    }

    // отдельным методом можем получать данные из синка
    public Flux<ProductDto> productStream() {
        return this.sink.asFlux();
    }


}
