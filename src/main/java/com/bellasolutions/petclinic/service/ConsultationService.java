package com.bellasolutions.petclinic.service;

import com.bellasolutions.petclinic.entity.Consultation;
import com.bellasolutions.petclinic.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    public List<Consultation> findAll() {
        List<Consultation> consultations = consultationRepository.findAll();
        // Force initialization of lazy-loaded properties
        consultations.forEach(consultation -> {
            if (consultation.getPet() != null) {
                consultation.getPet().getName(); // Force loading
                if (consultation.getPet().getOwner() != null) {
                    consultation.getPet().getOwner().getFirstName(); // Force loading
                }
            }
        });
        return consultations;
    }

    public Optional<Consultation> findById(Long id) {
        Optional<Consultation> consultation = consultationRepository.findById(id);
        // Force initialization of lazy-loaded properties
        consultation.ifPresent(c -> {
            if (c.getPet() != null) {
                c.getPet().getName(); // Force loading
                if (c.getPet().getOwner() != null) {
                    c.getPet().getOwner().getFirstName(); // Force loading
                }
            }
        });
        return consultation;
    }

    public Consultation save(Consultation consultation) {
        if (consultation.getConsultationDate() == null) {
            consultation.setConsultationDate(LocalDateTime.now());
        }
        return consultationRepository.save(consultation);
    }

    public void deleteById(Long id) {
        consultationRepository.deleteById(id);
    }

    public List<Consultation> findByPetId(Long petId) {
        return consultationRepository.findByPetIdOrderByConsultationDateDesc(petId);
    }

    public List<Consultation> findByOwnerId(Long ownerId) {
        return consultationRepository.findByOwnerIdOrderByConsultationDateDesc(ownerId);
    }

    public List<Consultation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return consultationRepository.findByConsultationDateBetween(startDate, endDate);
    }

    public BigDecimal getTotalFeesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = consultationRepository.getTotalFeesBetweenDates(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public boolean existsById(Long id) {
        return consultationRepository.existsById(id);
    }

    public Consultation updateConsultation(Long id, Consultation consultationDetails) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found with id: " + id));
        
        consultation.setDescription(consultationDetails.getDescription());
        consultation.setFee(consultationDetails.getFee());
        consultation.setTreatment(consultationDetails.getTreatment());
        
        return consultationRepository.save(consultation);
    }
}
