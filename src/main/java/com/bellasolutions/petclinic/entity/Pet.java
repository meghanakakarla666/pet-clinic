package com.bellasolutions.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Pet name is required")
    @Size(max = 30, message = "Pet name must not exceed 30 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @NotBlank(message = "Pet type is required")
    @Size(max = 20, message = "Pet type must not exceed 20 characters")
    @Column(name = "type", nullable = false)
    private String type;
    
    @Size(max = 50, message = "Breed must not exceed 50 characters")
    @Column(name = "breed")
    private String breed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private Owner owner;
    
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Consultation> consultations = new ArrayList<>();

    // Constructors
    public Pet() {}

    public Pet(String name, LocalDate birthDate, String type, String breed) {
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.breed = breed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public void addConsultation(Consultation consultation) {
        consultations.add(consultation);
        consultation.setPet(this);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                '}';
    }
}
