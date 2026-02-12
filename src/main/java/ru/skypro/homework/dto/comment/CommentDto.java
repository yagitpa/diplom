package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "Информация о комментарии")
public class CommentDto {

    @Schema(description = "ID автора комментария", example = "1")
    private Integer author;

    @Schema(
            description = "Ссылка на аватар автора комментария",
            example = "/images/avatar/1.jpg",
            nullable = true)
    private String authorImage;

    @Schema(description = "Имя создателя комментария", example = "Иван")
    private String authorFirstName;

    @Schema(
            description = "Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970",
            example = "1641034800000")
    private Long createdAt;

    @Schema(description = "ID комментария", example = "1")
    private Integer pk;

    @Schema(description = "Текст комментария", example = "Очень заинтересовало! 5.000 уступите?")
    private String text;
}
