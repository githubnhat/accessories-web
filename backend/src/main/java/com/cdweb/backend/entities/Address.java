package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Address extends BaseEntity {
    private String address;
    private Boolean isMainAddress;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
