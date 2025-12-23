package com.bellasolutions.petclinic.repository;

import com.bellasolutions.petclinic.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    List<Pet> findByOwnerId(Long ownerId);
    
    List<Pet> findByNameContainingIgnoreCase(String name);
    
    List<Pet> findByTypeIgnoreCase(String type);
    
    @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pet> findByOwnerIdAndNameContaining(@Param("ownerId") Long ownerId, @Param("name") String name);
}
