package ru.clevertec.ecl.exeption;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.clevertec.ecl.exeption.model.ErrorResponse;

@ControllerAdvice
@Slf4j
public class Handler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseBody
    public ErrorResponse entityNotFound(EntityNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(String.valueOf(HttpStatus.NOT_FOUND));
        log.error("Exception", e);
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse handelValidatorException(MethodArgumentNotValidException e) {
        var error = e.getBindingResult().getAllErrors();
        log.error(error.get(0).getDefaultMessage(), e);
        var er = error.get(0);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(er.getDefaultMessage());
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return errorResponse;
    }

}

