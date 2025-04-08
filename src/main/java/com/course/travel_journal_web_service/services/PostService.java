package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.dto.post.PostRequest;
import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.models.*;
import com.course.travel_journal_web_service.repos.PostLikeRepos;
import com.course.travel_journal_web_service.repos.PostRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;
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

        if(currentUser == null){ //это не выполнится раньше упадет по ошибке
            throw new ExpressionException("You are not logged in");
        }

        Post newPost = new Post();

        newPost.setTitle(createPostRequest.getTitle());
        newPost.setAuthor(currentUser);
        newPost.setDate(java.time.LocalDateTime.now());
        newPost.setLocation(createPostRequest.getLocation());
        newPost.setDescription(createPostRequest.getDescription());

        save(newPost);

        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .author(newPost.getAuthor().getUsername())
                .date(newPost.getDate())
                .location(newPost.getLocation())
                .description(newPost.getDescription())
                .likes(newPost.getLikes())
                .isLiked(false)
                .build();
    }

    /**
     * Обновление данных поста
     *
     * @param postEditRequest Запрос на изменение данных поста
     * @return Обновленные данные поста
     */
    public PostResponse updatePostData(PostRequest postEditRequest) {
        // Получаем пост по ID
        User currentUser = userService.getCurrentUser();
        Post post = getPostById(postEditRequest.getId());

        // Проверяем, что пользователь изменяет свой пост
        if(!(Objects.equals(post.getAuthor().getUsername(), currentUser.getUsername())
                || currentUser.getRole() == Role.ROLE_ADMIN)){
            throw new RuntimeException("Нельзя изменить не свой пост");
        }

        post.setTitle(postEditRequest.getTitle());
        post.setLocation(postEditRequest.getLocation());
        post.setDescription(postEditRequest.getDescription());

        // Сохраняем обновленный пост
        Post newPost = save(post);

        // Возвращаем обновленные данные о посте
        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .author(newPost.getAuthor().getUsername())
                .date(newPost.getDate())
                .location(newPost.getLocation())
                .description(newPost.getDescription())
                .likes(newPost.getLikes())
                .isLiked(postLikeRepos.
                        existsByUserAndPost(currentUser,newPost))
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

        boolean isLiked = false;

        try{
            User currentUser = userService.getCurrentUser();
            isLiked = postLikeRepos
                    .existsByUserAndPost(currentUser, post);
        }
        catch (Exception ignored){  }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor().getUsername())
                .date(post.getDate())
                .location(post.getLocation())
                .description(post.getDescription())
                .likes(post.getLikes())
                .isLiked(isLiked)
                .build();
    }

    /**
     * Удаление поста по id
     *
     * @param post_id id поста для удаления
     * @throws ExpressionException если пользователь удаляет не свой пост
     */
    public void deletePost(Long post_id) {
        User currentUser = userService.getCurrentUser();
        Post post = getPostById(post_id);

        // Проверяем, что пользователь удаляет свой пост
        if(!(Objects.equals(post.getAuthor().getUsername(), currentUser.getUsername())
                || currentUser.getRole() == Role.ROLE_ADMIN)){
            throw new ExpressionException("Нельзя удалить не свой пост");
        }
        repository.delete(post);
    }

    /**
     * Получение данных поста для отдачи
     *
     * @param post_id id поста для лайка
     */
    public Long likePost(Long post_id) {
        Post post = getPostById(post_id);
        PostLike postLike = new PostLike();

        if (postLikeRepos.existsByUserAndPost(
                userService.getCurrentUser(),
                getPostById(post_id))) {

            post.setLikes(post.getLikes() - 1);

            postLike = postLikeRepos.findByUserAndPost(userService.getCurrentUser(), post)
                    .orElseThrow(() -> new ExpressionException("Ошибка при попытке лайка"));
            postLikeRepos.delete(postLike);
        }
        else {
            post.setLikes(post.getLikes() + 1);

            postLike.setPost(getPostById(post_id));
            postLike.setUser(userService.getCurrentUser());
            postLikeRepos.save(postLike);
        }

        return post.getLikes();
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

    /**
     * Получение всех постов (с пагинацией)
     *
     * @return пост
     */
    public PageResponse<PostResponse> findAllPosts(Integer page, Integer limit, String sort, String search) {
        Page<Post> postPage = switch (sort) {
            case "subscriptions" -> repository.findAll(PageRequest.of(page, limit, PostSort.LIKES_DESC.getSortValue()));
            case "my-posts" -> repository.findAll(PageRequest.of(page, limit, PostSort.LIKES_ASC.getSortValue()));
            default -> repository.findAll(PageRequest.of(page, limit, PostSort.DATE_DESC.getSortValue()));
        };

        List<PostResponse> content = postPage.getContent().stream()
                .map(post -> PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .author(post.getAuthor().getUsername())
                        .date(post.getDate())
                        .location(post.getLocation())
                        .description(post.getDescription())
                        .likes(post.getLikes())
                        .isLiked(post.getIsLiked())
                        .build())
                .toList();

        return new PageResponse<>(
                content,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isFirst(),
                postPage.isLast()
        );
    }


}
