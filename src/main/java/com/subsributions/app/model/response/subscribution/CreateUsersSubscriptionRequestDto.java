package com.subsributions.app.model.response.subscribution;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateUsersSubscriptionRequestDto(@Schema(description = "электронная почта подписчика")
                                                @NotBlank
                                                @Email
                                                String email,

                                                @Schema(description = "название подписки")
                                                @NotBlank
                                                String name,

                                                @Schema(description = "дата начала подписки")
                                                @FutureOrPresent
                                                LocalDate startDate,

                                                @Schema(description = "дата окончания подписки")
                                                @FutureOrPresent
                                                LocalDate endDate) {
}
