package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.model.Panier;
import com.skillflow.skillflowbackend.service.PanierIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/panier/")
@Validated
public class PanierController {
    @Autowired
    private PanierIService panierIService;
    @PostMapping("/addCourseToPanier")
    public Panier addCourseToPanier(@RequestParam Long courseId,@RequestBody Panier panier) {
        return panierIService.addCourseToPanier(courseId, panier) ;
    }
    @PutMapping("/removeCourseFromPanier")
    public Panier removeCourseFromPanier(@RequestParam Long courseId) {
        return panierIService.removeCourseFromPanier(courseId);
    }
    @DeleteMapping("/clearPanier")
    public void clearPanier() {
        panierIService.clearPanier();
    }
    @GetMapping("/getPanier")
    public Panier getPanier() {
        return panierIService.getPanier();
    }
    @GetMapping("/getPaniertotal")
    public Double getPaniertotal() {
        return panierIService.getPaniertotal();
    }

}
