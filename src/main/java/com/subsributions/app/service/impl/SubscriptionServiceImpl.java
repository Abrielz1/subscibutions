package com.subsributions.app.service.impl;

import com.subsributions.app.entity.Subscription;
import com.subsributions.app.entity.User;
import com.subsributions.app.entity.UserSubscription;
import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;
import com.subsributions.app.repository.SubscriptionRepository;
import com.subsributions.app.repository.UserCRUDRepository;
import com.subsributions.app.repository.UserSubscriptionRepository;
import com.subsributions.app.service.SubscriptionService;
import com.subsributions.app.util.exception.exceptions.AlreadyExistsException;
import com.subsributions.app.util.exception.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserCRUDRepository userCRUDRepository;

    @Override
    public SubscriptionResponseDto createUsersSubscription(CreateUsersSubscriptionRequestDto newSubscription) {

        if (subscriptionRepository.existsByName(newSubscription.name())) {
            log.error("User with email: {} tries to create subscription with name: {} what already exists!",
                    newSubscription.email(), newSubscription.name());
            throw new AlreadyExistsException(("User with email: %s tries" +
                    " to create subscription with name: %s  what already exists!").formatted(newSubscription.email(),
                                                                                                newSubscription.name()));
        }

        User userFromDb = this.getUserByEmail(newSubscription.email());

      Subscription newSb = Subscription
                .builder()
                .name(newSubscription.name())
                .startDate(newSubscription.startDate())
                .endDate(newSubscription.endDate())
                .isExpired(false)
                .build();

       UserSubscription newUSb = UserSubscription
                .builder()
                .subscription(newSb)
                .user(userFromDb)
                .activationDate(LocalDate.now())
                .build();

        newSb.getUserSubscriptions().add(newUSb);

        Subscription savedSub = subscriptionRepository.save(newSb);
        newUSb.setSubscription(savedSub);

        return new SubscriptionResponseDto (
                                            newSubscription.name(),
                                            newSubscription.startDate(),
                                            newSubscription.endDate());
    }

    @Override
    public UserExtendSubscriptionResponseDto extendSubscription(UserExtendSubscriptionRequestDto extendSubscribe) {

        if (!subscriptionRepository.existsByName(extendSubscribe.name())) {
            log.error("User with email: {} tries to create subscription with name: {} what already exists!",
                    extendSubscribe.email(), extendSubscribe.name());
            throw new AlreadyExistsException(("User with email: %s tries" +
                    " to create subscription with name: %s  what already exists!").formatted(extendSubscribe.email(),
                    extendSubscribe.name()));
        }

        User userFromDb = this.getUserByEmail(extendSubscribe.email());

        Subscription subscription = this.getSubscriptionByName(extendSubscribe.name()) ;

        subscription.setEndDate(extendSubscribe.endDate());
        subscriptionRepository.saveAndFlush(subscription);

        return new UserExtendSubscriptionResponseDto(userFromDb.getEmail(), subscription.getEndDate());
    }

    @Override
    public void declineSubscription(UserDeclinedSubscriptionRequestDto decline) {

        User userFromDb = this.getUserByEmail(decline.email());
        Subscription subscription = this.getSubscriptionByName(decline.name());
        UserSubscription userSubscription = this.getUserSubscription(userFromDb.getId(), subscription.getId());
        userSubscription.setDeclined(true);

        userSubscriptionRepository.saveAndFlush(userSubscription);
    }

    private User getUserByEmail(String email) {

        return userCRUDRepository.findByEmail(email).orElseThrow(() -> {
            log.error("No user in db with such email: {}", email);
            return new UserNotFoundException("No user in db with such email: %s".formatted(email));
        });
    }

    private Subscription getSubscriptionByName(String name) {

        return  subscriptionRepository.findByName(name).orElseThrow(() -> {
           log.error("No subscription with such name: {}", name);
           return new UserNotFoundException("No subscription with such name: %s".formatted(name));
        });
    }

    private UserSubscription getUserSubscription(Long userId, Long subscriptionId) {

        return userSubscriptionRepository.findUserSubscriptionByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> {
                    log.error("{} {}", userId, subscriptionId);
                    return new UserNotFoundException("%d%d".formatted(userId, subscriptionId));
                });
    }
}
