package ru.skypro.homework.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.USERNAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.USERNAME_MIN_SIZE;

@Data
@Schema(description = "DTO для авторизации пользователя")
public class LoginDto {

    @Schema(description = "Логин пользователя", minLength = USERNAME_MIN_SIZE, maxLength = USERNAME_MAX_SIZE, example = "user123")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = USERNAME_MIN_SIZE, max = USERNAME_MAX_SIZE, message =
            "Логин должен быть от " + USERNAME_MIN_SIZE + " до " + USERNAME_MAX_SIZE + " символов")
    private String username;

    @Schema(
            description = "Пароль пользователя",
            minLength = PASSWORD_MIN_SIZE,
            maxLength = PASSWORD_MAX_SIZE,
            example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message =
            "Пароль должен быть от " + PASSWORD_MIN_SIZE + " до " + PASSWORD_MAX_SIZE + " символов")
    private String password;
}
