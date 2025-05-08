package com.subsributions.app.util.mapper;


import com.subsributions.app.entity.UserSubscription;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortDto;
import com.subsributions.app.util.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriptionMapper {

    private SubscriptionMapper() {
        log.info("Was attempt object creation for utility class {}", SubscriptionMapper.class.getName());
        throw new BadRequestException("Utility Class!");
    }

    public static SubscriptionResponseShortDto toSubscriptionResponseShortDto(UserSubscription subscription) {

       return new SubscriptionResponseShortDto(subscription.getUser().getEmail(),
                                               subscription.getSubscription().getName(),
                                               subscription.getSubscription().getStartDate(),
                                               subscription.getSubscription().getEndDate());
    }
}
