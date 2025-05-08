package com.subsributions.app.controller.subscribution;

import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;
import com.subsributions.app.service.SubscriptionService;
import com.subsributions.app.util.Create;
import com.subsributions.app.util.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SubscriptionController", description = "Контроллер для операций над подпиской")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/subscribe")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto createUsersSubscription(@NotNull @Validated(Create.class) @RequestBody CreateUsersSubscriptionRequestDto newSubscription) {

        return subscriptionService.createUsersSubscription(newSubscription);
    }

    @PutMapping("/extend")
    @ResponseStatus(HttpStatus.OK)
    public UserExtendSubscriptionResponseDto extendSubscription(@NotNull @Validated(Update.class) @RequestBody UserExtendSubscriptionRequestDto extendSubscribe){

        return subscriptionService.extendSubscription(extendSubscribe);
    }

    @PutMapping("/decline")
    @ResponseStatus(HttpStatus.OK)
    public void declineSubscription(@NotNull @Validated(Update.class) @RequestBody UserDeclinedSubscriptionRequestDto decline) {

        subscriptionService.declineSubscription(decline);
    }
}
