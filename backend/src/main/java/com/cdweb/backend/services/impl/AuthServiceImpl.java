package com.cdweb.backend.services.impl;

import com.cdweb.backend.common.JwtService;
import com.cdweb.backend.entities.Roles;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.requests.AuthRequest;
import com.cdweb.backend.payloads.requests.RegistrationRequest;
import com.cdweb.backend.payloads.responses.AuthResponse;
import com.cdweb.backend.repositories.RoleRepository;
import com.cdweb.backend.repositories.UserRepository;
import com.cdweb.backend.services.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {
    private final UserRepository usersRepository;
    private final RoleRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest request) {
        Users entity = usersRepository.findByUsernameAndIsActiveTrue(request.getUsername());
        if (entity == null || !passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
            throw new IllegalArgumentException("The Username or Password is incorrect!");
        }
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(entity))
                .build();
    }

    @Override
    public AuthResponse register(RegistrationRequest request) {
        if (usersRepository.findByUsernameAndIsActiveTrue(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }
        Roles role;
        if (request.getRoleCode() == ""){
            role = rolesRepository.findByRoleCode("ROLE_USER");
        } else {
            role = rolesRepository.findByRoleCode(request.getRoleCode());
        }

        Users entity = usersRepository.save(Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .gmail(request.getGmail())
                .roles(role)
                .isActive(true)
                .build());
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(entity))
                .build();
    }


}
