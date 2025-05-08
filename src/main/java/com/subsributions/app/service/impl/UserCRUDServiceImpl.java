package com.subsributions.app.service.impl;

import com.subsributions.app.entity.User;
import com.subsributions.app.model.request.user.CreateAccountRequestDto;
import com.subsributions.app.model.request.user.UpdateUserAccountRequestDto;
import com.subsributions.app.model.response.user.UserResponseDto;
import com.subsributions.app.repository.UserCRUDRepository;
import com.subsributions.app.service.UserCRUDService;
import com.subsributions.app.util.exception.exceptions.AlreadyExistsException;
import com.subsributions.app.util.exception.exceptions.UserNotFoundException;
import com.subsributions.app.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCRUDServiceImpl implements UserCRUDService {

    private final UserCRUDRepository userCRUDRepository;

    /**
     * Возвращает список пользователей с пагинацией.
     *
     * @param page параметры пагинации
     * @return список DTO пользователей
     */
    @Override
    public List<UserResponseDto> getUsersList(PageRequest page) {
        log.info("Users list was sent!");
        return userCRUDRepository.findAll(page)
                .stream().map(UserMapper::toUserResponseDto)
                .toList();
    }

    /**
     * Возвращает пользователя по email.
     *
     * @param email электронная почта пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserResponseDto getUserByEmail(String email) {
        log.info("User with email: {} was sent!", email);
        return UserMapper.toUserResponseDto(this.getUserFromDbByEmail(email));
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param newUser DTO с данными для регистрации
     * @return DTO зарегистрированного пользователя
     * @throws AlreadyExistsException если пользователь уже существует
     */
    @Override
    public UserResponseDto register(CreateAccountRequestDto newUser) {

        if (this.checkAlreadyExistedUser(newUser.email())) {
            log.error("Email: {} already registered", newUser.email());
            throw new AlreadyExistsException("Email: %s already registered".formatted(newUser.email()));
        }

        log.info("User with email: {} was created!", newUser.email());
        return UserMapper.toUserResponseDto(
                userCRUDRepository.saveAndFlush(UserMapper.toUser(newUser)));
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param email электронная почта пользователя
     * @param updateAccountDto DTO с новым паролем
     * @return DTO обновленного пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserResponseDto updateUserAccount(String email, UpdateUserAccountRequestDto updateAccountDto) {

        return UserMapper.toUserResponseDto(
                userCRUDRepository.saveAndFlush(this.setupEntity(
                        this.getUserFromDbByEmail(email), updateAccountDto)));
    }

    /**
     * Удаляет учетную запись пользователя.
     *
     * @param email электронная почта пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public void deleteUserAccount(String email) {
        userCRUDRepository.findByEmail(email).ifPresent(userCRUDRepository::delete);
    }

    private User getUserFromDbByEmail(String email) {

        return userCRUDRepository.findByEmail(email).orElseThrow(() -> {
            log.error("No user with such email: {}", email);
            return new UserNotFoundException("No user with such email: %s".formatted(email));
        });
    }

    private User setupEntity(User userFromDb, UpdateUserAccountRequestDto updateUser) {

        Optional.ofNullable(updateUser.password()).ifPresent(userFromDb::setPassword);
        log.info("user was updated! With email: {}", userFromDb.getEmail());
        return userFromDb;
    }

    private boolean checkAlreadyExistedUser(String email) {

        return userCRUDRepository.existsByEmail(email);
    }
}
