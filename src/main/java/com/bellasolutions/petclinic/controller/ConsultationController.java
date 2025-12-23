package com.bellasolutions.petclinic.controller;

import com.bellasolutions.petclinic.entity.Consultation;
import com.bellasolutions.petclinic.service.ConsultationService;
import com.bellasolutions.petclinic.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<Consultation>> getAllConsultations() {
        List<Consultation> consultations = consultationService.findAll();
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        Optional<Consultation> consultation = consultationService.findById(id);
        return consultation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@Valid @RequestBody Consultation consultation) {
        try {
            // Verify that the pet exists
            if (consultation.getPet() != null && consultation.getPet().getId() != null) {
                if (!petService.existsById(consultation.getPet().getId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            
            Consultation savedConsultation = consultationService.save(consultation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConsultation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id, @Valid @RequestBody Consultation consultationDetails) {
        try {
            Consultation updatedConsultation = consultationService.updateConsultation(id, consultationDetails);
            return ResponseEntity.ok(updatedConsultation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        if (consultationService.existsById(id)) {
            consultationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Consultation>> getConsultationsByPetId(@PathVariable Long petId) {
        if (!petService.existsById(petId)) {
            return ResponseEntity.notFound().build();
        }
        List<Consultation> consultations = consultationService.findByPetId(petId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Consultation>> getConsultationsByOwnerId(@PathVariable Long ownerId) {
        List<Consultation> consultations = consultationService.findByOwnerId(ownerId);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Consultation>> getConsultationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Consultation> consultations = consultationService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/fees/total")
    public ResponseEntity<BigDecimal> getTotalFeesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        BigDecimal totalFees = consultationService.getTotalFeesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(totalFees);
    }
}
