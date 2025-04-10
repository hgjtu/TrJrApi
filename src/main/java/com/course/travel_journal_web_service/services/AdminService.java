package com.course.travel_journal_web_service.services;


import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.repos.UserRepos;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends UserService {
    public AdminService(UserRepos repository, MinioService minioService) {
        super(repository, minioService);
    }

    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
