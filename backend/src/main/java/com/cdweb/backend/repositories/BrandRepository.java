package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Brands;
import com.cdweb.backend.payloads.responses.BrandResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brands, Long> {
    Brands findByName(String branchName);

    Brands findByCode(String code);
}
