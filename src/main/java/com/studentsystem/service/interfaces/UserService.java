package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.request.UserLoginRequest;
import com.studentsystem.dto.response.SuccessLogin;
import com.studentsystem.dto.response.SuccessUserCreated;

public interface UserService {    
    SuccessUserCreated createUser(UserCreate userCreateRequest);

    SuccessLogin getAccessToken(UserLoginRequest userLoginRequest);
}
