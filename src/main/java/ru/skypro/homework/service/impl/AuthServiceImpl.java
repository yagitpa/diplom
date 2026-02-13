package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.auth.RegisterDto;
import ru.skypro.homework.exception.UserAlreadyExistsException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void login(String userName, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("User {} successfully authenticated", userName);
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", userName);
            throw e; // пробрасываем дальше
        }
    }

    @Override
    public void register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getUsername())) {
            log.warn("Registration failed: user {} already exists", registerDto.getUsername());
            throw new UserAlreadyExistsException("User with email " + registerDto.getUsername() + " already exists");
        }
        UsersDao user = userMapper.toUserEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(user);
        log.info("User {} successfully registered", registerDto.getUsername());
    }
}