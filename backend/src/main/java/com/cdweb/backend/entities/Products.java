package com.cdweb.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Products extends BaseEntity {
    private String productName;
    private int year;
    private Double price;
    private String url;
}
