package ru.skypro.homework.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "API для работы с комментариями к объявлениям")
public class CommentController {

    @Operation(
            summary = "Получение комментариев объявления",
            description = "Возвращает список всех комментариев к указанному объявлению")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Комментарии получены",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Comments.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @GetMapping("/{adId}/comments")
    public ResponseEntity<Comments> getComments(
            @Parameter(description = "ID объявления") @PathVariable Integer adId) {
        log.info("Запрос комментариев для объявления с ID: {}", adId);
        // Заглушка
        Comments comments = new Comments();
        comments.setCount(0);
        return ResponseEntity.ok(comments);
    }

    @Operation(
            summary = "Добавление комментария к объявлению",
            description = "Создает новый комментарий к указанному объявлению")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Комментарий создан",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Comment.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @PostMapping("/{adId}/comments")
    public ResponseEntity<Comment> addComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Valid @RequestBody CreateOrUpdateComment createComment,
            Authentication authentication) {
        log.info("Запрос на добавление комментария к объявлению с ID: {}", adId);
        // Заглушка
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Удаляет комментарий по ID объявления и ID комментария")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Комментарий удален",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Доступ запрещен",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Комментарий или объявление не найдены",
                        content = @Content)
            })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId,
            Authentication authentication) {
        log.info("Запрос на удаление комментария с ID {} из объявления с ID: {}", commentId, adId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария", description = "Обновляет текст комментария")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Комментарий обновлен",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Comment.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Доступ запрещен",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Комментарий или объявление не найдены",
                        content = @Content)
            })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "ID объявления") @PathVariable Integer adId,
            @Parameter(description = "ID комментария") @PathVariable Integer commentId,
            @Valid @RequestBody CreateOrUpdateComment updateComment,
            Authentication authentication) {
        log.info("Запрос на обновление комментария с ID {} в объявлении с ID: {}", commentId, adId);
        // Заглушка
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }
}
