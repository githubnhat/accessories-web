package com.cdweb.backend.converters;

import com.cdweb.backend.entities.Users;
import com.cdweb.backend.payloads.responses.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserResponse toResponse(Users entity){
        return UserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .thumbnail(entity.getThumbnail())
                .gmail(entity.getGmail())
                .fullName(entity.getFullName())
                .role(entity.getRoles().getRoleCode())
                .build();
    }
}
