package ru.practicum.ewm.exception;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ValidationErrorResponse {
    // доп. класс для корректного вывода ошибок валидации MethodArgumentNotValidException

    private final List<ApiError> errorResponses;
}