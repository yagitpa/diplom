package ru.skypro.homework.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.auth.LoginDto;
import ru.skypro.homework.dto.auth.RegisterDto;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

@Slf4j
@Validated
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "API для регистрации и аутентификации пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Авторизация пользователя",
            description = "Вход пользователя в систему с использованием логина и пароля")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Успешная авторизация",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description = "Некорректные данные запроса (ошибка валидации)",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Неверные учетные данные (логин или пароль)",
                        content = @Content)
            })
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("Запрос на авторизацию пользователя: {}", loginDto.getUsername());

        if (authService.login(loginDto.getUsername(), loginDto.getPassword())) {
            log.info("Пользователь {} успешно авторизован", loginDto.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Неудачная попытка авторизации для пользователя: {}", loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создание нового пользователя в системе")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Пользователь успешно зарегистрирован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "400",
                        description =
                                "Некорректные данные запроса (ошибка валидации или пользователь уже существует)",
                        content = @Content)
            })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        log.info(
                "Запрос на регистрацию пользователя: {} {}",
                registerDto.getFirstName(),
                registerDto.getLastName());

        if (authService.register(registerDto)) {
            log.info("Пользователь {} успешно зарегистрирован", registerDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Неудачная попытка регистрации пользователя: {}", registerDto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
