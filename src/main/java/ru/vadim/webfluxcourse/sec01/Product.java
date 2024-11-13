package ru.vadim.webfluxcourse.sec01;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String description;
    private Integer price;
}
