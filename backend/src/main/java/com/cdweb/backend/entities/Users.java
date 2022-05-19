package com.cdweb.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
