package com.bellasolutions.petclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import com.bellasolutions.petclinic.entity.Owner;
import com.bellasolutions.petclinic.entity.Pet;
import com.bellasolutions.petclinic.entity.Consultation;
import com.bellasolutions.petclinic.service.OwnerService;
import com.bellasolutions.petclinic.service.PetService;
import com.bellasolutions.petclinic.service.ConsultationService;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Web Controller for Pet Clinic Frontend
 * Provides HTML web interface for managing owners, pets, and consultations
 */
@Controller
public class WebController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PetService petService;

    @Autowired
    private ConsultationService consultationService;

    /**
     * Home page - Dashboard with overview
     */
    @GetMapping("/")
    public String home(Model model) {
        // Load data safely without lazy loading issues
        List<Owner> recentOwners = ownerService.findAll();
        List<Pet> recentPets = petService.findAll();
        List<Consultation> recentConsultations = consultationService.findAll();
        
        model.addAttribute("ownersCount", recentOwners.size());
        model.addAttribute("petsCount", recentPets.size());
        model.addAttribute("consultationsCount", recentConsultations.size());
        model.addAttribute("recentOwners", recentOwners.size() > 5 ? recentOwners.subList(0, 5) : recentOwners);
        model.addAttribute("recentPets", recentPets.size() > 5 ? recentPets.subList(0, 5) : recentPets);
        
        return "index";
    }

    /**
     * Owners Management
     */
    @GetMapping("/owners")
    public String listOwners(Model model) {
        model.addAttribute("owners", ownerService.findAll());
        return "owners/list";
    }

    @GetMapping("/owners/new")
    public String newOwnerForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/form";
    }

    @PostMapping("/owners")
    public String saveOwner(@ModelAttribute Owner owner, RedirectAttributes redirectAttributes) {
        // Create a new Owner object to ensure no ID is set
        Owner newOwner = new Owner();
        newOwner.setFirstName(owner.getFirstName());
        newOwner.setLastName(owner.getLastName());
        newOwner.setAddress(owner.getAddress());
        newOwner.setCity(owner.getCity());
        newOwner.setPhone(owner.getPhone());
        
        Owner savedOwner = ownerService.save(newOwner);
        redirectAttributes.addFlashAttribute("message", "Owner " + savedOwner.getFirstName() + " " + savedOwner.getLastName() + " added successfully!");
        return "redirect:/owners";
    }

    @GetMapping("/owners/{id}")
    public String viewOwner(@PathVariable Long id, Model model) {
        Optional<Owner> ownerOpt = ownerService.findById(id);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            List<Pet> pets = petService.findByOwnerId(id);
            model.addAttribute("owner", owner);
            model.addAttribute("pets", pets);
            return "owners/view";
        }
        return "redirect:/owners";
    }

    @GetMapping("/owners/{id}/edit")
    public String editOwnerForm(@PathVariable Long id, Model model) {
        Optional<Owner> ownerOpt = ownerService.findById(id);
        if (ownerOpt.isPresent()) {
            model.addAttribute("owner", ownerOpt.get());
            return "owners/form";
        }
        return "redirect:/owners";
    }

    @PostMapping("/owners/{id}")
    public String updateOwner(@PathVariable Long id, @ModelAttribute Owner owner, RedirectAttributes redirectAttributes) {
        owner.setId(id);
        Owner updatedOwner = ownerService.save(owner);
        redirectAttributes.addFlashAttribute("message", "Owner " + updatedOwner.getFirstName() + " " + updatedOwner.getLastName() + " updated successfully!");
        return "redirect:/owners";
    }

    @PostMapping("/owners/{id}/delete")
    public String deleteOwner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Owner> ownerOpt = ownerService.findById(id);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            ownerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Owner " + owner.getFirstName() + " " + owner.getLastName() + " deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Owner not found!");
        }
        return "redirect:/owners";
    }

    /**
     * Pets Management
     */
    @GetMapping("/pets")
    @Transactional(readOnly = true)
    public String listPets(Model model) {
        model.addAttribute("pets", petService.findAll());
        return "pets/list";
    }

    @GetMapping("/pets/new")
    public String newPetForm(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("owners", ownerService.findAll());
        return "pets/form";
    }

    @PostMapping("/pets")
    public String savePet(@ModelAttribute Pet pet, RedirectAttributes redirectAttributes) {
        // Create a new Pet object to ensure no ID is set
        Pet newPet = new Pet();
        newPet.setName(pet.getName());
        newPet.setType(pet.getType());
        newPet.setBreed(pet.getBreed());
        newPet.setBirthDate(pet.getBirthDate());
        
        // Fetch the owner object from the database using the owner ID
        if (pet.getOwner() != null && pet.getOwner().getId() != null) {
            Owner owner = ownerService.findById(pet.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            newPet.setOwner(owner);
        }
        
        Pet savedPet = petService.save(newPet);
        redirectAttributes.addFlashAttribute("message", "Pet " + savedPet.getName() + " added successfully!");
        return "redirect:/pets";
    }

    @GetMapping("/pets/{id}")
    public String viewPet(@PathVariable Long id, Model model) {
        Optional<Pet> petOpt = petService.findById(id);
        if (petOpt.isPresent()) {
            Pet pet = petOpt.get();
            List<Consultation> consultations = consultationService.findByPetId(id);
            model.addAttribute("pet", pet);
            model.addAttribute("consultations", consultations);
            return "pets/view";
        }
        return "redirect:/pets";
    }

    @GetMapping("/pets/{id}/edit")
    public String editPetForm(@PathVariable Long id, Model model) {
        Optional<Pet> petOpt = petService.findById(id);
        if (petOpt.isPresent()) {
            model.addAttribute("pet", petOpt.get());
            model.addAttribute("owners", ownerService.findAll());
            return "pets/form";
        }
        return "redirect:/pets";
    }

    @PostMapping("/pets/{id}")
    public String updatePet(@PathVariable Long id, @ModelAttribute Pet pet, RedirectAttributes redirectAttributes) {
        pet.setId(id);
        
        // Fetch the owner object from the database using the owner ID
        if (pet.getOwner() != null && pet.getOwner().getId() != null) {
            Owner owner = ownerService.findById(pet.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            pet.setOwner(owner);
        }
        
        Pet updatedPet = petService.save(pet);
        redirectAttributes.addFlashAttribute("message", "Pet " + updatedPet.getName() + " updated successfully!");
        return "redirect:/pets";
    }

    @PostMapping("/pets/{id}/delete")
    public String deletePet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Pet> petOpt = petService.findById(id);
        if (petOpt.isPresent()) {
            Pet pet = petOpt.get();
            petService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Pet " + pet.getName() + " deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Pet not found!");
        }
        return "redirect:/pets";
    }

    /**
     * Consultations Management
     */
    @GetMapping("/consultations")
    @Transactional(readOnly = true)
    public String listConsultations(Model model) {
        model.addAttribute("consultations", consultationService.findAll());
        model.addAttribute("allPets", petService.findAll());
        return "consultations/list";
    }

    @GetMapping("/consultations/new")
    public String newConsultationForm(Model model) {
        model.addAttribute("consultation", new Consultation());
        model.addAttribute("pets", petService.findAll());
        return "consultations/form";
    }

    @PostMapping("/consultations")
    public String saveConsultation(@ModelAttribute Consultation consultation, RedirectAttributes redirectAttributes) {
        // Create a new Consultation object to ensure no ID is set
        Consultation newConsultation = new Consultation();
        newConsultation.setConsultationDate(consultation.getConsultationDate());
        newConsultation.setDescription(consultation.getDescription());
        newConsultation.setFee(consultation.getFee());
        newConsultation.setTreatment(consultation.getTreatment());
        
        // Fetch the pet object from the database using the pet ID
        if (consultation.getPet() != null && consultation.getPet().getId() != null) {
            Pet pet = petService.findById(consultation.getPet().getId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));
            newConsultation.setPet(pet);
        }
        
        Consultation savedConsultation = consultationService.save(newConsultation);
        redirectAttributes.addFlashAttribute("message", "Consultation for " + savedConsultation.getPet().getName() + " added successfully!");
        return "redirect:/consultations";
    }

    @GetMapping("/consultations/{id}")
    public String viewConsultation(@PathVariable Long id, Model model) {
        Optional<Consultation> consultationOpt = consultationService.findById(id);
        if (consultationOpt.isPresent()) {
            model.addAttribute("consultation", consultationOpt.get());
            return "consultations/view";
        }
        return "redirect:/consultations";
    }

    @PostMapping("/consultations/{id}/delete")
    public String deleteConsultation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Consultation> consultationOpt = consultationService.findById(id);
        if (consultationOpt.isPresent()) {
            Consultation consultation = consultationOpt.get();
            consultationService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Consultation for " + consultation.getPet().getName() + " deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Consultation not found!");
        }
        return "redirect:/consultations";
    }
}
