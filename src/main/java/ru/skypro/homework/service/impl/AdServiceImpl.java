package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.AdDto;
import ru.skypro.homework.dto.ad.AdsDto;
import ru.skypro.homework.dto.ad.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ad.ExtendedAdDto;
import ru.skypro.homework.dto.auth.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdsDao;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.util.ImageHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @Value("${app.image.ad-dir}")
    private String adImageDir;

    @Override
    @Transactional(readOnly = true)
    public AdsDto getAllAds() {
        List<AdsDao> ads = adRepository.findAll();
        List<AdDto> adDtos = ads.stream()
                                .map(adMapper::toAdDto)
                                .collect(Collectors.toList());
        AdsDto result = new AdsDto();
        result.setCount(adDtos.size());
        result.setResults(adDtos);
        return result;
    }

    @Override
    public AdDto addAd(String email, CreateOrUpdateAdDto properties, MultipartFile image) {
        UsersDao author = getUserByEmail(email);
        String imagePath = saveImage(image);

        AdsDao ad = adMapper.toAdEntity(properties);
        ad.setAuthor(author);
        ad.setImage(imagePath);
        AdsDao savedAd = adRepository.save(ad);

        log.info("Ad created with id: {} by user: {}", savedAd.getPk(), email);
        return adMapper.toAdDto(savedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAdDto getAd(Integer id) {
        AdsDao ad = getAdById(id);
        return adMapper.toExtendedAdDto(ad);
    }

    @Override
    public void removeAd(Integer id, String email) {
        AdsDao ad = getAdById(id);
        checkPermissions(ad, email);

        if (ad.getImage() != null) {
            deleteImageFile(ad.getImage());
        }
        adRepository.delete(ad);
        log.info("Ad deleted with id: {} by user: {}", id, email);
    }

    @Override
    public AdDto updateAd(Integer id, String email, CreateOrUpdateAdDto updateAd) {
        AdsDao ad = getAdById(id);
        checkPermissions(ad, email);
        adMapper.updateAdFromDto(updateAd, ad);
        AdsDao updatedAd = adRepository.save(ad);
        log.info("Ad updated with id: {} by user: {}", id, email);
        return adMapper.toAdDto(updatedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public AdsDto getAdsMe(String email) {
        UsersDao author = getUserByEmail(email);
        List<AdsDao> ads = adRepository.findByAuthorId(author.getId());
        List<AdDto> adDtos = ads.stream()
                                .map(adMapper::toAdDto)
                                .collect(Collectors.toList());
        AdsDto result = new AdsDto();
        result.setCount(adDtos.size());
        result.setResults(adDtos);
        return result;
    }

    @Override
    public byte[] updateImage(Integer id, String email, MultipartFile image) {
        AdsDao ad = getAdById(id);
        checkPermissions(ad, email);

        String newImagePath = saveImage(image);
        if (ad.getImage() != null) {
            deleteImageFile(ad.getImage());
        }

        ad.setImage(newImagePath);
        adRepository.save(ad);
        log.info("Image updated for ad id: {} by user: {}", id, email);

        // Возвращаем массив байт нового изображения
        try {
            Path path = Paths.get(newImagePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Failed to read saved image file: {}", newImagePath, e);
            throw new RuntimeException("Failed to read image file", e);
        }
    }

    private UsersDao getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private AdsDao getAdById(Integer id) {
        return adRepository.findById(id)
                           .orElseThrow(() -> new AdNotFoundException("Ad not found with id: " + id));
    }

    private void checkPermissions(AdsDao ad, String email) {
        UsersDao user = getUserByEmail(email);
        boolean isAuthor = ad.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedAccessException("User does not have permission to modify this ad");
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String extension = ImageHelper.getExtension(image.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;
            Path uploadPath = Paths.get(adImageDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            image.transferTo(filePath.toFile());
            // Возвращаем относительный
            return "/ads-images/" + filename;
        } catch (IOException e) {
            log.error("Failed to save ad image", e);
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void deleteImageFile(String imagePath) {
        try {
            Path fullPath = Paths.get(adImageDir, Paths.get(imagePath).getFileName().toString());
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            log.warn("Failed to delete old image file: {}", imagePath, e);
        }
    }
}