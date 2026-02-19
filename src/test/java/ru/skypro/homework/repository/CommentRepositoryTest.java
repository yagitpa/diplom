package ru.skypro.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.AbstractIntegrationTest;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.model.AdsDao;
import ru.skypro.homework.model.CommentsDao;
import ru.skypro.homework.model.UsersDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private UsersDao createUser(String email) {
        UsersDao user = new UsersDao();
        user.setEmail(email);
        user.setPassword("pass");
        user.setFirstName("Имя");
        user.setLastName("Фамилия");
        user.setPhone("+7 (999) 111-22-33");
        user.setRole(Role.USER);
        return userRepository.saveAndFlush(user);
    }

    private AdsDao createAd(UsersDao author) {
        AdsDao ad = new AdsDao();
        ad.setTitle("Ad Title");
        ad.setDescription("Ad Description");
        ad.setPrice(1000);
        ad.setAuthor(author);
        return adRepository.saveAndFlush(ad);
    }

    private CommentsDao createComment(UsersDao author, AdsDao ad, String text, LocalDateTime createdAt) {
        CommentsDao comment = new CommentsDao();
        comment.setText(text);
        comment.setCreatedAt(createdAt);
        comment.setAuthor(author);
        comment.setAd(ad);
        return commentRepository.saveAndFlush(comment);
    }

    @Test
    void findByAdPkOrderByCreatedAtDesc_ShouldReturnCommentsSorted() {
        UsersDao author = createUser("author@test.com");
        AdsDao ad = createAd(author);

        LocalDateTime now = LocalDateTime.now();
        CommentsDao comment1 = createComment(author, ad, "First", now.minusHours(2));
        CommentsDao comment2 = createComment(author, ad, "Second", now.minusHours(1));
        CommentsDao comment3 = createComment(author, ad, "Third", now);

        List<CommentsDao> comments = commentRepository.findByAdPkOrderByCreatedAtDesc(ad.getPk());

        assertThat(comments).hasSize(3);
        assertThat(comments.get(0).getText()).isEqualTo("Third");
        assertThat(comments.get(1).getText()).isEqualTo("Second");
        assertThat(comments.get(2).getText()).isEqualTo("First");
    }

    @Test
    void findByPkAndAdPk_ShouldReturnComment_WhenExists() {
        UsersDao author = createUser("author@test.com");
        AdsDao ad = createAd(author);
        CommentsDao comment = createComment(author, ad, "Test comment", LocalDateTime.now());

        Optional<CommentsDao> found = commentRepository.findByPkAndAdPk(comment.getPk(), ad.getPk());

        assertThat(found).isPresent();
        assertThat(found.get().getText()).isEqualTo("Test comment");
    }

    @Test
    void findByPkAndAdPk_ShouldReturnEmpty_WhenNotMatch() {
        UsersDao author = createUser("author@test.com");
        AdsDao ad1 = createAd(author);
        AdsDao ad2 = createAd(author);
        CommentsDao comment = createComment(author, ad1, "Test", LocalDateTime.now());

        Optional<CommentsDao> found = commentRepository.findByPkAndAdPk(comment.getPk(), ad2.getPk());
        assertThat(found).isEmpty();
    }

    @Test
    void findByPkAndAuthorId_ShouldReturnComment_WhenAuthorMatches() {
        UsersDao author = createUser("author@test.com");
        UsersDao other = createUser("other@test.com");
        AdsDao ad = createAd(author);
        CommentsDao comment = createComment(author, ad, "Author comment", LocalDateTime.now());

        Optional<CommentsDao> found = commentRepository.findByPkAndAuthorId(comment.getPk(), author.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getText()).isEqualTo("Author comment");

        Optional<CommentsDao> notFound = commentRepository.findByPkAndAuthorId(comment.getPk(), other.getId());
        assertThat(notFound).isEmpty();
    }

    @Test
    void countByAdPk_ShouldReturnCorrectCount() {
        UsersDao author = createUser("author@test.com");
        AdsDao ad = createAd(author);
        createComment(author, ad, "C1", LocalDateTime.now());
        createComment(author, ad, "C2", LocalDateTime.now());

        long count = commentRepository.countByAdPk(ad.getPk());
        assertThat(count).isEqualTo(2);
    }
}