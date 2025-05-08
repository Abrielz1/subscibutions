package com.subsributions.app.controller.subscribution;

import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;
import com.subsributions.app.service.SubscriptionService;
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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "SubscriptionController", description = "Контроллер для операций над подпиской")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/subscribe")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Получает топ-N активных подписок.
     * - Проверяет валидность параметра top (1 ≤ top ≤ 100).
     */
    @Operation(
            summary = "Создание новой подписки",
            description = "Создает новую подписку для пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "email": "user@example.com",
                                                  "name": "Premium Access",
                                                  "startDate": "2024-01-01",
                                                  "endDate": "2024-12-31"
                                                }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Подписка успешно создана",
                            content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Подписка с таким именем уже существует"
                    )
            }
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto createUsersSubscription(@NotNull @Validated(Create.class) @RequestBody CreateUsersSubscriptionRequestDto newSubscription) {

        return subscriptionService.createUsersSubscription(newSubscription);
    }

    @Operation(
            summary = "Продление срока действия подписки",
            description = "Обновляет дату окончания активной подписки",
            parameters = @Parameter(
                    name = "email",
                    description = "Электронная почта пользователя",
                    example = "user@example.com",
                    required = true
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "name": "Premium Access",
                                                  "endDate": "2025-12-31"
                                                }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подписка успешно продлена",
                            content = @Content(schema = @Schema(implementation = UserExtendSubscriptionResponseDto.class))
                    )

            }
    )
    @PutMapping("/extend")
    @ResponseStatus(HttpStatus.OK)
    public UserExtendSubscriptionResponseDto extendSubscription(@NotNull @Validated(Update.class) @RequestBody UserExtendSubscriptionRequestDto extendSubscribe) {

        return subscriptionService.extendSubscription(extendSubscribe);
    }

    @Operation(
            summary = "Отмена подписки",
            description = "Помечает подписку как отклоненную",
            parameters = @Parameter(
                    name = "email",
                    description = "Электронная почта пользователя",
                    example = "user@example.com",
                    required = true
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                                {
                                                  "name": "Premium Access"
                                                }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Подписка успешно отменена"),
                    @ApiResponse(responseCode = "404", description = "Подписка не найдена")
            }
    )
    @PutMapping("/decline")
    @ResponseStatus(HttpStatus.OK)
    public void declineSubscription(@NotNull @Validated(Update.class) @RequestBody UserDeclinedSubscriptionRequestDto decline) {

        subscriptionService.declineSubscription(decline);
    }

    @Operation(
            summary = "Получение списка активных подписок",
            description = "Возвращает подписки с пагинацией",
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
                            description = "Список активных подписок",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubscriptionResponseShortDto.class)))
                    )
            }
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscriptionResponseShortDto> getAllActiveSubscriptions(@Positive @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer size) {

        return subscriptionService.getAllActiveSubscriptions(PageRequest.of(from, size));
    }

    @Operation(
            summary = "Получение подписки по идентификатору",
            description = "Возвращает полную информацию о подписке",
            parameters = @Parameter(
                    name = "id",
                    description = "Идентификатор подписки",
                    example = "123",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о подписке",
                            content = @Content(schema = @Schema(implementation = SubscriptionResponseShortDto.class))
                    )
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubscriptionResponseShortDto getSubscriptionById(@NotBlank @PathVariable(name = "id") Long id) {

        return subscriptionService.getSubscriptionById(id);
    }

    @Operation(
            summary = "Получение самых популярных подписок",
            description = "Возвращает топ-N подписок по количеству активных пользователей",
            parameters = @Parameter(
                    name = "top",
                    description = "Количество возвращаемых подписок",
                    example = "5",
                    schema = @Schema(minimum = "1", maximum = "100", defaultValue = "3")
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Топ подписок",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubscriptionResponseShortestDto.class)))
                    )
            }
    )
    @GetMapping("/top")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscriptionResponseShortestDto> getAllActiveTop3Subscriptions(@NotNull @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "3") Integer top) {

        return subscriptionService.getAllActiveTop3Subscriptions(top);
    }
}
