package ru.clevertec.ecl.exeption;

public class NotEmptyException extends RuntimeException {

    public NotEmptyException(String message) {
        super(message);
    }
}