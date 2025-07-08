package com.yetgim.library_management_system.mapper;

import com.yetgim.library_management_system.dto.req.UserRegisterRequest;
import com.yetgim.library_management_system.dto.req.UserUpdateRequest;
import com.yetgim.library_management_system.dto.res.UserResponse;
import com.yetgim.library_management_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "borrows", ignore = true)
    User toEntity(UserRegisterRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "borrows", ignore = true)
    void updateEntity(@MappingTarget User user, UserUpdateRequest request);
}