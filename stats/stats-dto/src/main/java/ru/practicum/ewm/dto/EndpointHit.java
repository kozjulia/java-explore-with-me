package ru.practicum.ewm.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import static util.Constants.PATTERN_FOR_DATETIME;

@Data
public class EndpointHit {

    Long id; // Идентификатор записи

    String app; // Идентификатор сервиса для которого записывается информация

    String uri; // URI для которого был осуществлен запрос

    String ip; // IP-адрес пользователя, осуществившего запрос

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_FOR_DATETIME)
    String timestamp; //  Дата и время, когда был совершен запрос к эндпоинту

}