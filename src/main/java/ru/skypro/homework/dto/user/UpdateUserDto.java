package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.FIRST_NAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.FIRST_NAME_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.LAST_NAME_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.LAST_NAME_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.PHONE_EXAMPLE;
import static ru.skypro.homework.constants.ValidationConstants.PHONE_PATTERN;

@Data
@Schema(description = "DTO для обновления информации о пользователе")
public class UpdateUserDto {

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
    @Pattern(regexp = PHONE_PATTERN, message = "Телефон должен соответствовать формату " + PHONE_EXAMPLE)
    private String phone;
}
