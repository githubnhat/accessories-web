package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users extends BaseEntity{
    private String username;
    private String password;
    private String fullName;
    private String gmail;
    @Column(name = "is_active")
    private boolean isActive;
    @ManyToOne
    @JoinColumn(name = "roles_id", nullable = false)
    private Roles roles;
}
