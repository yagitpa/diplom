package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Обертка для списка объявлений")
public class Ads {

    @Schema(description = "Общее количество объявлений", example = "10")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<Ad> results;
}
