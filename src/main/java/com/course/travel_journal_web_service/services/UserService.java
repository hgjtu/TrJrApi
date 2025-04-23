package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.dto.user.ChangePasswordRequest;
import com.course.travel_journal_web_service.dto.user.UserEditRequest;
import com.course.travel_journal_web_service.dto.user.UserResponse;
import com.course.travel_journal_web_service.dto.user.UserMinResponse;
import com.course.travel_journal_web_service.models.Post;
import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.models.UserForResponse;
import com.course.travel_journal_web_service.repos.UserRepos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectDeletedException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
//    private final String serviceName = "Users";
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
    public User createUser(User user) {
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
    public UserResponse updateUserData(UserEditRequest userEditRequest,
                                       MultipartFile imageFile) throws Exception {
        // Получаем текущего пользователя
        User currentUser = getCurrentUser();

        try{
            // Проверяем, существует ли пользователь с таким email, если email изменен
            if (userEditRequest.getEmail() != null && !userEditRequest.getEmail().equals(currentUser.getEmail())) {
                if (repository.existsByEmail(userEditRequest.getEmail())) {
                    throw new RuntimeException("Пользователь с таким email уже существует");
                }
                currentUser.setEmail(userEditRequest.getEmail());
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = minioService.uploadFile(imageFile);
                currentUser.setImageName(imageName);
            }

            // Сохраняем обновленного пользователя
            User updatedUser = save(currentUser);

            if (!minioService.fileExists(updatedUser.getImageName())) {
                resetUserImage();
            }

            // Возвращаем обновленные данные пользователя
            return UserResponse.builder()
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .image(minioService.getFileAsBase64(updatedUser.getImageName()))
                    .build();
        }
        catch (Exception e){
//            throw new RuntimeException(e.getMessage());
            return null;
        }
    }

    /**
     * Сброс фото профиля до базоыой картинки
     *
     */
    public void resetUserImage() {
        User currentUser = getCurrentUser();
        currentUser.setImageName("default-user-img.png");
        save(currentUser);
    }

    /**
     * Получение данных пользователя для отдачи
     *
     * @return данные о текущем пользователе
     */
    public UserResponse getUserData() {
        // Получение пользователя
        User user = getCurrentUser();

        if (!minioService.fileExists(user.getImageName())) {
            resetUserImage();
        }

        try {
            return UserResponse.builder()
                    .username(user.getUsername())
                    .image(minioService.getFileAsBase64(user.getImageName()))
                    .email(user.getEmail())
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * Получение минимального кол-ва данных пользователя для отдачи
     *
     * @return минимальные данные о текущем пользователе
     */
    public UserForResponse getUserMinData() {
        // Получение пользователя
        User user = getCurrentUser();

        try{
            if (!minioService.fileExists(user.getImageName())) {
                resetUserImage();
            }

            return UserForResponse.builder()
                    .username(user.getUsername())
                    .image(minioService.getFileAsBase64(user.getImageName()))
                    .role(user.getRole())
                    .build();
        }
        catch (Exception e){
            return null;
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

    /**
     * Получение признака, что пользователь лайкнул какой-то пост
     *
     * @param post id поста для проверки
     * @return boolean
     */
    public boolean isLikedPost(Post post) {
        return getCurrentUser().getLikedPosts().contains(post);
    }

    /**
     * Удалить упоминание пользователя из лайкнувших
     *
     * @param post пост
     */
    public void addLike(Post post) {
        User currentUser = getCurrentUser();

        post.getLikedUsers().add(currentUser);
        currentUser.getLikedPosts().add(post);

        save(currentUser);
    }

    /**
     * Удалить упоминание пользователя из лайкнувших
     *
     * @param post пост
     */
    public void deleteLike(Post post) {
        User currentUser = getCurrentUser();

        post.getLikedUsers().remove(currentUser);
        currentUser.getLikedPosts().remove(post);

        save(currentUser);
    }
}
