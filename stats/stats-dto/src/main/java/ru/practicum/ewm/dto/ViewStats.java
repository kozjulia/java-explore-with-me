package ru.practicum.ewm.dto;

import lombok.*;

@AllArgsConstructor
@Getter
public class ViewStats {

    String app; // Название сервиса

    String uri; // URI сервиса

    Long hits; //  Количество просмотров

}