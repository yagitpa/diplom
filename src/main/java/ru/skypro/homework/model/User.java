package ru.skypro.homework.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.dto.comment.Comment;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "uk_users_email"),
                @UniqueConstraint(columnNames = "username", name = "uk_users_username")
        },
        indexes = {
                @Index(columnList = "email", name = "idx_users_email"),
                @Index(columnList = "username", name = "idx_users_username"),
                @Index(columnList = "role", name = "idx_users_role")
        }
)
@Getter
@Setter
@ToString(exclude = {"ads", "comments"})
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность пользователя системы")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Schema(description = "Уникальный идентификатор пользователя",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer id;

    @Column(name = "username", nullable = false, length = 32)
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 32, message = "Логин должен быть от 4 до 32 символов")
    @Schema(
            description = "Логин пользователя (уникальный идентификатор для входа)",
            example = "user123",
            minLength = 4,
            maxLength = 32,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @Column(name = "password", nullable = false, length = 256)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Пароль должен быть от 8 до 256 символов")
    @Schema(
            description = "Хешированный пароль пользователя",
            example = "$2a$10$X5eC5bB6N7Q8Zz1q2w3e4rT5y6u7i8o9p0Q1w2e3r4t5y6u7i8o9p",
            minLength = 8,
            maxLength = 256,
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Column(name = "first_name", nullable = false, length = 32)
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 32, message = "Имя должно быть от 2 до 32 символов")
    @Schema(
            description = "Имя пользователя",
            example = "Иван",
            minLength = 2,
            maxLength = 32,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 32)
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 32, message = "Фамилия должна быть от 2 до 32 символов")
    @Schema(
            description = "Фамилия пользователя",
            example = "Иванов",
            minLength = 2,
            maxLength = 32,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Column(name = "phone", nullable = false, length = 20)
    @NotBlank(message = "Телефон не может быть пустым")
    @Size(max = 20, message = "Телефон не может превышать 20 символов")
    @Pattern(
            regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен соответствовать формату +7 (XXX) XXX-XX-XX"
    )
    @Schema(
            description = "Телефон пользователя в формате +7 (XXX) XXX-XX-XX",
            example = "+7 (999) 123-45-67",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phone;

    @Column(name = "email", length = 255)
    @Email(message = "Email должен быть валидным")
    @Size(max = 255, message = "Email не может превышать 255 символов")
    @Schema(
            description = "Электронная почта пользователя",
            example = "user@example.com",
            maxLength = 255
    )
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    @Schema(
            description = "Роль пользователя в системе",
            example = "USER",
            allowableValues = {"USER", "ADMIN"},
            defaultValue = "USER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Role role = Role.USER;

    @Column(name = "image", length = 512)
    @Schema(
            description = "Путь к изображению аватара пользователя",
            example = "/images/avatar/1.jpg",
            maxLength = 512
    )
    private String image;

    @OneToMany(
            mappedBy = "authorId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Schema(description = "Список объявлений пользователя", hidden = true)
    private List<Ad> ads = new ArrayList<>();

    @OneToMany(
            mappedBy = "authorId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Schema(description = "Список комментариев пользователя", hidden = true)
    private List<Comment> comments = new ArrayList<>();
}