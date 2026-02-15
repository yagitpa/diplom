package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.util.ImageHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Сервис для работы с изображениями: сохранение, удаление, чтение
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    /**
     * Сохраняет файл в указанную директорию.
     *
     * @param image     загружаемый файл
     * @param directory корневая директория для сохранения (например, "./avatars")
     * @param urlPrefix префикс URL для доступа (например, "/avatars/")
     * @return относительный путь к файлу (например, "/avatars/file.jpg")
     */
    public String saveImage(MultipartFile image, String directory, String urlPrefix) {
        try {
            String extension = ImageHelper.getExtension(image.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;
            Path uploadPath = Paths.get(directory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            image.transferTo(filePath.toFile());
            return urlPrefix + filename;
        } catch (IOException e) {
            log.error("Failed to save image to {}", directory, e);
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Удаляет файл по его относительному пути.
     *
     * @param imagePath относительный путь (например, "/avatars/file.jpg")
     * @param directory корневая директория (например, "./avatars")
     */
    public void deleteImage(String imagePath, String directory) {
        if (imagePath == null) return;
        try {
            Path fullPath = Paths.get(directory, Paths.get(imagePath).getFileName().toString());
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            log.warn("Failed to delete image file: {}", imagePath, e);
        }
    }

    /**
     * Читает файл в массив байт.
     *
     * @param imagePath относительный путь к файлу
     * @param directory корневая директория
     * @return массив байт файла
     */
    public byte[] readImageAsBytes(String imagePath, String directory) {
        try {
            Path fullPath = Paths.get(directory, Paths.get(imagePath).getFileName().toString());
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            log.error("Failed to read image file: {}", imagePath, e);
            throw new RuntimeException("Failed to read image file", e);
        }
    }
}