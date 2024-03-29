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
public class Thumbnails extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String imageLink;
    private boolean isActive;
    @ManyToOne
    @JoinColumn(name = "products_id")
    private Products product;
}
