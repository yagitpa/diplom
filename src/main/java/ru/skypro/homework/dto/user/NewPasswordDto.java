package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PASSWORD_MIN_SIZE;

@Data
@Schema(description = "DTO для смены пароля пользователя")
public class NewPasswordDto {

    @Schema(description = "Текущий пароль", minLength = PASSWORD_MIN_SIZE, maxLength = PASSWORD_MAX_SIZE,
            example = "oldPassword123")
    @NotBlank(message = "Текущий пароль не может быть пустым")
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message =
            "Текущий пароль должен быть от " + PASSWORD_MIN_SIZE + " до " + PASSWORD_MAX_SIZE + " символов")
    private String currentPassword;

    @Schema(description = "Новый пароль", minLength = PASSWORD_MIN_SIZE, maxLength = PASSWORD_MAX_SIZE, example = "newPassword123")
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = "Новый пароль должен быть от " + PASSWORD_MIN_SIZE + " до " + PASSWORD_MAX_SIZE + " символов")
    private String newPassword;
}
