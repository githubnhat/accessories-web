package com.cdweb.backend.payloads.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String gmail;
    private String thumbnail;
    private String role;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;

}
