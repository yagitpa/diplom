package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.InvalidCurrentPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CurrentUserService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final ImageService imageService;

    @Value("${app.image.avatar-dir}")
    private String avatarDir;

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String email) {
        UsersDao user = currentUserService.getUserByEmail(email);
        return userMapper.toUserDto(user);
    }

    @Override
    public UpdateUserDto updateUser(String email, UpdateUserDto updateUserDto) {
        UsersDao user = currentUserService.getUserByEmail(email);
        userMapper.updateUserFromDto(updateUserDto, user);
        userRepository.save(user);
        return updateUserDto;
    }

    @Override
    public void setPassword(String email, NewPasswordDto newPasswordDto) {
        UsersDao user = currentUserService.getUserByEmail(email);
        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", email);
    }

    @Override
    public void updateUserImage(String email, MultipartFile image) {
        UsersDao user = currentUserService.getUserByEmail(email);
        String newImagePath = imageService.saveImage(image, avatarDir, "/avatars/");
        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage(), avatarDir);
        }
        user.setImage(newImagePath);
        userRepository.save(user);
    }
}