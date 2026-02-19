package ru.skypro.homework.controller.auth;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.auth.LoginDto;
import ru.skypro.homework.dto.auth.RegisterDto;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void register_ShouldReturnCreated() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("newuser@test.com");
        registerDto.setPassword("password");
        registerDto.setFirstName("Новый");
        registerDto.setLastName("Пользователь");
        registerDto.setPhone("+7 (999) 888-77-66");
        registerDto.setRole(Role.USER);

        HttpEntity<RegisterDto> request = new HttpEntity<>(registerDto);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl() + "/register", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void register_WithExistingEmail_ShouldReturnBadRequest() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("existing@test.com");
        registerDto.setPassword("password");
        registerDto.setFirstName("Существующий");
        registerDto.setLastName("Пользователь");
        registerDto.setPhone("+7 (999) 111-22-33");
        registerDto.setRole(Role.USER);

        // Первая регистрация
        restTemplate.postForEntity(baseUrl() + "/register", new HttpEntity<>(registerDto), Void.class);

        // Повторная регистрация
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl() + "/register", new HttpEntity<>(registerDto), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_ShouldReturnOk() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("login@test.com");
        registerDto.setPassword("password");
        registerDto.setFirstName("Login");
        registerDto.setLastName("User");
        registerDto.setPhone("+7 (999) 555-44-33");
        registerDto.setRole(Role.USER);

        restTemplate.postForEntity(baseUrl() + "/register", new HttpEntity<>(registerDto), Void.class);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("login@test.com");
        loginDto.setPassword("password");

        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl() + "/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void login_WithWrongPassword_ShouldReturnUnauthorized() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("real@test.com");
        registerDto.setPassword("password");
        registerDto.setFirstName("Real");
        registerDto.setLastName("User");
        registerDto.setPhone("+7 (999) 555-44-33");
        registerDto.setRole(Role.USER);
        restTemplate.postForEntity(baseUrl() + "/register", new HttpEntity<>(registerDto), Void.class);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("real@test.com");
        loginDto.setPassword("wrong123"); // 8 символов, но неверный

        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto);
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl() + "/login", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}