package com.course.travel_journal_web_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Ответ c данными о пользователе")
public class UserResponse {
    @Schema(description = "Логин пользователя", example = "Jon2000")
    private String username;
    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    private String email;
    @Schema(description = "Ссылка на изображение профиля", example = "")
    private String imageUrl;
}