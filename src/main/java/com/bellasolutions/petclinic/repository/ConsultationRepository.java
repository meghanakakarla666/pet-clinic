package com.bellasolutions.petclinic.repository;

import com.bellasolutions.petclinic.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
    List<Consultation> findByPetId(Long petId);
    
    List<Consultation> findByPetIdOrderByConsultationDateDesc(Long petId);
    
    @Query("SELECT c FROM Consultation c WHERE c.pet.owner.id = :ownerId ORDER BY c.consultationDate DESC")
    List<Consultation> findByOwnerIdOrderByConsultationDateDesc(@Param("ownerId") Long ownerId);
    
    List<Consultation> findByConsultationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT SUM(c.fee) FROM Consultation c WHERE c.consultationDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalFeesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
