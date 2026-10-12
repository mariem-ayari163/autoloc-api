package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Paiement;
import tn.esprit.autolocation.Services.IPaiementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Paiement")
public class PaiementController {

    private final IPaiementService paiementService;

    @PostMapping("/ajouterPaiement")
    public Paiement ajouterPaiement(@RequestBody Paiement paiement) {
        return paiementService.ajouterPaiement(paiement);
    }

    @PutMapping("/modifierPaiement")
    public Paiement modifierPaiement(@RequestBody Paiement paiement) {
        return paiementService.modifierPaiement(paiement);
    }

    @DeleteMapping("/supprimerPaiement/{id}")
    public void supprimerPaiement(@PathVariable Long id) {
        paiementService.supprimerPaiement(id);
    }

    @GetMapping("/getPaiement/{id}")
    public Paiement afficherPaiementByID(@PathVariable Long id) {
        return paiementService.AfficherPaiementByID(id);
    }

    @GetMapping("/allPaiements")
    public List<Paiement> afficherPaiements() {
        return paiementService.afficherAgences();
    }
}
