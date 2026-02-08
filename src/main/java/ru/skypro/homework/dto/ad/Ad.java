package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Базовая информация об объявлении")
public class Ad {

    @Schema(description = "ID автора объявления", example = "1")
    private Integer author;

    @Schema(
            description = "Ссылка на картинку объявления",
            example = "/images/ads/1/image.jpg",
            nullable = true)
    private String image;

    @Schema(description = "ID объявления", example = "1")
    private Integer pk;

    @Schema(description = "Цена объявления", example = "15000", minimum = "0", maximum = "10000000")
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам ноутбук")
    private String title;
}
