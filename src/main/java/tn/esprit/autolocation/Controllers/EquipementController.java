package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Equipement;
import tn.esprit.autolocation.Services.IEquipementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Equipement")
public class EquipementController {

    private final IEquipementService equipementService;

    @PostMapping("/ajouterEquipement")
    public Equipement ajouterEquipement(@RequestBody Equipement equipement) {
        return equipementService.ajouterEquipement(equipement);
    }

    @PutMapping("/modifierEquipement")
    public Equipement modifierEquipement(@RequestBody Equipement equipement) {
        return equipementService.modifierEquipement(equipement);
    }

    @DeleteMapping("/supprimerEquipement/{id}")
    public void supprimerEquipement(@PathVariable Long id) {
        equipementService.supprimerEquipement(id);
    }

    @GetMapping("/getEquipement/{id}")
    public Equipement afficherEquipementByID(@PathVariable Long id) {
        return equipementService.AfficherEquipementByID(id);
    }

    @GetMapping("/allEquipements")
    public List<Equipement> afficherEquipements() {
        return equipementService.afficherEquipements();
    }
}
