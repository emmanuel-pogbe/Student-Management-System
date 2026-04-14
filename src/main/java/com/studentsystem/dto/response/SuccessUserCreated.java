package com.studentsystem.dto.response;

import com.studentsystem.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessUserCreated {
    private String email;
    private String fullName;
    private RoleEnum role;
}
