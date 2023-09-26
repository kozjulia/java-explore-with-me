package ru.practicum.ewm.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewUserRequest {

    @NotBlank(message = "Ошибка! Электронный адрес пользователя не может быть пустым.")
    @Size(min = 6, max = 254, message =
            "Ошибка! Электронный адрес пользователя может содержать минимум 6, максимум 254 символа.")
    @Email(message = "Ошибка! Неверный e-mail.")
    private String email; // Электронный адрес пользователя

    @NotBlank(message = "Ошибка! Имя пользователя не может быть пустым.")
    @Size(min = 2, max = 250, message =
            "Ошибка! Имя пользователя может содержать минимум 2, максимум 250 символов.")
    private String name; // Имя пользователя

}