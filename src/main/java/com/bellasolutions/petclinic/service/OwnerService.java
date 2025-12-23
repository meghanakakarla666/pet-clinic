package com.bellasolutions.petclinic.service;

import com.bellasolutions.petclinic.entity.Owner;
import com.bellasolutions.petclinic.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    public List<Owner> findAll() {
        List<Owner> owners = ownerRepository.findAll();
        // Force initialization of pets
        owners.forEach(owner -> owner.getPets().size());
        return owners;
    }

    public Optional<Owner> findById(Long id) {
        return ownerRepository.findById(id);
    }

    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }

    public List<Owner> findByName(String name) {
        return ownerRepository.findByNameContaining(name);
    }

    public List<Owner> findByFirstName(String firstName) {
        return ownerRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    public List<Owner> findByLastName(String lastName) {
        return ownerRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<Owner> findByCity(String city) {
        return ownerRepository.findByCityIgnoreCase(city);
    }

    public boolean existsById(Long id) {
        return ownerRepository.existsById(id);
    }

    public Owner updateOwner(Long id, Owner ownerDetails) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
        
        owner.setFirstName(ownerDetails.getFirstName());
        owner.setLastName(ownerDetails.getLastName());
        owner.setAddress(ownerDetails.getAddress());
        owner.setCity(ownerDetails.getCity());
        owner.setPhone(ownerDetails.getPhone());
        
        return ownerRepository.save(owner);
    }
}
