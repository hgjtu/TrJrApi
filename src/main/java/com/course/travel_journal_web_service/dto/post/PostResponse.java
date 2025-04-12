package com.course.travel_journal_web_service.dto.post;

import com.course.travel_journal_web_service.models.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Ответ c данными о посте")
public class PostResponse {
    @Schema(description = "Id поста", example = "1")
    private Long id;

    @Schema(description = "Название поста", example = "Невероятные виды Санторини")
    private String title;

    @Schema(description = "Username автора поста")
    private String author;

    @Schema(description = "Дата создания поста", example = "2023-05-15T10:30:00")
    private LocalDateTime date;

    @Schema(description = "Локация", example = "Санторини, Греция")
    private String location;

    @Schema(description = "Основной текст поста", example = "Потрясающие закаты, белоснежные дома и синее море...")
    private String description;

    @Schema(description = "Название изображения", example = "none-post-img")
    private String imageName;

    @Schema(description = "Кол-во лайков на посте", example = "123")
    private Long likes;

    @Schema(description = "Признак того, что пост лайкнут запросившим пользователем", example = "true")
    private Boolean isLiked;
}