package com.bellasolutions.petclinic.repository;

import com.bellasolutions.petclinic.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    
    List<Owner> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Owner> findByLastNameContainingIgnoreCase(String lastName);
    
    @Query("SELECT o FROM Owner o WHERE LOWER(o.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(o.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Owner> findByNameContaining(@Param("name") String name);
    
    List<Owner> findByCityIgnoreCase(String city);
}
