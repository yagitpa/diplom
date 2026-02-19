package ru.skypro.homework.controller.comment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.model.AdsDao;
import ru.skypro.homework.model.CommentsDao;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.AbstractIntegrationTest;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CommentRepository commentRepository;

    private UsersDao author;
    private UsersDao otherUser;
    private UsersDao admin;
    private AdsDao ad;
    private CommentsDao comment;

    private final String authorPassword = "password";
    private final String otherPassword = "password";
    private final String adminPassword = "admin";

    @BeforeEach
    void setUp() {
        createImageDirectories();

        author = new UsersDao();
        author.setEmail("author@test.com");
        author.setPassword(passwordEncoder.encode(authorPassword));
        author.setFirstName("Автор");
        author.setLastName("Комментария");
        author.setPhone("+7 (999) 111-11-11");
        author.setRole(Role.USER);
        userRepository.save(author);

        otherUser = new UsersDao();
        otherUser.setEmail("other@test.com");
        otherUser.setPassword(passwordEncoder.encode(otherPassword));
        otherUser.setFirstName("Другой");
        otherUser.setLastName("Пользователь");
        otherUser.setPhone("+7 (999) 222-22-22");
        otherUser.setRole(Role.USER);
        userRepository.save(otherUser);

        admin = new UsersDao();
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName("Админ");
        admin.setLastName("Админов");
        admin.setPhone("+7 (999) 333-33-33");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        ad = new AdsDao();
        ad.setTitle("Ad for comments");
        ad.setDescription("Description");
        ad.setPrice(500);
        ad.setAuthor(author);
        ad.setImage("/ads-images/ad.jpg");
        adRepository.save(ad);

        comment = new CommentsDao();
        comment.setText("Original comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(author);
        comment.setAd(ad);
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getComments_ShouldReturnList() {
        ResponseEntity<CommentsDto> response = withAuth(author.getEmail(), authorPassword)
                .getForEntity(baseUrl() + "/ads/{adId}/comments", CommentsDto.class, ad.getPk());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCount()).isEqualTo(1);
        assertThat(response.getBody().getResults()).hasSize(1);
        assertThat(response.getBody().getResults().get(0).getText()).isEqualTo("Original comment");
    }

    @Test
    void addComment_ShouldCreateComment() {
        CreateOrUpdateCommentDto newComment = new CreateOrUpdateCommentDto();
        newComment.setText("New comment");

        HttpEntity<CreateOrUpdateCommentDto> request = new HttpEntity<>(newComment);
        ResponseEntity<CommentDto> response = withAuth(otherUser.getEmail(), otherPassword)
                .postForEntity(baseUrl() + "/ads/{adId}/comments", request, CommentDto.class, ad.getPk());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getText()).isEqualTo("New comment");
        assertThat(response.getBody().getAuthor()).isEqualTo(otherUser.getId());
        assertThat(response.getBody().getAuthorFirstName()).isEqualTo(otherUser.getFirstName());

        assertThat(commentRepository.countByAdPk(ad.getPk())).isEqualTo(2);
    }

    @Test
    void deleteComment_ByAuthor_ShouldReturnOk() {
        ResponseEntity<Void> response = withAuth(author.getEmail(), authorPassword)
                .exchange(baseUrl() + "/ads/{adId}/comments/{commentId}",
                        HttpMethod.DELETE, null, Void.class, ad.getPk(), comment.getPk());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentRepository.findById(comment.getPk())).isEmpty();
    }

    @Test
    void deleteComment_ByAdmin_ShouldReturnOk() {
        ResponseEntity<Void> response = withAuth(admin.getEmail(), adminPassword)
                .exchange(baseUrl() + "/ads/{adId}/comments/{commentId}",
                        HttpMethod.DELETE, null, Void.class, ad.getPk(), comment.getPk());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentRepository.findById(comment.getPk())).isEmpty();
    }

    @Test
    void deleteComment_ByOtherUser_ShouldReturnForbidden() {
        ResponseEntity<Void> response = withAuth(otherUser.getEmail(), otherPassword)
                .exchange(baseUrl() + "/ads/{adId}/comments/{commentId}",
                        HttpMethod.DELETE, null, Void.class, ad.getPk(), comment.getPk());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(commentRepository.findById(comment.getPk())).isPresent();
    }

    @Test
    void updateComment_ByAuthor_ShouldReturnUpdated() {
        CreateOrUpdateCommentDto update = new CreateOrUpdateCommentDto();
        update.setText("Updated comment");

        ResponseEntity<CommentDto> response = patchWithAuth(
                baseUrl() + "/ads/{adId}/comments/{commentId}",
                update,
                CommentDto.class,
                author.getEmail(),
                authorPassword,
                ad.getPk(),
                comment.getPk()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getText()).isEqualTo("Updated comment");
        assertThat(response.getBody().getPk()).isEqualTo(comment.getPk());

        CommentsDao updated = commentRepository.findById(comment.getPk()).orElseThrow();
        assertThat(updated.getText()).isEqualTo("Updated comment");
    }

    @Test
    void updateComment_ByOtherUser_ShouldReturnForbidden() {
        CreateOrUpdateCommentDto update = new CreateOrUpdateCommentDto();
        update.setText("Hacked comment");

        ResponseEntity<Void> response = patchWithAuth(
                baseUrl() + "/ads/{adId}/comments/{commentId}",
                update,
                Void.class,
                otherUser.getEmail(),
                otherPassword,
                ad.getPk(),
                comment.getPk()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        CommentsDao unchanged = commentRepository.findById(comment.getPk()).orElseThrow();
        assertThat(unchanged.getText()).isEqualTo("Original comment");
    }
}