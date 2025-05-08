package com.subsributions.app.util.mapper;

import com.subsributions.app.entity.User;
import com.subsributions.app.model.request.user.CreateAccountRequestDto;
import com.subsributions.app.model.response.user.UserResponseDto;
import com.subsributions.app.util.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
public class UserMapper {

    private UserMapper() {
        log.info("Was attempt object creation for utility class {}", UserMapper.class.getName());
        throw new BadRequestException("Utility Class!");
    }

    public static User toUser(CreateAccountRequestDto newUser) {

        new User();
        return User
                .builder()
                .email(newUser.email())
                .password(newUser.password())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDto toUserResponseDto(User userFromDb) {

        return new UserResponseDto(userFromDb.getEmail());
    }
}
