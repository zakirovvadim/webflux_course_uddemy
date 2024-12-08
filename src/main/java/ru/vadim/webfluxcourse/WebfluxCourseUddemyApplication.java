package ru.vadim.webfluxcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "ru.vadim.webfluxcourse.sec08")
@EnableR2dbcRepositories(basePackages = "ru.vadim.webfluxcourse.sec08.repository")
public class WebfluxCourseUddemyApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebfluxCourseUddemyApplication.class, args);
    }
}
