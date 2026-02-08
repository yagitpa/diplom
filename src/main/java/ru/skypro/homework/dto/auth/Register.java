package ru.skypro.homework.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "DTO для регистрации пользователя")
public class Register {

    @Schema(description = "Логин пользователя", minLength = 4, maxLength = 32, example = "user123")
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 32, message = "Логин должен быть от 4 до 32 символов")
    private String username;

    @Schema(
            description = "Пароль пользователя",
            minLength = 8,
            maxLength = 16,
            example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;

    @Schema(description = "Имя пользователя", minLength = 2, maxLength = 16, example = "Иван")
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 16, message = "Имя должно быть от 2 до 16 символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя", minLength = 2, maxLength = 16, example = "Иванов")
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 16, message = "Фамилия должна быть от 2 до 16 символов")
    private String lastName;

    @Schema(
            description = "Телефон пользователя",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            example = "+7 (999) 123-45-67")
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен соответствовать формату +7 (XXX) XXX-XX-XX")
    private String phone;

    @Schema(
            description = "Роль пользователя",
            allowableValues = {"USER", "ADMIN"},
            example = "USER",
            defaultValue = "USER")
    private Role role = Role.USER;
}
