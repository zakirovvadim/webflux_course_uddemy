package ru.vadim.webfluxcourse.sec03.mapper;

import ru.vadim.webfluxcourse.sec03.dto.CustomerDto;
import ru.vadim.webfluxcourse.sec03.entity.Customer;

public class EntityDtoMapper {

    public static Customer map(CustomerDto dto) {
        var customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setId(dto.getId());
        return customer;
    }

    public static CustomerDto map(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());
    }
}
