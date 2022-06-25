package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsernameAndIsActiveTrue(String username);

    Users findByIdAndIsActiveFalse(Long userId);

    List<Users> findByIsActiveTrue();

}
