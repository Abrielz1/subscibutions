package com.subsributions.app.model.request.subscribution;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserSubscriptionRequest(
                                      /**
                                      * электронная почта подписчика
                                      */
                                      @Email
                                      @NotBlank
                                      String email,

                                      /**
                                       * название подписки
                                       */
                                      @NotBlank
                                      String subscriptionsName,
                                      /**
                                      * дата начала подписки
                                      */
                                      @FutureOrPresent
                                      LocalDate startOfSubscription,
                                      /**
                                      * дата окончания подписки
                                      */
                                      @FutureOrPresent
                                      LocalDate endOfSubscription) {
}
