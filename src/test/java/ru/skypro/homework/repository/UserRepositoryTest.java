package ru.skypro.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.AbstractIntegrationTest;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.model.UsersDao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_ShouldPersistUser() {
        UsersDao user = new UsersDao();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setPhone("+7 (999) 123-45-67");
        user.setRole(Role.USER);

        UsersDao saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        UsersDao user = new UsersDao();
        user.setEmail("find@example.com");
        user.setPassword("pass");
        user.setFirstName("Ирина");
        user.setLastName("Иванова");
        user.setPhone("+7 (999) 765-43-21");
        user.setRole(Role.ADMIN);
        userRepository.saveAndFlush(user); // сохраняем и сбрасываем в БД

        Optional<UsersDao> found = userRepository.findByEmail("find@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Ирина");
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        UsersDao user = new UsersDao();
        user.setEmail("exists@example.com");
        user.setPassword("pass");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+7 (999) 111-22-33");
        user.setRole(Role.USER);
        userRepository.saveAndFlush(user);

        boolean exists = userRepository.existsByEmail("exists@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("notfound@example.com");
        assertThat(exists).isFalse();
    }
}