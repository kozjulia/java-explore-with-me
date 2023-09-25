package ru.practicum.ewm.compilation.dto;

import java.util.Set;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateCompilationRequest {

    Set<Long> events; // Список идентификаторов событий, входящих в подборку

    Boolean pinned = false; // Закреплена ли подборка на главной странице сайта

    @Size(min = 1, max = 50, message =
            "Ошибка! Заголовок подборки может содержать минимум 1, максимум 50 символов.")
    String title; // Заголовок подборки

}