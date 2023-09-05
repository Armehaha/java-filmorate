package ru.yandex.practicum.exception;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
