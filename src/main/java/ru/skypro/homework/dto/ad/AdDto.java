package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Базовая информация об объявлении")
public class AdDto {

    @Schema(description = "ID автора объявления", example = "1")
    private Integer author;

    @Schema(description = "Ссылка на картинку объявления", example = "/ads-images/image.jpg", nullable = true)
    private String image;

    @Schema(description = "ID объявления", example = "1")
    private Integer pk;

    @Schema(description = "Цена объявления")
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам ноутбук")
    private String title;
}
