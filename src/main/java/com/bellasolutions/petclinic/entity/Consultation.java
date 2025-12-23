package com.bellasolutions.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "consultation_date", nullable = false)
    private LocalDateTime consultationDate;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", nullable = false)
    private String description;
    
    @NotNull(message = "Fee is required")
    @Positive(message = "Fee must be positive")
    @Column(name = "fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;
    
    @Size(max = 100, message = "Treatment must not exceed 100 characters")
    @Column(name = "treatment")
    private String treatment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @JsonIgnore
    private Pet pet;

    // Constructors
    public Consultation() {}

    public Consultation(LocalDateTime consultationDate, String description, BigDecimal fee, String treatment) {
        this.consultationDate = consultationDate;
        this.description = description;
        this.fee = fee;
        this.treatment = treatment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDateTime consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", consultationDate=" + consultationDate +
                ", description='" + description + '\'' +
                ", fee=" + fee +
                ", treatment='" + treatment + '\'' +
                '}';
    }
}
