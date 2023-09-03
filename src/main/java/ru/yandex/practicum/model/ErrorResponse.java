package ru.yandex.practicum.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    // название ошибки
    private String error;
    // подробное описание
    private String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }


}
