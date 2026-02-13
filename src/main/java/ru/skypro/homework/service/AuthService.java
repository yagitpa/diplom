package ru.skypro.homework.service;

import ru.skypro.homework.dto.auth.RegisterDto;

public interface AuthService {

    /**
     * Аутентификация пользователя.
     *
     * @param userName логин (email)
     * @param password пароль
     * @throws org.springframework.security.authentication.BadCredentialsException если неверные учетные данные
     */
    void login(String userName, String password);

    /**
     * Регистрация нового пользователя.
     *
     * @param registerDto данные регистрации
     * @throws ru.skypro.homework.exception.UserAlreadyExistsException если пользователь с таким email уже существует
     */
    void register(RegisterDto registerDto);
}