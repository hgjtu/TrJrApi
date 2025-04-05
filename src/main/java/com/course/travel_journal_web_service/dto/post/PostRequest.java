package com.course.travel_journal_web_service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление, изменение поста")
public class PostRequest {
    @Schema(description = "Id поста", example = "1")
    private Long id;

    @Schema(description = "Название поста", example = "Невероятные виды Санторини")
    @NotBlank(message = "Название поста cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(description = "Username автора поста")
    @NotNull(message = "Username автора поста cannot be null")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String author;

    @Schema(description = "Дата создания поста", example = "2023-05-15T10:30:00")
    @NotNull(message = "Дата создания поста cannot be null")
    private LocalDateTime date;

    @Schema(description = "Локация", example = "Санторини, Греция")
    @NotBlank(message = "Локация cannot be blank")
    @Size(max = 100, message = "Локация должна содержать до 100 символов")
    private String location;

    @Schema(description = "Основной текст поста", example = "Потрясающие закаты, белоснежные дома и синее море...")
    @Size(max = 2000, message = "Основной текст поста must be up to 2000 characters")
    private String description;
}