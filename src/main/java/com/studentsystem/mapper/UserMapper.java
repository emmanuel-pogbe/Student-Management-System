package com.studentsystem.mapper;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToModel(UserCreate userCreate);
}
