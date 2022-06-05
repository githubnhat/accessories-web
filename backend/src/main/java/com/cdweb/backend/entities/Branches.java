package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Branches extends BaseEntity {
    private String name;
    private String code;
    @OneToMany(mappedBy = "branches")
    private Set<Products> products = new HashSet<>();
}
