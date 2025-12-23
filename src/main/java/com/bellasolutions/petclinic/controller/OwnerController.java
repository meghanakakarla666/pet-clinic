package com.bellasolutions.petclinic.controller;

import com.bellasolutions.petclinic.entity.Owner;
import com.bellasolutions.petclinic.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        Optional<Owner> owner = ownerService.findById(id);
        return owner.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Owner> createOwner(@Valid @RequestBody Owner owner) {
        try {
            Owner savedOwner = ownerService.save(owner);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOwner);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @Valid @RequestBody Owner ownerDetails) {
        try {
            Owner updatedOwner = ownerService.updateOwner(id, ownerDetails);
            return ResponseEntity.ok(updatedOwner);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        if (ownerService.existsById(id)) {
            ownerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Owner>> searchOwners(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String city) {
        
        List<Owner> owners;
        
        if (name != null && !name.isEmpty()) {
            owners = ownerService.findByName(name);
        } else if (firstName != null && !firstName.isEmpty()) {
            owners = ownerService.findByFirstName(firstName);
        } else if (lastName != null && !lastName.isEmpty()) {
            owners = ownerService.findByLastName(lastName);
        } else if (city != null && !city.isEmpty()) {
            owners = ownerService.findByCity(city);
        } else {
            owners = ownerService.findAll();
        }
        
        return ResponseEntity.ok(owners);
    }
}
