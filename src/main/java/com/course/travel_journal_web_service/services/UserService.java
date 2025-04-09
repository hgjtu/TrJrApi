package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.dto.user.UserEditRequest;
import com.course.travel_journal_web_service.dto.user.UserResponse;
import com.course.travel_journal_web_service.dto.user.UserMinResponse;
import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.models.UserForResponse;
import com.course.travel_journal_web_service.repos.UserRepos;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectDeletedException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepos repository;
    private final MinioService minioService;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Обновление данных пользователя
     *
     * @param userEditRequest Запрос на изменение данных пользователя
     * @return Обновленные данные пользователя
     */
    public UserResponse updateUserData(UserEditRequest userEditRequest) {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();

        // Проверяем, существует ли пользователь с таким email, если email изменен
        if (userEditRequest.getEmail() != null && !userEditRequest.getEmail().equals(currentUser.getEmail())) {
            if (repository.existsByEmail(userEditRequest.getEmail())) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            currentUser.setEmail(userEditRequest.getEmail());
        }

        // Сохраняем обновленного пользователя
        User updatedUser = save(currentUser);

        // Возвращаем обновленные данные пользователя
        return UserResponse.builder()
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .build();
    }


    /**
     * Получение данных пользователя для отдачи
     *
     * @return данные о текущем пользователе
     */
    public UserResponse getUserData() {
        // Получение пользователя
        User user = getCurrentUser();

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    /**
     * Получение минимального кол-ва данных пользователя для отдачи
     *
     * @return минимальные данные о текущем пользователе
     */
    public UserMinResponse getUserMinData() {
        // Получение пользователя
        User user = getCurrentUser();

        var userForResponse = UserForResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        return new UserMinResponse(userForResponse);
    }

    /**
     * Удаление пользователя по имени пользователя
     *
     * @param username имя пользователя для удлаения
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    public void deleteUser(String username) {
        User currentUser = getCurrentUser();

        // Проверяем, что юзер хочет удалить самого себя
        if (currentUser.getUsername().equals(username)) {
            // Проверяем, существует ли пользователь с таким именем
            User user = repository.findByUsername(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Пользователь с именем "
                                    + username + " не найден"));

            // Удаляем пользователя из базы данных
            repository.delete(user);
        }
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
