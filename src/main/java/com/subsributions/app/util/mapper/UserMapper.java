package com.subsributions.app.util.mapper;

import com.subsributions.app.util.exception.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper {

    private UserMapper() {
//        log.info("Was attempt object creation for utility class %s"
//                .formatted(UserMapper.class.getName()));
        throw new BadRequestException("Utility Class!");
    }


}
