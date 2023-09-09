package ru.practicum.ewm.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class ViewStats {

    String app; // Название сервиса

    String uri; // URI сервиса

    Long hits; //  Количество просмотров

}