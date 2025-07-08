package com.yetgim.library_management_system.service.concretes;

import com.yetgim.library_management_system.dto.req.UserLoginRequest;
import com.yetgim.library_management_system.dto.req.UserRegisterRequest;
import com.yetgim.library_management_system.dto.req.UserUpdateRequest;
import com.yetgim.library_management_system.dto.res.AuthResponse;
import com.yetgim.library_management_system.dto.res.UserResponse;
import com.yetgim.library_management_system.entity.User;
import com.yetgim.library_management_system.enums.Role;
import com.yetgim.library_management_system.exception.ResourceAlreadyExistsException;
import com.yetgim.library_management_system.exception.ResourceNotFoundException;
import com.yetgim.library_management_system.mapper.UserMapper;
import com.yetgim.library_management_system.repository.UserRepository;
import com.yetgim.library_management_system.security.JwtTokenProvider;
import com.yetgim.library_management_system.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username '" + request.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser.getUsername());

        UserResponse userResponse = userMapper.toResponse(savedUser);
        return new AuthResponse(token, userResponse);
    }

    @Override
    public AuthResponse login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication.getName());

        User user = findUserByUsername(request.getUsername());
        UserResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(token, userResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = findUserByUsername(username);
        return userMapper.toResponse(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findUserById(id);

        // Check if new username already exists
        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username '" + request.getUsername() + "' is already taken");
        }

        // Check if new email already exists
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email '" + request.getEmail() + "' is already registered");
        }

        // Update user
        userMapper.updateEntity(user, request);
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);

        // Check if user has active borrows
        boolean hasActiveBorrows = user.getBorrows().stream()
                .anyMatch(borrow -> borrow.getReturnDate() == null);

        if (hasActiveBorrows) {
            throw new IllegalStateException("Cannot delete user with active borrows");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = findUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // helpers
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
}
