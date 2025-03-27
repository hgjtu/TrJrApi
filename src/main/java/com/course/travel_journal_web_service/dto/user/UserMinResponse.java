package com.course.travel_journal_web_service.dto.user;

import com.course.travel_journal_web_service.models.UserForResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Ответ c минимальными данными о пользователе")
public class UserMinResponse {
    @Schema(description = "Данные о пользователе", example = "")
    private UserForResponse user;
}