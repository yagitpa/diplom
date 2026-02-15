package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ru.skypro.homework.constants.ValidationConstants.COMMENT_TEXT_MAX_SIZE;
import static ru.skypro.homework.constants.ValidationConstants.COMMENT_TEXT_MIN_SIZE;

@Data
@Schema(description = "DTO для создания или обновления комментария")
public class CreateOrUpdateCommentDto {

    @Schema(description = "Текст комментария", minLength = COMMENT_TEXT_MIN_SIZE, maxLength = COMMENT_TEXT_MAX_SIZE,
            example = "Очень заинтересовало! 5.000 уступите?")
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = COMMENT_TEXT_MIN_SIZE, max = COMMENT_TEXT_MAX_SIZE, message = "Текст комментария должен содержать от "
            + COMMENT_TEXT_MIN_SIZE + " до " + COMMENT_TEXT_MAX_SIZE + " символов")
    private String text;
}
