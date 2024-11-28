package ru.vadim.webfluxcourse.sec04.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vadim.webfluxcourse.sec04.exceptions.CustomerNotFoundException;
import ru.vadim.webfluxcourse.sec04.exceptions.InvalidInputException;

import java.net.URI;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("http://confluence.com/problems/customer-not-found"));
        problemDetail.setTitle("Customer not found");
        return problemDetail;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleException(InvalidInputException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("http://confluence.com/problems/invalid-input"));
        problemDetail.setTitle("Customer not found");
        return problemDetail;
    }
}
