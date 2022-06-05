package com.cdweb.backend.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductGalleries extends BaseEntity {
    private String imageLink;
    @ManyToOne
    @JoinColumn(name = "products_id")
    private Products products;
}
