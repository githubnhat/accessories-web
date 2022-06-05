package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Branches;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branches, Long> {
    Branches findByName(String name);
}
