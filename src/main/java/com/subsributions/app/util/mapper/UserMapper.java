package com.subsributions.app.util.mapper;

import com.subsributions.app.entity.User;
import com.subsributions.app.model.request.user.CreateAccountRequest;
import com.subsributions.app.model.response.user.UserResponseDto;
import com.subsributions.app.util.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
public class UserMapper {

    private UserMapper() {
        log.info("Was attempt object creation for utility class %s"
                .formatted(UserMapper.class.getName()));
        throw new BadRequestException("Utility Class!");
    }

    public static User toUser(CreateAccountRequest newUser) {

        new User();
        return User
                .builder()
                .email(newUser.password())
                .password(newUser.email())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDto toUserResponseDto(User userFromDb) {

        return new UserResponseDto(userFromDb.getEmail());
    }
}
