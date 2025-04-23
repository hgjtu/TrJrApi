package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.models.Post;
import com.course.travel_journal_web_service.models.PostStatus;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.repos.UserRepos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ModeratorService extends UserService {
    private final PostService postService;

    public ModeratorService(UserRepos repository, MinioService minioService, PostService postService) {
        super(repository, minioService);
        this.postService = postService;
    }

    /**
     * Установка статуса после модерации
     *
     * @param post_id id поста для установки статуса
     * @param decision статус
     */
    public void setDecision(Long post_id, String decision) {
        // Получение поста
        Post post = postService.getPostById(post_id);

        switch (decision){
            case "approved":
                post.setStatus(PostStatus.STATUS_VERIFIED);
                break;
            case "rejected":
                post.setStatus(PostStatus.STATUS_DENIED);
                break;
            default:
                //ошибка в респонсе тут 409 какое-нибудь
                break;
        }
    }
}
