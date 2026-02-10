package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(columnList = "author_id", name = "idx_comments_author_id"),
                @Index(columnList = "ad_id", name = "idx_comments_ad_id"),
                @Index(columnList = "created_at", name = "idx_comments_created_at")
        }
)
@Getter
@Setter
@ToString(exclude = {"author", "ad"})
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность комментария")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Schema(
            description = "Уникальный идентификатор комментария",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer id;

    @Column(name = "text", nullable = false, length = 1000)
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 1, max = 1000, message = "Текст комментария должен содержать от 1 до 1000 символов")
    @Schema(
            description = "Текст комментария",
            example = "Очень заинтересовало! 5.000 уступите?",
            minLength = 1,
            maxLength = 1000,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String text;

    @Column(name = "author_id", nullable = false, insertable = false, updatable = false)
    @Schema(
            description = "ID автора комментария",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comments_user_id")
    )
    @Schema(description = "Автор комментария", hidden = true)
    private User author;

    @Column(name = "ad_id", nullable = false, insertable = false, updatable = false)
    @Schema(
            description = "ID объявления, к которому относится комментарий",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer adId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ad_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comments_ad_id")
    )
    @Schema(description = "Объявление, к которому относится комментарий", hidden = true)
    private Ad ad;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Дата и время создания комментария в UTC",
            example = "2023-10-01T12:00:00Z",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Column(name = "created_at_millis", nullable = false, updatable = false)
    @Schema(
            description = "Дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970",
            example = "1641034800000",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long createdAtMillis;

    @Column(name = "is_active", nullable = false)
    @Schema(
            description = "Флаг активности комментария",
            example = "true",
            defaultValue = "true"
    )
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        if (createdAtMillis == null) {
            createdAtMillis = System.currentTimeMillis();
        }
    }

}