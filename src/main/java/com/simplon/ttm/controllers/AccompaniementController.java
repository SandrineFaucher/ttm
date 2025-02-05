package com.simplon.ttm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simplon.ttm.dto.AccompaniementDto;
import com.simplon.ttm.models.Accompaniement;
import com.simplon.ttm.services.AccompaniementService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AccompaniementController {
    private final AccompaniementService accompaniementService;

    public AccompaniementController(AccompaniementService accompaniementService){
        this.accompaniementService = accompaniementService;
    }

    @PostMapping("/accompaniement")
    public ResponseEntity<String> createAccompaniement(@Valid @RequestBody AccompaniementDto accompaniementDto){
        accompaniementService.saveAccompaniement(accompaniementDto);
        return ResponseEntity.ok("Accompaniement created successfully");
    }
    @GetMapping("/accompaniement/{id}")
    public ResponseEntity<Accompaniement> getAccompaniementById(@PathVariable Long id){
        Optional<Accompaniement> accompaniement = accompaniementService.getById(id);
        if(accompaniement.isPresent()){
            return ResponseEntity.ok(accompaniement.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/accompaniementUpdate/{id}")
    public ResponseEntity<Accompaniement> updateAccompaniementById(@PathVariable Long id, @RequestBody AccompaniementDto accompaniementDto){
        Optional<Accompaniement> accompaniementOptional = accompaniementService.getById(id);
        if(accompaniementOptional.isPresent()){
            Accompaniement updateAccompaniement = accompaniementService.updateAccompaniement(accompaniementOptional.get(), accompaniementDto);
            return ResponseEntity.ok(updateAccompaniement);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/accompaniements")
    public ResponseEntity<List<Accompaniement>> getAllAccompaniements(){
        List<Accompaniement> accompaniements = accompaniementService.findAll();
        return ResponseEntity.ok(accompaniements);
    }
    @DeleteMapping("/accompaniementDelete/{id}")
    public ResponseEntity<String> deleteAccompaniement(@PathVariable Long id){
        try{
            accompaniementService.deleteAccompaniement(id);
            return ResponseEntity.ok("Accompaniement is successfully deleted");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
