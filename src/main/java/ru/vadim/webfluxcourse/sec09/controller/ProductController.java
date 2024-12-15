package ru.vadim.webfluxcourse.sec09.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxcourse.sec09.dto.ProductDto;
import ru.vadim.webfluxcourse.sec09.dto.UploadResponse;
import ru.vadim.webfluxcourse.sec09.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> mono) {
        return productService.saveProducts(mono);
    }

    // Для стриминга вводим в браузере путь до этого эндопоинта и дергаем метод сохранения. В бразуере появляется запись.
//    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ProductDto> productStream() {
//        return productService.productStream();
//    }

    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> productStream(@PathVariable Integer maxPrice) {
        return productService.productStream()
                .filter(dto -> dto.getPrice() <= maxPrice);
    }
}
