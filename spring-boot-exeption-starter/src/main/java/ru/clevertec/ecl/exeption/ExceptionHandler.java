package ru.clevertec.ecl.exeption;


import ru.clevertec.ecl.model.ErrorResponse;

public interface ExceptionHandler {
    ErrorResponse handleException(Exception e);
}
