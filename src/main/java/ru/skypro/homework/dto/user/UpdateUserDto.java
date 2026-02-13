package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "DTO для обновления информации о пользователе")
public class UpdateUserDto {

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
}
