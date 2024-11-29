package ru.vadim.webfluxcourse.sec05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
}
