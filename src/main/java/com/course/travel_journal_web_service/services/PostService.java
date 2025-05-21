package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.CustomExceptions.ResourceNotFoundException;
import com.course.travel_journal_web_service.dto.post.PostRequest;
import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.models.*;
import com.course.travel_journal_web_service.repos.PostRepos;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.ExpressionException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
//    private final String serviceName = "Posts";

    private final PostRepos repository;
    private final UserService userService;

    @Autowired
    private MinioService minioService;

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
    public PostResponse createPost(PostRequest createPostRequest,
                                   MultipartFile imageFile) throws Exception {
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

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = minioService.uploadFile(imageFile);
            newPost.setImageName(imageName);
        } else {
            newPost.setImageName("default-post-img.png");
        }

        save(newPost);

//        if (!minioService.fileExists(newPost.getImageName())) {
//            resetPostImage(newPost.getId());
//        }

        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .author(newPost.getAuthor().getUsername())
                .date(newPost.getDate())
                .location(newPost.getLocation())
                .description(newPost.getDescription())
                .image(minioService.getFileAsBase64(newPost.getImageName()))
                .likes(newPost.getLikes())
                .isLiked(false)
                .status(newPost.getStatus())
                .build();
    }

    /**
     * Обновление данных поста
     *
     * @param postEditRequest Запрос на изменение данных поста
     * @return Обновленные данные поста
     */
    public PostResponse updatePostData(PostRequest postEditRequest,
                                       MultipartFile imageFile) throws Exception {
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

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = minioService.uploadFile(imageFile);
            post.setImageName(imageName);
        }

        // Сохраняем обновленный пост
        Post newPost = save(post);

//        if (!minioService.fileExists(newPost.getImageName())) {
//            resetPostImage(newPost.getId());
//        }

        // Возвращаем обновленные данные о посте
        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .author(newPost.getAuthor().getUsername())
                .date(newPost.getDate())
                .location(newPost.getLocation())
                .description(newPost.getDescription())
                .image(minioService.getFileAsBase64(newPost.getImageName()))
                .likes(newPost.getLikes())
                .isLiked(userService.isLikedPost(newPost))
                .status(newPost.getStatus())
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            try {
                isLiked = userService.isLikedPost(post);
            } catch (Exception ignored) { }
        }

//        if (!minioService.fileExists(post.getImageName())) {
//            resetPostImage(post.getId());
//        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor().getUsername())
                .date(post.getDate())
                .location(post.getLocation())
                .description(post.getDescription())
//                .imageUrl(minioService
//                        .getFileUrl(post.getImageName()))
                .image(minioService.getFileAsBase64(post.getImageName()))
                .likes(post.getLikes())
                .isLiked(isLiked)
                .status(post.getStatus())
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

        if (userService.isLikedPost(post)) {
            post.setLikes(post.getLikes() - 1);
            userService.deleteLike(post);
        }
        else {
            post.setLikes(post.getLikes() + 1);
            userService.addLike(post);
        }

        save(post);
        return post.getLikes();
    }

    /**
     * Установка статуса после модерации
     *
     * @param post_id id поста для установки статуса
     */
    public void resubmitPost(Long post_id) {
        // Получение поста
        Post post = getPostById(post_id);
        User currentUser = userService.getCurrentUser();

        // Проверяем, что пользователь переотправляет свой пост
        if(!(Objects.equals(post.getAuthor().getUsername(), currentUser.getUsername())
                || currentUser.getRole() == Role.ROLE_ADMIN)){
            throw new ExpressionException("Нельзя переотправить не свой пост");
        }

        post.setStatus(PostStatus.STATUS_NOT_CHECKED);
    }

    /**
     * Сброс фото поста до базовой картинки
     *
     */
    public void resetPostImage(Long post_id) {
        Post post = getPostById(post_id);
        post.setImageName("default-post-img.png");
        save(post);
    }

    /**
     * Получение поста по id
     *
     * @return пост
     */
    public Post getPostById(Long post_id) {
        return repository.findById(post_id)
                .orElseThrow(() -> new ResourceNotFoundException("Пост не найден"));

    }

    /**
     * Получение всех постов (с пагинацией и фильтрацией)
     *
     * @param page номер страницы
     * @param limit количество элементов на странице
     * @param search строка поиска в формате "author=...&title=...&location=...&startDate=...&endDate=..."
     * @return посты по запросу
     */
    public PageResponse<PostResponse> findAllPosts(Integer page, Integer limit, String sort, String search) {
        Specification<Post> spec = Specification.where(null);

        if(Objects.equals(sort, "my-posts")){
            String author = userService.getCurrentUser().getUsername();
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("author").get("username")), "%" + author.toLowerCase() + "%"));
        }
        else if(Objects.equals(sort, "moderator")){
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("status"), "%STATUS_NOT_CHECKED%"));
        }
        else{
            spec = spec.and((root, query, cb) ->
                    cb.notLike(root.get("status"), "%STATUS_DENIED%"));
        }

        if (search != null && !search.isEmpty()) {
            Map<String, String> searchParams = Arrays.stream(search.split("&"))
                    .map(param -> param.split("="))
                    .filter(pair -> pair.length == 2)
                    .collect(Collectors.toMap(
                            pair -> pair[0],
                            pair -> {
                                try {
                                    return URLDecoder.decode(pair[1], StandardCharsets.UTF_8.toString());
                                } catch (UnsupportedEncodingException e) {
                                    return pair[1];
                                }
                            }
                    ));

            // Фильтр по автору
            if (searchParams.containsKey("author") && !Objects.equals(sort, "my-posts")) {
                String author = searchParams.get("author");
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("author").get("username")), "%" + author.toLowerCase() + "%"));
            }

            // Фильтр по заголовку
            if (searchParams.containsKey("title")) {
                String title = searchParams.get("title");
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // Фильтр по местоположению
            if (searchParams.containsKey("location")) {
                String location = searchParams.get("location");
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            // Фильтр по дате (диапазон)
            if (searchParams.containsKey("startDate") || searchParams.containsKey("endDate")) {
                try {
                    LocalDateTime startDate = searchParams.containsKey("startDate")
                            ? LocalDateTime.parse(searchParams.get("startDate"))
                            : null;
                    LocalDateTime endDate = searchParams.containsKey("endDate")
                            ? LocalDateTime.parse(searchParams.get("endDate"))
                            : null;

                    spec = spec.and((root, query, cb) -> {
                        List<Predicate> predicates = new ArrayList<>();
                        if (startDate != null) {
                            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
                        }
                        if (endDate != null) {
                            predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
                        }
                        return cb.and(predicates.toArray(new Predicate[0]));
                    });
                } catch (DateTimeParseException e) {
                    // Логируем ошибку, но не прерываем выполнение
//                logger.error("Error parsing date filter parameters", e);
                }
            }
        }

        Page<Post> postPage;

        if(Objects.equals(sort, "my-posts")) {
            postPage = repository.findAll(spec, PageRequest.of(page, limit,
                    PostSort.STATUS_ASC.getSortValue()
                            .and(PostSort.DATE_DESC.getSortValue())));
        }
        else{
            postPage = repository.findAll(spec, PageRequest.of(page, limit,
                    PostSort.STATUS_DESC.getSortValue()
                            .and(PostSort.DATE_DESC.getSortValue())));
        }

        return getPageResponseFromPostPage(postPage);
    }

    /**
     * Получение самых популярных постов
     *
     * @return самые попоулярныек посты
     */
    public PageResponse<PostResponse> findRecommendedPosts() {
        Specification<Post> spec = Specification.where(null);

        spec = spec.and((root, query, cb) ->
                cb.notLike(root.get("status"), "%STATUS_DENIED%"));

        Page<Post> postPage = repository.findAll(spec, PageRequest.of(0, 5, PostSort.LIKES_DESC.getSortValue()));

        return getPageResponseFromPostPage(postPage);
    }

    @NotNull
    private PageResponse<PostResponse> getPageResponseFromPostPage(Page<Post> postPage) {
        List<PostResponse> content = postPage.getContent().stream()
                .map(post -> PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .author(post.getAuthor().getUsername())
                        .date(post.getDate())
                        .location(post.getLocation())
                        .description(post.getDescription())
                        .image(minioService.getFileAsBase64(post.getImageName()))
                        .likes(post.getLikes())
                        .isLiked(post.getIsLiked())
                        .status(post.getStatus())
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
