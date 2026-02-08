package ru.skypro.homework.controller.ad;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "API для работы с объявлениями")
public class AdController {

    @Operation(
            summary = "Получение всех объявлений",
            description = "Возвращает список всех объявлений")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Список объявлений получен",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Ads.class)))
            })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Запрос на получение всех объявлений");
        // Заглушка
        Ads ads = new Ads();
        ads.setCount(0);
        return ResponseEntity.ok(ads);
    }

    @Operation(
            summary = "Добавление объявления",
            description = "Создание нового объявления с изображением")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Объявление создано",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Ad.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content)
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {
        log.info("Запрос на создание объявления");
        // Заглушка
        Ad ad = new Ad();
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @Operation(
            summary = "Получение информации об объявлении",
            description = "Возвращает расширенную информацию об объявлении")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Информация об объявлении получена",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ExtendedAd.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(
            @Parameter(description = "ID объявления") @PathVariable Integer id) {
        log.info("Запрос информации об объявлении с ID: {}", id);
        // Заглушка
        ExtendedAd extendedAd = new ExtendedAd();
        return ResponseEntity.ok(extendedAd);
    }

    @Operation(summary = "Удаление объявления", description = "Удаляет объявление по ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Объявление удалено",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Доступ запрещен",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            Authentication authentication) {
        log.info("Запрос на удаление объявления с ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Обновление информации об объявлении",
            description = "Обновляет данные объявления")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Объявление обновлено",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Ad.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Доступ запрещен",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateAd updateAd,
            Authentication authentication) {
        log.info("Запрос на обновление объявления с ID: {}", id);
        // Заглушка
        Ad ad = new Ad();
        return ResponseEntity.ok(ad);
    }

    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            description = "Возвращает список объявлений текущего пользователя")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Список объявлений получен",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = Ads.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content)
            })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Запрос на получение объявлений текущего пользователя");
        // Заглушка
        Ads ads = new Ads();
        ads.setCount(0);
        return ResponseEntity.ok(ads);
    }

    @Operation(
            summary = "Обновление картинки объявления",
            description = "Загружает новое изображение для объявления")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Изображение обновлено",
                        content = @Content),
                @ApiResponse(
                        responseCode = "401",
                        description = "Пользователь не авторизован",
                        content = @Content),
                @ApiResponse(
                        responseCode = "403",
                        description = "Доступ запрещен",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Объявление не найдено",
                        content = @Content)
            })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(
            @Parameter(description = "ID объявления") @PathVariable Integer id,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {
        log.info("Запрос на обновление изображения объявления с ID: {}", id);
        return ResponseEntity.ok().build();
    }
}
