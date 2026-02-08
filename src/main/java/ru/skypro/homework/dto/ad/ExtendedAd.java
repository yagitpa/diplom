package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Расширенная информация об объявлении")
public class ExtendedAd {

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
            example = "/images/ads/1/image.jpg",
            nullable = true)
    private String image;

    @Schema(description = "Телефон автора объявления", example = "+7 (999) 123-45-67")
    private String phone;

    @Schema(description = "Цена объявления", example = "15000", minimum = "0", maximum = "10000000")
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам ноутбук")
    private String title;
}
