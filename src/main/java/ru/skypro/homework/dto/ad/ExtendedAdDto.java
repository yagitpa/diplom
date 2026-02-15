package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static ru.skypro.homework.constants.ValidationConstants.PHONE_EXAMPLE;

@Data
@Schema(description = "Расширенная информация об объявлении")
public class ExtendedAdDto {

    @Schema(description = "ID объявления", example = "1")
    private Integer pk;

    @Schema(description = "Имя автора объявления", example = "Иван")
    private String authorFirstName;

    @Schema(description = "Фамилия автора объявления", example = "Иванов")
    private String authorLastName;

    @Schema(description = "Описание объявления", example = "Отличный ноутбук в идеальном состоянии")
    private String description;

    @Schema(description = "Логин автора объявления", example = "user@example.com")
    private String email;

    @Schema(
            description = "Ссылка на картинку объявления",
            example = "/ads-images/image.jpg",
            nullable = true)
    private String image;

    @Schema(description = "Телефон автора объявления", example = PHONE_EXAMPLE)
    private String phone;

    @Schema(description = "Цена объявления")
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам ноутбук")
    private String title;
}
