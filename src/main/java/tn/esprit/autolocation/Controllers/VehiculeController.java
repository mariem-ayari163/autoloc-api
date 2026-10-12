package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Vehicule;
import tn.esprit.autolocation.Services.IVehiculeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Vehicule")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @PostMapping("/ajouterVehicule")
    public Vehicule ajouterVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.ajouterVehicule(vehicule);
    }

    @PutMapping("/modifierVehicule")
    public Vehicule modifierVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.modifierVehicule(vehicule);
    }

    @DeleteMapping("/supprimerVehicule/{id}")
    public void supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerVehicule(id);
    }

    @GetMapping("/getVehicule/{id}")
    public Vehicule afficherVehiculeByID(@PathVariable Long id) {
        return vehiculeService.AfficherVehiculeByID(id);
    }

    @GetMapping("/allVehicules")
    public List<Vehicule> afficherVehicules() {
        return vehiculeService.afficherVehicules();
    }
}
