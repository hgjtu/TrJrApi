package com.course.travel_journal_web_service.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
@Schema(description = "Объект пользователя для респонса")
public class UserForResponse {

    @Schema(description = "Имя пользователя", example = "Jon")
    private String username;

    @Schema(description = "Изображение профиля", example = "none-user-img")
    private String imageUrl;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

//    @Schema(description = "ID пользователя", example = "1")
//    private String id;
}
