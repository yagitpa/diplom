package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.AD_DESCRIPTION_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.AD_DESCRIPTION_MIN_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.AD_PRICE_MAX;
import static ru.skypro.homework.constants.ValidationConstants.AD_PRICE_MIN;
import static ru.skypro.homework.constants.ValidationConstants.AD_TITLE_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.AD_TITLE_MIN_SIZE;

@Data
@Schema(description = "DTO для создания или обновления объявления")
public class CreateOrUpdateAdDto {

    @Schema(description = "Заголовок объявления", minLength = AD_TITLE_MIN_SIZE,
            maxLength = AD_TITLE_MAX_SIZE, example = "Продам ноутбук")
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = AD_TITLE_MIN_SIZE, max = AD_TITLE_MAX_SIZE, message = "Заголовок должен содержать от " + AD_TITLE_MIN_SIZE +
            " до " + AD_TITLE_MAX_SIZE + " символов")
    private String title;

    @Schema(description = "Цена объявления")
    @Min(value = AD_PRICE_MIN, message = "Цена не может быть отрицательной")
    @Max(value = AD_PRICE_MAX, message = "Цена не может превышать " + AD_PRICE_MAX)
    private Integer price;

    @Schema(description = "Описание объявления", minLength = AD_DESCRIPTION_MIN_SIZE, maxLength = AD_DESCRIPTION_MAX_SIZE,
            example = "Отличный ноутбук в идеальном состоянии. 2 года использования.")
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = AD_DESCRIPTION_MIN_SIZE, max = AD_DESCRIPTION_MAX_SIZE, message = "Описание должно содержать от " + AD_DESCRIPTION_MIN_SIZE +
            " до " + AD_DESCRIPTION_MAX_SIZE + " символов")
    private String description;
}
