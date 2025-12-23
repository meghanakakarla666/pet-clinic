package com.bellasolutions.petclinic.service;

import com.bellasolutions.petclinic.entity.Pet;
import com.bellasolutions.petclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<Pet> findAll() {
        List<Pet> pets = petRepository.findAll();
        // Force initialization of lazy-loaded properties
        pets.forEach(pet -> {
            if (pet.getOwner() != null) {
                pet.getOwner().getFirstName(); // Force loading
            }
        });
        return pets;
    }

    public Optional<Pet> findById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        // Force initialization of lazy-loaded properties
        pet.ifPresent(p -> {
            if (p.getOwner() != null) {
                p.getOwner().getFirstName(); // Force loading
            }
            // Load consultations
            p.getConsultations().size();
        });
        return pet;
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    public List<Pet> findByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public List<Pet> findByName(String name) {
        return petRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Pet> findByType(String type) {
        return petRepository.findByTypeIgnoreCase(type);
    }

    public List<Pet> findByOwnerIdAndName(Long ownerId, String name) {
        return petRepository.findByOwnerIdAndNameContaining(ownerId, name);
    }

    public boolean existsById(Long id) {
        return petRepository.existsById(id);
    }

    public Pet updatePet(Long id, Pet petDetails) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
        
        pet.setName(petDetails.getName());
        pet.setBirthDate(petDetails.getBirthDate());
        pet.setType(petDetails.getType());
        pet.setBreed(petDetails.getBreed());
        
        return petRepository.save(pet);
    }
}
