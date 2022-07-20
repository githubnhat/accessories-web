package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Address;
import com.cdweb.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserOrderByCreatedDateDesc(Users user);
    Address findByUserAndIsMainAddressTrue(Users user);
}
