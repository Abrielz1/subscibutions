package com.subsributions.app.service;

import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDto createUsersSubscription(CreateUsersSubscriptionRequestDto newSubscription);

    UserExtendSubscriptionResponseDto extendSubscription(UserExtendSubscriptionRequestDto extendSubscribe);

    void declineSubscription(UserDeclinedSubscriptionRequestDto decline);

    List<SubscriptionResponseShortDto> getAllActiveSubscriptions(PageRequest page);

    SubscriptionResponseShortDto getSubscriptionById(Long id);

    List<SubscriptionResponseShortestDto> getAllActiveTop3Subscriptions(Integer top);
}
