package com.course.travel_journal_web_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

// TODO УДАЛИТЬ
@Data
@Schema(description = "Запрос на удаление пользователя")
public class UserDeleteRequest {
    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    private String username;
}
