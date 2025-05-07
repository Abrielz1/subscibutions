package com.subsributions.app.model.request.subscribution;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserUdpateSubscriptionRequest(/**
                                             * Электронная почта подписчика
                                             */
                                            @Email
                                            @NotBlank
                                            String email,


                                            String subscriptionsName,

                                            /**
                                             * Дата начала подписки
                                             */
                                            @FutureOrPresent
                                            LocalDate startOfSubscription,

                                            /**
                                             * Дата окончания подписки
                                             */
                                            @FutureOrPresent
                                            LocalDate endOfSubscription,

                                            /**
                                             *  Не продлевать подписку
                                             */
                                            Boolean isDeclined,

                                            /**
                                             *  Отказаться от подписки
                                             */
                                            Boolean isExpired) {
}
