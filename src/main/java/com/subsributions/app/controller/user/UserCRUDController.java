package com.subsributions.app.controller.user;

import com.subsributions.app.model.request.user.CreateAccountRequest;
import com.subsributions.app.model.request.user.UpdateUserAccountRequestDto;
import com.subsributions.app.model.response.user.UserResponseDto;
import com.subsributions.app.service.UserCRUDService;
import com.subsributions.app.util.Create;
import com.subsributions.app.util.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "UserCRUDController", description = "Контроллер для CRUD операций над пользователями")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserCRUDController {

    private final UserCRUDService userCRUDService;

    @Operation(
            summary = "Получение списка пользователей со limit/offset пагинацией",
            description = "\"Получение списка пользователей"
    )
    @GetMapping("/by-email")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserByEmail(@NotBlank @Email @RequestParam(name = "email") String email) {

        log.info("\nUser with email: {} was sent from users controller", email);

        return userCRUDService.getUserByEmail(email);
    }

    @Operation(
            summary = "Получение списка пользователей со limit/offset пагинацией",
            description = "\"Получение списка пользователей"
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUsersList(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("\nlist of users were sent from users controller");

        return userCRUDService.getUsersList(PageRequest.of(from / size, size));
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Validated(Create.class) @RequestBody CreateAccountRequest newUser) {

        log.info("%nVia AuthController User created account with email: {}", newUser.email());
        return userCRUDService.register(newUser);
    }

    @Operation(
            summary = "Редактирование пользователя (USER) по id (User)",
            description = "Позволяет редактирование пользователя"
    )
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto editUserAccountByAdmin(@NotBlank @Email @RequestParam(name = "email") String email,
                                                  @Validated(Update.class)@RequestBody UpdateUserAccountRequestDto updateAccountDto) {

        log.info("%nVia Admin Controller Admin with email: {} delete task: {}", email, updateAccountDto);
        return userCRUDService.updateUserAccount(email, updateAccountDto);
    }

    @Operation(
            summary = "Удаление пользователя (USER) по id (User) (ADMIN)",
            description = "Позволяет удалить пользователя (ADMIN)"
    )
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserAccount(@NotBlank @Email @RequestParam(name = "email") String email) {

        log.info("%nVia Admin Controller User with email: {} was deleted on server", email);
        userCRUDService.deleteUserAccount(email);
    }
}
