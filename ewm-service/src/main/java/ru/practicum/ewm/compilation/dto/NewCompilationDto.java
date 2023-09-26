package ru.practicum.ewm.compilation.dto;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewCompilationDto {
    //Подборка событий

    private Set<Long> events; // Список идентификаторов событий, входящих в подборку

    private Boolean pinned = false; // Закреплена ли подборка на главной странице сайта

    @NotBlank(message = "Ошибка! Заголовок подборки не может быть пустым.")
    @Size(min = 1, max = 50, message =
            "Ошибка! Заголовок подборки может содержать минимум 1, максимум 50 символов.")
    private String title; // Заголовок подборки

}