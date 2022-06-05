package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Products extends BaseEntity {
    private String productName;
    private int year;
    private Double price;
    @Column(name = "is_active")
    private boolean isActive;
    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories categories;

    @ManyToOne
    @JoinColumn(name = "brands_id")
    private Brands brands;

    @OneToMany(mappedBy = "products")
    private Set<ProductGalleries> productGalleries = new HashSet<>();
}
