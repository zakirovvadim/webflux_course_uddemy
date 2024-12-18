package ru.vadim.webfluxcourse.sec08.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec08.dto.ProductDto;
import ru.vadim.webfluxcourse.sec08.dto.UploadResponse;
import ru.vadim.webfluxcourse.sec08.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> flux) {
        log.info("invoker");
        return this.productService.saveProducts(flux)
                .then(this.productService.getProductCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto>  downloadProducts() {
        return this.productService.findAll();
    }
}
