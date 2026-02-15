package ru.skypro.homework.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.FIRST_NAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.FIRST_NAME_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.LAST_NAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.LAST_NAME_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PHONE_EXAMPLE;
import static ru.skypro.homework.constants.ValidationConstants.PHONE_PATTERN;
import static ru.skypro.homework.constants.ValidationConstants.USERNAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.USERNAME_MIN_SIZE;

@Data
@Schema(description = "DTO для регистрации пользователя")
public class RegisterDto {

    @Schema(description = "Логин пользователя", minLength = USERNAME_MIN_SIZE, maxLength = USERNAME_MAX_SIZE, example = "user123")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = USERNAME_MIN_SIZE, max = USERNAME_MAX_SIZE, message =
            "Логин должен быть от " + USERNAME_MIN_SIZE + " до " + USERNAME_MAX_SIZE + " символов")
    private String username;

    @Schema(description = "Пароль пользователя", minLength = PASSWORD_MIN_SIZE, maxLength = PASSWORD_MAX_SIZE,
            example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "Пароль должен быть от " + PASSWORD_MIN_SIZE + " до " + PASSWORD_MAX_SIZE + " символов")
    private String password;

    @Schema(description = "Имя пользователя", minLength = FIRST_NAME_MIN_SIZE, maxLength = FIRST_NAME_MAX_SIZE, example = "Иван")
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = FIRST_NAME_MIN_SIZE, max = FIRST_NAME_MAX_SIZE, message =
            "Имя должно быть от " + FIRST_NAME_MIN_SIZE + " до " + FIRST_NAME_MAX_SIZE + " символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя", minLength = LAST_NAME_MIN_SIZE, maxLength = LAST_NAME_MAX_SIZE, example = "Иванов")
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = LAST_NAME_MIN_SIZE, max = LAST_NAME_MAX_SIZE, message =
            "Фамилия должна быть от " + LAST_NAME_MIN_SIZE + " до " + LAST_NAME_MAX_SIZE + " символов")
    private String lastName;

    @Schema(description = "Телефон пользователя", pattern = PHONE_PATTERN, example = PHONE_EXAMPLE)
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = PHONE_PATTERN, message = "Телефон должен соответствовать формату, например " + PHONE_EXAMPLE)
    private String phone;

    @Schema(description = "Роль пользователя", allowableValues = {"USER", "ADMIN"}, example = "USER", defaultValue = "USER")
    private Role role = Role.USER;
}
