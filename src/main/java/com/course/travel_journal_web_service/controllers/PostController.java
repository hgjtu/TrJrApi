package com.course.travel_journal_web_service.controllers;

import com.course.travel_journal_web_service.dto.post.PostRequest;
import com.course.travel_journal_web_service.dto.post.PostResponse;
import com.course.travel_journal_web_service.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Посты")
public class PostController {
    private final PostService postService;

    @Operation(summary = "Изменение информации в посте")
    @PostMapping("/create-post")
    public PostResponse createPost(@RequestBody @Valid PostRequest request) {
        return postService.createPost(request);
    }

    @Operation(summary = "Получение информации о посте")
    @GetMapping("/get-post-data/{post_id}")
    public PostResponse getPostData(@PathVariable Long post_id) {
        return postService.getPostData(post_id);
    }

    @Operation(summary = "Изменение информации в посте")
    @PutMapping("/update-post-data")
    public PostResponse updatePostData(@RequestBody @Valid PostRequest request) {
        return postService.updatePostData(request);
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
        postService.likePost(post_id);
        return ResponseEntity.ok("Пост " + post_id +  " удален");
    }
}
