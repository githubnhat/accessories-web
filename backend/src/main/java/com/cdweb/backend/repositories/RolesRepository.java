package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RolesRepository extends JpaRepository<Roles, Long> {
//    List<Roles> findByIsActiveTrue();
    Roles findByRoleCode(String roleName);
}
