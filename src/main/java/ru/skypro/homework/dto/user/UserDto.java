package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.auth.Role;

import static ru.skypro.homework.constants.ValidationConstants.PHONE_EXAMPLE;

@Data
@Schema(description = "DTO для отображения информации о пользователе")
public class UserDto {

    @Schema(description = "ID пользователя", example = "1")
    private Integer id;

    @Schema(description = "Логин пользователя (email)", example = "user@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Телефон пользователя", example = PHONE_EXAMPLE)
    private String phone;

    @Schema(
            description = "Роль пользователя", allowableValues = {"USER", "ADMIN"}, example = "USER")
    private Role role;

    @Schema(
            description = "Ссылка на аватар пользователя",
            example = "/images/avatar.jpg",
            nullable = true)
    private String image;
}
