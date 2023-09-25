package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Set;

import lombok.Data;

@Data
public class CompilationDto {

    Long id; // Идентификатор

    Set<EventShortDto> events; // Список идентификаторов событий, входящих в подборку

    Boolean pinned; // Закреплена ли подборка на главной странице сайта

    String title; // Заголовок подборки

}