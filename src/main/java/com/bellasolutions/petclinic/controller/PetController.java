package com.bellasolutions.petclinic.controller;

import com.bellasolutions.petclinic.entity.Pet;
import com.bellasolutions.petclinic.service.PetService;
import com.bellasolutions.petclinic.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.findAll();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.findById(id);
        return pet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        try {
            // Verify that the owner exists
            if (pet.getOwner() != null && pet.getOwner().getId() != null) {
                if (!ownerService.existsById(pet.getOwner().getId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            
            Pet savedPet = petService.save(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @Valid @RequestBody Pet petDetails) {
        try {
            Pet updatedPet = petService.updatePet(id, petDetails);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        if (petService.existsById(id)) {
            petService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Pet>> getPetsByOwnerId(@PathVariable Long ownerId) {
        if (!ownerService.existsById(ownerId)) {
            return ResponseEntity.notFound().build();
        }
        List<Pet> pets = petService.findByOwnerId(ownerId);
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Pet>> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long ownerId) {
        
        List<Pet> pets;
        
        if (ownerId != null && name != null && !name.isEmpty()) {
            pets = petService.findByOwnerIdAndName(ownerId, name);
        } else if (ownerId != null) {
            pets = petService.findByOwnerId(ownerId);
        } else if (name != null && !name.isEmpty()) {
            pets = petService.findByName(name);
        } else if (type != null && !type.isEmpty()) {
            pets = petService.findByType(type);
        } else {
            pets = petService.findAll();
        }
        
        return ResponseEntity.ok(pets);
    }
}
