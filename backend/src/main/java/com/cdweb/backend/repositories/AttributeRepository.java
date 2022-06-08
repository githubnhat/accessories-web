package com.cdweb.backend.repositories;

import com.cdweb.backend.entities.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes, Long> {

    Optional<Attributes> findById(Long id);

    Attributes findByIdAndIsActiveTrue(Long id);

    List<Attributes> findByIsActiveTrue();

    Attributes findByAttributeNameAndIsActiveTrue(String attributeName);
}
