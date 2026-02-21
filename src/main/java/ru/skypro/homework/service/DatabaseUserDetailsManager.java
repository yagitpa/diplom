package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.exception.InvalidCurrentPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.UsersDao;
import ru.skypro.homework.repository.UserRepository;

/**
 * Кастомная реализация {@link UserDetailsManager}, обеспечивающая хранение и управление
 * пользователями в базе данных через {@link UserRepository}.
 * <p>
 * Используется вместо стандартного InMemoryUserDetailsManager для аутентификации
 * и авторизации на основе данных из БД. Предоставляет полный набор операций
 * управления пользователями:
 * <ul>
 *   <li>загрузка пользователя по имени (email) – {@link #loadUserByUsername(String)}</li>
 *   <li>создание нового пользователя – {@link #createUser(UserDetails)}</li>
 *   <li>обновление существующего – {@link #updateUser(UserDetails)}</li>
 *   <li>удаление – {@link #deleteUser(String)}</li>
 *   <li>смена пароля – {@link #changePassword(String, String)}</li>
 *   <li>проверка существования – {@link #userExists(String)}</li>
 * </ul>
 * <p>
 * В текущей реализации методы createUser, updateUser не используются напрямую,
 * так как регистрация выполняется через {@link ru.skypro.homework.service.AuthService}
 * с сохранением полной информации о пользователе (имя, фамилия, телефон). Однако они
 * оставлены для возможного расширения и соответствия контракту интерфейса.
 * <p>
 * Метод {@link #changePassword(String, String)} предназначен для смены пароля
 * аутентифицированным пользователем: он получает текущего пользователя из
 * контекста безопасности, проверяет старый пароль с помощью {@link PasswordEncoder}
 * и сохраняет новый зашифрованный пароль.
 *
 * @see UserDetailsManager
 * @see UserRepository
 * @see PasswordEncoder
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DatabaseUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Загружает пользователя по его email (username).
     *
     * @param username email пользователя
     * @return объект {@link UserDetails}, содержащий имя, пароль и роли
     * @throws UsernameNotFoundException если пользователь с таким email не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersDao user = userRepository.findByEmail(username)
                                      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
                   .username(user.getEmail())
                   .password(user.getPassword())
                   .roles(user.getRole().name())
                   .build();
    }

    /**
     * Создаёт нового пользователя в БД.
     * <p>
     * <b>Примечание:</b> Этот метод не используется при регистрации, так как
     * стандартный {@link UserDetails} не содержит полей firstName, lastName, phone.
     * Для полноценной регистрации используется отдельный метод
     * {@link ru.skypro.homework.service.AuthService#register(ru.skypro.homework.dto.auth.RegisterDto)}.
     *
     * @param user данные пользователя (только username, password, roles)
     * @throws UnsupportedOperationException всегда, так как метод не реализован
     */
    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Use register method with full data");
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param user данные пользователя
     * @throws UnsupportedOperationException всегда, так как метод не реализован
     */
    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Update user not supported via UserDetailsManager");
    }

    /**
     * Удаляет пользователя по email.
     *
     * @param username email удаляемого пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void deleteUser(String username) {
        UsersDao user = userRepository.findByEmail(username)
                                      .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        userRepository.delete(user);
    }

    /**
     * Изменяет пароль текущего аутентифицированного пользователя.
     * <p>
     * Получает имя пользователя из контекста безопасности, загружает его из БД,
     * проверяет соответствие старого пароля (через {@link PasswordEncoder#matches}),
     * кодирует новый пароль и сохраняет обновлённую сущность.
     *
     * @param oldPassword старый пароль (в открытом виде)
     * @param newPassword новый пароль (в открытом виде)
     * @throws UserNotFoundException          если пользователь не найден в БД
     * @throws InvalidCurrentPasswordException если старый пароль не совпадает
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersDao user = userRepository.findByEmail(currentUsername)
                                      .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param username email
     * @return true если пользователь существует, иначе false
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.existsByEmail(username);
    }
}