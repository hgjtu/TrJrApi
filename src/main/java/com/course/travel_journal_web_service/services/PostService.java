package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.dto.post.PostRequest;
import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.models.Post;
import com.course.travel_journal_web_service.models.PostLike;
import com.course.travel_journal_web_service.models.Role;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.repos.PostLikeRepos;
import com.course.travel_journal_web_service.repos.PostRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepos repository;
    private final PostLikeRepos postLikeRepos;
    private final UserService userService;

    /**
     * Сохранение поста
     *
     * @return сохраненный пост
     */
    public Post save(Post post) {
        return repository.save(post);
    }

    /**
     * Создание поста
     *
     * @return созданный пост
     */
    public PostResponse createPost(PostRequest createPostRequest) {
        // Получаем пользователя
        User currentUser = userService.getCurrentUser();

        Post newPost = new Post();

        newPost.setTitle(createPostRequest.getTitle());
        newPost.setAuthor(currentUser);
        newPost.setDate(createPostRequest.getDate());
        newPost.setLocation(createPostRequest.getLocation());
        newPost.setDescription(createPostRequest.getDescription());

        return PostResponse.builder()
                .post(save(newPost))
                .build();
    }

    /**
     * Обновление данных поста
     *
     * @param postEditRequest Запрос на изменение данных пользователя
     * @return Обновленные данные пользователя
     */
    public PostResponse updatePostData(PostRequest postEditRequest) {
        // Получаем пост по ID
        User currentUser = userService.getCurrentUser();

        // Проверяем, что пользователь изменяет свой пост
        if(!(Objects.equals(postEditRequest.getAuthor(), currentUser.getUsername())
                || currentUser.getRole() == Role.ROLE_ADMIN)){
            throw new RuntimeException("Нельзя изменить не свой пост");
        }

        Post post = getPostById(postEditRequest.getId());

        post.setTitle(postEditRequest.getTitle());
        post.setDate(postEditRequest.getDate());
        post.setLocation(postEditRequest.getLocation());
        post.setDescription(postEditRequest.getDescription());

        // Сохраняем обновленный пост
        Post newPost = save(post);

        // Возвращаем обновленные данные посте
        return PostResponse.builder()
                .post(newPost)
                .build();
    }

    /**
     * Получение данных поста для отдачи
     *
     * @param post_id id поста для поиска
     * @return данные о посте
     */
    public PostResponse getPostData(Long post_id) {
        // Получение поста
        Post post = getPostById(post_id);

        return PostResponse.builder()
                .post(post)
                .build();
    }

    /**
     * Удаление поста по id
     *
     * @param post_id id поста для удаления
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    public void deletePost(Long post_id) {
        User currentUser = userService.getCurrentUser();
        Post post = getPostById(post_id);

        // Проверяем, что пользователь удаляет свой пост
        if(!(Objects.equals(post.getAuthor().getUsername(), currentUser.getUsername())
                || currentUser.getRole() == Role.ROLE_ADMIN)){
            throw new RuntimeException("Нельзя удалить не свой пост");
        }
        repository.delete(post);
    }

    /**
     * Получение данных поста для отдачи
     *
     * @param post_id id поста для лайка
     */
    public void likePost(Long post_id) {
        PostLike postLike = new PostLike();
        postLike.setPost(getPostById(post_id));
        postLike.setUser(userService.getCurrentUser());

        postLikeRepos.save(postLike);

    }

    /**
     * Получение поста по id
     *
     * @return пост
     */
    public Post getPostById(Long post_id) {
        return repository.findById(post_id)
                .orElseThrow(() -> new ExpressionException("Пост не найден"));

    }

}
