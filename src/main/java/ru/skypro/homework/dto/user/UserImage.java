package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "DTO для обновления аватара пользователя")
public class UserImage {

    @Schema(description = "Изображение аватара", format = "binary", type = "string")
    private MultipartFile image;
}
