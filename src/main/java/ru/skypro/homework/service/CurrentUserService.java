package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для получения пользователя по email. Используется в других сервисах.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UsersDao getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}