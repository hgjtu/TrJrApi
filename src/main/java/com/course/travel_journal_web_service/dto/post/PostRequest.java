package com.course.travel_journal_web_service.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Запрос на изменение поста")
public class PostRequest {
    @Schema(description = "Id поста", example = "1")
    private Long id;

    @Schema(description = "Название поста", example = "Невероятные виды Санторини")
    @NotBlank(message = "Название поста cannot be blank")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(description = "Локация", example = "Санторини, Греция")
    @NotBlank(message = "Локация cannot be blank")
    @Size(max = 100, message = "Локация должна содержать до 100 символов")
    private String location;

    @Schema(description = "Основной текст поста", example = "Потрясающие закаты, белоснежные дома и синее море...")
    @Size(max = 2000, message = "Основной текст поста must be up to 2000 characters")
    private String description;
}