package ru.vadim.webfluxcourse.sec08.mapper;

import ru.vadim.webfluxcourse.sec08.dto.ProductDto;
import ru.vadim.webfluxcourse.sec08.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return product;
    }

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getDescription(), product.getPrice());
    }
}
