package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.post.PostRequest;
import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.models.PageResponse;
import com.course.travel_journal_web_service.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Посты")
public class PostController {
    private final PostService postService;

    @Operation(summary = "Создание поста")
    @PostMapping(value = "/create-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponse createPost(
            @RequestPart("post") @Valid PostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        return postService.createPost(request, image);
    }

    @Operation(summary = "Получение информации о посте")
    @GetMapping("/get-post-data/{post_id}")
    public PostResponse getPostData(@PathVariable Long post_id) {
        return postService.getPostData(post_id);
    }

    @Operation(summary = "Изменение информации в посте")
    @PutMapping(value = "/update-post-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponse updatePostData(@RequestPart("post") @Valid PostRequest request,
                                       @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        return postService.updatePostData(request, image);
    }

    @Operation(summary = "Удаление поста")
    @DeleteMapping("/delete-post/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable Long post_id) {
        postService.deletePost(post_id);
        return ResponseEntity.ok("Пост " + post_id +  " удален");
    }

    @Operation(summary = "Поставить лайк")
    @PostMapping("/like-post/{post_id}")
    public ResponseEntity<?> likePost(@PathVariable Long post_id) {
        return ResponseEntity.ok(postService.likePost(post_id));
    }

    @Operation(summary = "Получить данные о всех постах (пагинация)")
    @GetMapping("/get-posts-data")
    public PageResponse<PostResponse> createPost(@RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
                                                 @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit,
                                                 @RequestParam(value = "sort") String sort,
                                                 @RequestParam(value = "search", required = false) String search){
        return postService.findAllPosts(page, limit, sort, search);
    }
}
