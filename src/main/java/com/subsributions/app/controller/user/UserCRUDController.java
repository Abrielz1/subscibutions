package com.subsributions.app.controller.user;

import com.subsributions.app.model.request.user.CreateAccountRequestDto;
import com.subsributions.app.model.request.user.UpdateUserAccountRequestDto;
import com.subsributions.app.model.response.user.UserResponseDto;
import com.subsributions.app.service.UserCRUDService;
import com.subsributions.app.util.Create;
import com.subsributions.app.util.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.List;

@Tag(name = "UserCRUDController", description = "Полное управление учетными записями пользователей")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserCRUDController {

    private final UserCRUDService userCRUDService;

    @Operation(
            summary = "Получение пользователя по email",
            description = "Возвращает полную информацию о пользователе по его email",
            parameters = @Parameter(
                    name = "email",
                    description = "Электронная почта пользователя",
                    example = "user@example.com",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь найден",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидный формат email"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден"
                    )
            }
    )
    @GetMapping("/by-email")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserByEmail(@NotBlank @Email @RequestParam(name = "email") String email) {

        log.info("User with email: {} was sent from users controller", email);

        return userCRUDService.getUserByEmail(email);
    }

    @Operation(
            summary = "Постраничное получение пользователей",
            description = "Возвращает список пользователей с пагинацией",
            parameters = {
                    @Parameter(
                            name = "from",
                            description = "Номер страницы (начиная с 0)",
                            example = "0",
                            schema = @Schema(minimum = "0", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Количество элементов на странице",
                            example = "10",
                            schema = @Schema(minimum = "1", maximum = "100", defaultValue = "10")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры пагинации"
                    )
            }
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUsersList(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("list of users were sent from users controller");

        return userCRUDService.getUsersList(PageRequest.of(from, size));
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает новую учетную запись пользователя в системе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                        {
                          "email": "user@example.com",
                          "password": "SecurePass123!"
                        }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Учетная запись успешно создана",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь с таким email уже существует"
                    )
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Validated(Create.class) @RequestBody CreateAccountRequestDto newUser) {

        log.info("Via AuthController User created account with email: {}", newUser.email());
        return userCRUDService.register(newUser);
    }

    @Operation(
            summary = "Обновление данных пользователя",
            description = "Позволяет изменить пароль пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                    {
                      "password": "NewSecurePass123!"
                    }
                """
                            )
                    )
            ),
            parameters = @Parameter(
                    name = "email",
                    description = "Электронная почта пользователя",
                    example = "user@example.com",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные успешно обновлены",
                            content = @Content(schema = @Schema(implementation = UserResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидные входные данные"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден"
                    )
            }
    )
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto editUserAccountByAdmin(@NotBlank @Email @RequestParam(name = "email") String email,
                                                  @Validated(Update.class)@RequestBody UpdateUserAccountRequestDto updateAccountDto) {

        log.info("Via Admin Controller Admin with email: {} delete task: {}", email, updateAccountDto);
        return userCRUDService.updateUserAccount(email, updateAccountDto);
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Полное удаление учетной записи пользователя из системы",
            parameters = @Parameter(
                    name = "email",
                    description = "Электронная почта пользователя",
                    example = "user@example.com",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден"
                    )
            }
    )
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserAccount(@NotBlank @Email @RequestParam(name = "email") String email) {

        log.info("Via Admin Controller User with email: {} was deleted on server", email);
        userCRUDService.deleteUserAccount(email);
    }
}
