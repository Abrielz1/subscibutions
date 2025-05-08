package com.subsributions.app.service;

import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;

public interface SubscriptionService {
    SubscriptionResponseDto createUsersSubscription(CreateUsersSubscriptionRequestDto newSubscription);

    UserExtendSubscriptionResponseDto extendSubscription(UserExtendSubscriptionRequestDto extendSubscribe);

    void declineSubscription(UserDeclinedSubscriptionRequestDto decline);
}
