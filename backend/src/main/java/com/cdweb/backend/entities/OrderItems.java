package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItems extends BaseEntity {
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private String productCombination;
    private int quantity;
    private int discount;
    @Column(columnDefinition = "TEXT")
    private String imageLink;
    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories categories;

    @ManyToOne
    @JoinColumn(name = "brands_id")
    private Brands brands;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
}
