package ru.skypro.homework.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "DTO для авторизации пользователя")
public class Login {

    @Schema(
            description = "Логин пользователя",
            minLength = 4,
            maxLength = 32,
            example = "user123"
    )
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 32, message = "Логин должен быть от 4 до 32 символов")
    private String username;

    @Schema(
            description = "Пароль пользователя",
            minLength = 8,
            maxLength = 16,
            example = "password123"
    )
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;
}
