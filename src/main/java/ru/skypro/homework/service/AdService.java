package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ad.ExtendedAdDto;

public interface AdService {

    /**
     * Получение всех объявлений.
     */
    AdsDto getAllAds();

    /**
     * Добавление нового объявления.
     *
     * @param email      email автора (из Authentication)
     * @param properties данные объявления
     * @param image      файл изображения
     * @return созданное объявление (AdDto)
     */
    AdDto addAd(String email, CreateOrUpdateAdDto properties, MultipartFile image);

    /**
     * Получение расширенной информации об объявлении по id.
     *
     * @param id идентификатор объявления
     * @return ExtendedAdDto
     */
    ExtendedAdDto getAd(Integer id);

    /**
     * Удаление объявления.
     *
     * @param id    идентификатор объявления
     * @param email email текущего пользователя
     */
    void removeAd(Integer id, String email);

    /**
     * Обновление информации об объявлении.
     *
     * @param id         идентификатор объявления
     * @param email      email текущего пользователя
     * @param updateAd   новые данные
     * @return обновлённое объявление (AdDto)
     */
    AdDto updateAd(Integer id, String email, CreateOrUpdateAdDto updateAd);

    /**
     * Получение всех объявлений текущего пользователя.
     *
     * @param email email автора
     * @return AdsDto с его объявлениями
     */
    AdsDto getAdsMe(String email);

    /**
     * Обновление картинки объявления.
     *
     * @param id    идентификатор объявления
     * @param email email текущего пользователя
     * @param image новый файл изображения
     * @return обновлённое изображение в виде байт
     */
    byte[] updateImage(Integer id, String email, MultipartFile image);
}