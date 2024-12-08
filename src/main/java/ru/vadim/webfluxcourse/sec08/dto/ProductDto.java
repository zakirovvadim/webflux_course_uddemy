package ru.vadim.webfluxcourse.sec08.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String description;
    private Integer price;
}
