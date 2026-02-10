package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skypro.homework.dto.comment.Comment;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ads",
        indexes = {
                @Index(columnList = "author_id", name = "idx_ads_author_id"),
                @Index(columnList = "price", name = "idx_ads_price"),
                @Index(columnList = "created_at", name = "idx_ads_created_at"),
                @Index(columnList = "title", name = "idx_ads_title")
        }
)
@Getter
@Setter
@ToString(exclude = {"author", "comments"})
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность объявления")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Schema(
            description = "Уникальный идентификатор объявления",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 1, max = 100, message = "Заголовок должен содержать от 1 до 100 символов")
    @Schema(
            description = "Заголовок объявления",
            example = "Продам ноутбук",
            minLength = 1,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @Column(name = "price", nullable = false)
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Max(value = 10000000, message = "Цена не может превышать 10 000 000")
    @Schema(
            description = "Цена объявления",
            example = "15000",
            minimum = "0",
            maximum = "10000000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer price;

    @Column(name = "description", nullable = false, length = 1000)
    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 1, max = 1000, message = "Описание должно содержать от 1 до 1000 символов")
    @Schema(
            description = "Описание объявления",
            example = "Отличный ноутбук в идеальном состоянии. 2 года использования.",
            minLength = 1,
            maxLength = 1000,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

    @Column(name = "image", length = 512)
    @Schema(
            description = "Путь к изображению объявления",
            example = "/images/ads/1/image.jpg",
            maxLength = 512
    )
    private String image;

    @Column(name = "author_id", nullable = false, insertable = false, updatable = false)
    @Schema(
            description = "ID автора объявления",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ads_user_id")
    )
    @Schema(description = "Автор объявления", hidden = true)
    private User author;

    @OneToMany(
            mappedBy = "ad",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Schema(description = "Список комментариев к объявлению", hidden = true)
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(
            description = "Дата и время создания объявления",
            example = "2023-10-01T12:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Schema(
            description = "Дата и время последнего обновления объявления",
            example = "2023-10-02T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    @Schema(
            description = "Флаг активности объявления",
            example = "true",
            defaultValue = "true"
    )
    private Boolean isActive = true;

}