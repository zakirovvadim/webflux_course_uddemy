package ru.vadim.webfluxcourse.sec09.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;
import ru.vadim.webfluxcourse.sec09.dto.ProductDto;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDto> sink() {
        return Sinks.many().replay().limit(1);
    }
}
