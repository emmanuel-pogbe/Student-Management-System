package com.studentsystem.mapper;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.models.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    public User dtoToModel(UserCreate userCreate);
}
