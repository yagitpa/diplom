package ru.skypro.homework.controller.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.AbstractIntegrationTest;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CommentRepository commentRepository;

    @MockBean
    private ImageService imageService;

    private UsersDao testUser;
    private final String userEmail = "user@test.com";
    private final String userPassword = "password";

    @BeforeEach
    void setUp() {
        testUser = new UsersDao();
        testUser.setEmail(userEmail);
        testUser.setPassword(passwordEncoder.encode(userPassword));
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("+7 (999) 123-45-67");
        testUser.setRole(Role.USER);
        testUser.setImage("/avatars/old.jpg");
        userRepository.save(testUser);

        when(imageService.saveImage(any(MultipartFile.class), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    MultipartFile file = invocation.getArgument(0);
                    String originalFilename = file.getOriginalFilename();
                    String extension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    return "/avatars/" + UUID.randomUUID() + extension;
                });

        when(imageService.readImageAsBytes(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    String imagePath = invocation.getArgument(0);
                    return imagePath.contains("new") ? "new avatar".getBytes() : "old avatar".getBytes();
                });
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getUser_ShouldReturnUserInfo() {
        ResponseEntity<UserDto> response = withAuth(userEmail, userPassword)
                .getForEntity(baseUrl() + "/users/me", UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testUser.getId());
        assertThat(response.getBody().getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getBody().getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(response.getBody().getLastName()).isEqualTo(testUser.getLastName());
        assertThat(response.getBody().getPhone()).isEqualTo(testUser.getPhone());
        assertThat(response.getBody().getRole()).isEqualTo(testUser.getRole());
        assertThat(response.getBody().getImage()).isEqualTo(testUser.getImage());
    }

    @Test
    void updateUser_ShouldReturnUpdatedInfo() {
        UpdateUserDto update = new UpdateUserDto();
        update.setFirstName("Пётр");
        update.setLastName("Петров");
        update.setPhone("+7 (999) 999-99-99");

        ResponseEntity<UpdateUserDto> response = patchWithAuth(
                baseUrl() + "/users/me",
                update,
                UpdateUserDto.class,
                userEmail,
                userPassword
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Пётр");
        assertThat(response.getBody().getLastName()).isEqualTo("Петров");
        assertThat(response.getBody().getPhone()).isEqualTo("+7 (999) 999-99-99");

        UsersDao updated = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo("Пётр");
        assertThat(updated.getLastName()).isEqualTo("Петров");
        assertThat(updated.getPhone()).isEqualTo("+7 (999) 999-99-99");
    }

    @Test
    void setPassword_ShouldChangePassword() {
        NewPasswordDto passwordDto = new NewPasswordDto();
        passwordDto.setCurrentPassword(userPassword);
        passwordDto.setNewPassword("newPassword");

        HttpEntity<NewPasswordDto> request = new HttpEntity<>(passwordDto);
        ResponseEntity<Void> response = withAuth(userEmail, userPassword)
                .postForEntity(baseUrl() + "/users/set_password", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<UserDto> failedLogin = withAuth(userEmail, userPassword)
                .getForEntity(baseUrl() + "/users/me", UserDto.class);
        assertThat(failedLogin.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<UserDto> successLogin = withAuth(userEmail, "newPassword")
                .getForEntity(baseUrl() + "/users/me", UserDto.class);
        assertThat(successLogin.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void setPassword_WithWrongCurrent_ShouldReturnBadRequest() {
        NewPasswordDto passwordDto = new NewPasswordDto();
        passwordDto.setCurrentPassword("wrong123");
        passwordDto.setNewPassword("newPassword");

        HttpEntity<NewPasswordDto> request = new HttpEntity<>(passwordDto);
        ResponseEntity<Void> response = withAuth(userEmail, userPassword)
                .postForEntity(baseUrl() + "/users/set_password", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateUserImage_ShouldReturnOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource imagePart = new ByteArrayResource("new avatar".getBytes()) {
            @Override
            public String getFilename() {
                return "avatar.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imagePart);

        ResponseEntity<Void> response = patchMultipartWithAuth(
                baseUrl() + "/users/me/image",
                body,
                Void.class,
                userEmail,
                userPassword
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UsersDao updated = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updated.getImage()).startsWith("/avatars/");
        assertThat(updated.getImage()).isNotEqualTo(testUser.getImage());
    }
}