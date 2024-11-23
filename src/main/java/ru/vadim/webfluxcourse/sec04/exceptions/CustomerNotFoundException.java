package ru.vadim.webfluxcourse.sec04.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Customer with [id=%d] is not found";

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
