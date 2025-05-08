package com.subsributions.app.service.impl;

import com.subsributions.app.entity.Subscription;
import com.subsributions.app.entity.User;
import com.subsributions.app.entity.UserSubscription;
import com.subsributions.app.model.request.subscribution.UserDeclinedSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.UserExtendSubscriptionRequestDto;
import com.subsributions.app.model.request.subscribution.CreateUsersSubscriptionRequestDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortDto;
import com.subsributions.app.model.response.subscribution.SubscriptionResponseShortestDto;
import com.subsributions.app.model.response.subscribution.UserExtendSubscriptionResponseDto;
import com.subsributions.app.model.response.user.SubscriptionResponseDto;
import com.subsributions.app.repository.SubscriptionRepository;
import com.subsributions.app.repository.UserCRUDRepository;
import com.subsributions.app.repository.UserSubscriptionRepository;
import com.subsributions.app.service.SubscriptionService;
import com.subsributions.app.util.exception.exceptions.AlreadyExistsException;
import com.subsributions.app.util.exception.exceptions.UserNotFoundException;
import com.subsributions.app.util.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserCRUDRepository userCRUDRepository;

    /**
     * Создает новую подписку для пользователя.
     *
     * @param newSubscription DTO с данными для создания подписки
     * @return DTO созданной подписки
     * @throws AlreadyExistsException если подписка с таким именем уже существует
     * @throws UserNotFoundException если пользователь не найден
     */
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

        subscriptionRepository.save(newSb);

        return new SubscriptionResponseDto (
                                            newSubscription.name(),
                                            newSubscription.startDate(),
                                            newSubscription.endDate());
    }


    /**
     * Продлевает срок действия подписки.
     *
     * @param extendSubscribe DTO с данными для продления
     * @return DTO с обновленной датой окончания
     * @throws NotFoundException если подписка не найдена
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserExtendSubscriptionResponseDto extendSubscription(UserExtendSubscriptionRequestDto extendSubscribe) {

        if (!subscriptionRepository.existsByName(extendSubscribe.name())) {
            log.error("User with email: {} tries to create subscription with name: {} what is not exists!",
                    extendSubscribe.email(), extendSubscribe.name());
            throw new AlreadyExistsException(("User with email: %s tries" +
                    " to create subscription with name: %s  what is not exists!").formatted(extendSubscribe.email(),
                    extendSubscribe.name()));
        }

        User userFromDb = this.getUserByEmail(extendSubscribe.email());
        Subscription subscription = this.getSubscriptionByName(extendSubscribe.name()) ;

        subscription.setEndDate(extendSubscribe.endDate());
        subscriptionRepository.saveAndFlush(subscription);

        return new UserExtendSubscriptionResponseDto(userFromDb.getEmail(), subscription.getEndDate());
    }

    /**
     * Отменяет подписку пользователя.
     *
     * @param decline DTO с данными для отмены
     * @throws NotFoundException если подписка или связь не найдены
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void declineSubscription(UserDeclinedSubscriptionRequestDto decline) {

        User userFromDb = this.getUserByEmail(decline.email());
        Subscription subscription = this.getSubscriptionByName(decline.name());
        UserSubscription userSubscription = this.getUserSubscription(userFromDb.getId(), subscription.getId());
        userSubscription.setDeclined(true);

        userSubscriptionRepository.saveAndFlush(userSubscription);
    }

    /**
     * Возвращает список активных подписок с пагинацией.
     *
     * @param page параметры пагинации
     * @return список DTO подписок
     */
    @Override
    public List<SubscriptionResponseShortDto> getAllActiveSubscriptions(PageRequest page) {
        return userSubscriptionRepository.findAll(page).stream()
                .map(SubscriptionMapper::toSubscriptionResponseShortDto)
                .toList();
    }

    /**
     * Возвращает подписку по идентификатору.
     *
     * @param id идентификатор подписки
     * @return DTO подписки
     * @throws NotFoundException если подписка не найдена
     */
    @Override
    public SubscriptionResponseShortDto getSubscriptionById(Long id) {
        return SubscriptionMapper.toSubscriptionResponseShortDto(this.getUserSubscriptionById(id));
    }

    /**
     * Возвращает топ-N популярных подписок.
     *
     * @param top количество подписок в топе, а количество подписок в топе (1-100)
     * @return список DTO с статистикой
     */
    @Override
    public List<SubscriptionResponseShortestDto> getAllActiveTop3Subscriptions(Integer top) {

        return userSubscriptionRepository.getAllActiveTop3Subscriptions(top)
                                                            .stream()
                                                            .map(projection -> new SubscriptionResponseShortestDto(
                                                                    projection.getName(),
                                                                    projection.getCount()
                                                            ))
                                                            .toList();
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

    private UserSubscription getUserSubscriptionById(Long id) {

        return  userSubscriptionRepository.findById(id).orElseThrow(() -> {
            log.error("No subscription with such id: {}", id);
            return new UserNotFoundException("No subscription with such Id: %d".formatted(id));
        });
    }
}
