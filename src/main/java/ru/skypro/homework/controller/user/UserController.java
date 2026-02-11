package ru.skypro.homework.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {

    @Operation(
            summary = "Обновление пароля",
            description = "Позволяет авторизованному пользователю изменить свой пароль")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Пароль успешно изменен",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Некорректные данные запроса",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Текущий пароль указан неверно",
                        content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(
            @Valid @RequestBody NewPassword newPassword, Authentication authentication) {

        log.info("Запрос на смену пароля");

        // Заглушка - возвращаем успешный ответ
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            description = "Возвращает данные текущего авторизованного пользователя")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Информация о пользователе успешно получена",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDto.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {

        log.info("Запрос информации о пользователе");

        // Заглушка - возвращаем DTO с дефолтными значениями
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("userDto@example.com");
        userDto.setFirstName("Иван");
        userDto.setLastName("Иванов");
        userDto.setPhone("+7 (999) 123-45-67");
        userDto.setRole(Role.USER);
        userDto.setImage("/images/avatar.jpg");

        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            description = "Обновляет данные пользователя: имя, фамилию и телефон")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Информация о пользователе успешно обновлена",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UpdateUser.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Некорректные данные запроса",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(
            @Valid @RequestBody UpdateUser updateUser, Authentication authentication) {

        log.info("Запрос на обновление данных пользователя");

        // Заглушка - возвращаем тот же DTO, который получили
        return ResponseEntity.ok(updateUser);
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            description = "Позволяет загрузить новый аватар пользователя")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Аватар успешно обновлен",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Некорректный формат файла",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(
                        responseCode = "413",
                        description = "Размер файла превышает допустимый лимит",
                        content = @Content(schema = @Schema(hidden = true)))
            })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(
            @RequestParam("image") MultipartFile image, Authentication authentication) {

        log.info("Запрос на обновление аватара пользователя");

        // Заглушка - возвращаем успешный ответ
        return ResponseEntity.ok().build();
    }
}
