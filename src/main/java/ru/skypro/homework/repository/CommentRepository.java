package ru.skypro.homework.repository;

import ru.skypro.homework.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // ---------- Методы без пагинации ----------

    /**
     * Получение всех комментариев к объявлению, отсортированных по дате создания (сначала новые).
     * Без пагинации – возвращает полный список, например, для экспорта.
     *
     * @param adPk идентификатор объявления (ad.pk)
     * @return список комментариев
     */
    List<Comment> findByAdPkOrderByCreatedAtDesc(Integer adPk);

    /**
     * Поиск комментария по его идентификатору и идентификатору объявления.
     *
     * @param id   идентификатор комментария (comment.pk)
     * @param adPk идентификатор объявления (ad.pk)
     * @return Optional с комментарием или пустой Optional
     */
    Optional<Comment> findByIdAndAdPk(Integer id, Integer adPk);

    /**
     * Поиск комментария по его идентификатору и идентификатору автора.
     *
     * @param id       идентификатор комментария (comment.pk)
     * @param authorId идентификатор автора (author.id)
     * @return Optional с комментарием или пустой Optional
     */
    Optional<Comment> findByIdAndAuthorId(Integer id, Integer authorId);

    // ---------- Методы с пагинацией ----------

    /**
     * Получение комментариев к объявлению с пагинацией и сортировкой по дате создания (новые первыми).
     *
     * @param adPk     идентификатор объявления (ad.pk)
     * @param pageable параметры пагинации и сортировки
     * @return страница комментариев
     */
    Page<Comment> findByAdPk(Integer adPk, Pageable pageable);

    /**
     * Подсчёт количества комментариев у объявления.
     *
     * @param adPk идентификатор объявления
     * @return количество комментариев
     */
    long countByAdPk(Integer adPk);
}