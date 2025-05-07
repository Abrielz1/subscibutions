package com.subsributions.app.service;

import com.subsributions.app.model.request.user.CreateAccountRequest;
import com.subsributions.app.model.request.user.UpdateUserAccountRequestDto;
import com.subsributions.app.model.response.user.UserResponseDto;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface UserCRUDService {
    List<UserResponseDto> getUsersList(PageRequest of);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto register(CreateAccountRequest newUser);
    UserResponseDto updateUserAccount(String email, UpdateUserAccountRequestDto updateAccountDto);
    void deleteUserAccount(String email);
}
