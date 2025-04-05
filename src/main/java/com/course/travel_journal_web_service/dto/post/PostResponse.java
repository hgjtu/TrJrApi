package com.course.travel_journal_web_service.dto.post;

import com.course.travel_journal_web_service.models.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Ответ c данными о посте")
public class PostResponse {
    @Schema(description = "Данные о посте", example = "")
    private Post post;
}