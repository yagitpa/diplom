package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;

public interface CommentService {

    /**
     * Получение всех комментариев к объявлению
     *
     * @param adId идентификатор объявления
     * @return CommentsDto со списком комментариев
     */
    CommentsDto getComments(Integer adId);

    /**
     * Добавление комментария к объявлению
     *
     * @param adId идентификатор объявления
     * @param email email автора комментария
     * @param createComment данные нового комментария
     * @return созданный комментарий
     */
    CommentDto addComment(Integer adId, String email, CreateOrUpdateCommentDto createComment);

    /**
     * Удаление комментария
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email текущего пользователя
     */
    void deleteComment(Integer adId, Integer commentId, String email);

    /**
     * Обновление комментария
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param email email текущего пользователя
     * @param updateComment новые данные комментария
     * @return обновлённый комментарий
     */
    CommentDto updateComment(Integer adId, Integer commentId, String email, CreateOrUpdateCommentDto updateComment);
}