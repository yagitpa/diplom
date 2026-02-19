package ru.skypro.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.AbstractIntegrationTest;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.model.AdsDao;
import ru.skypro.homework.model.UsersDao;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class AdRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private UsersDao createTestUser(String email) {
        UsersDao user = new UsersDao();
        user.setEmail(email);
        user.setPassword("pass");
        user.setFirstName("Тест");
        user.setLastName("Тестов");
        user.setPhone("+7 (999) 111-22-33");
        user.setRole(Role.USER);
        return userRepository.saveAndFlush(user);
    }

    @Test
    void findByAuthorId_ShouldReturnAdsForUser() {
        UsersDao user = createTestUser("author@test.com");

        AdsDao ad1 = new AdsDao();
        ad1.setTitle("Ad 1");
        ad1.setDescription("Desc 1");
        ad1.setPrice(100);
        ad1.setAuthor(user);
        adRepository.saveAndFlush(ad1);

        AdsDao ad2 = new AdsDao();
        ad2.setTitle("Ad 2");
        ad2.setDescription("Desc 2");
        ad2.setPrice(200);
        ad2.setAuthor(user);
        adRepository.saveAndFlush(ad2);

        List<AdsDao> found = adRepository.findByAuthorId(user.getId());
        assertThat(found).hasSize(2);
        assertThat(found).extracting(AdsDao::getTitle).containsExactlyInAnyOrder("Ad 1", "Ad 2");
    }

    @Test
    void findByAuthorId_ShouldReturnEmptyList_WhenNoAds() {
        UsersDao user = createTestUser("empty@test.com");
        List<AdsDao> found = adRepository.findByAuthorId(user.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void countByAuthorId_ShouldReturnCorrectCount() {
        UsersDao user1 = createTestUser("user1@test.com");
        UsersDao user2 = createTestUser("user2@test.com");

        AdsDao ad = new AdsDao();
        ad.setTitle("Ad");
        ad.setDescription("Desc");
        ad.setPrice(100);
        ad.setAuthor(user1);
        adRepository.saveAndFlush(ad);

        long count1 = adRepository.countByAuthorId(user1.getId());
        long count2 = adRepository.countByAuthorId(user2.getId());

        assertThat(count1).isEqualTo(1);
        assertThat(count2).isEqualTo(0);
    }

    @Test
    void findById_ShouldReturnAdWithAuthor() {
        UsersDao user = createTestUser("author@test.com");
        AdsDao ad = new AdsDao();
        ad.setTitle("Test Ad");
        ad.setDescription("Test Desc");
        ad.setPrice(500);
        ad.setAuthor(user);
        AdsDao saved = adRepository.saveAndFlush(ad);

        AdsDao found = adRepository.findById(saved.getPk()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getAuthor().getEmail()).isEqualTo(user.getEmail());
    }
}