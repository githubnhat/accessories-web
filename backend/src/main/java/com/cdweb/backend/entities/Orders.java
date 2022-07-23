package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Orders extends BaseEntity {
    private String address;
    private String phone;
    private String totalBill;
    private String status;

    @OneToMany(mappedBy = "order")
    private Set<OrderItems> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
