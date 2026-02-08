package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "DTO для смены пароля пользователя")
public class NewPassword {

    @Schema(
            description = "Текущий пароль",
            minLength = 8,
            maxLength = 16,
            example = "oldPassword123")
    @NotBlank(message = "Текущий пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Текущий пароль должен быть от 8 до 16 символов")
    private String currentPassword;

    @Schema(description = "Новый пароль", minLength = 8, maxLength = 16, example = "newPassword123")
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Новый пароль должен быть от 8 до 16 символов")
    private String newPassword;
}
