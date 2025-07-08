package com.yetgim.library_management_system.service.abstracts;

import com.yetgim.library_management_system.dto.req.UserLoginRequest;
import com.yetgim.library_management_system.dto.req.UserRegisterRequest;
import com.yetgim.library_management_system.dto.req.UserUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthResponse;
import com.yetgim.library_management_system.dto.res.UserResponse;
import com.yetgim.library_management_system.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    AuthResponse register(UserRegisterRequest request);

    AuthResponse login(UserLoginRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    Page<UserResponse> getAllUsers(Pageable pageable);

    List<UserResponse> getUsersByRole(Role role);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);

    void changePassword(Long id, String newPassword);
}