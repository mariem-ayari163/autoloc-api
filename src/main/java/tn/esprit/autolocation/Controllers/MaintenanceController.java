package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Maintenance;
import tn.esprit.autolocation.Services.IMaintenanceService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Maintenance")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @PostMapping("/ajouterMaintenance")
    public Maintenance ajouterMaintenance(@RequestBody Maintenance maintenance) {
        return maintenanceService.ajouterMaintenance(maintenance);
    }

    @PutMapping("/modifierMaintenance")
    public Maintenance modifierMaintenance(@RequestBody Maintenance maintenance) {
        return maintenanceService.modifierMaintenance(maintenance);
    }

    @DeleteMapping("/supprimerMaintenance/{id}")
    public void supprimerMaintenance(@PathVariable Long id) {
        maintenanceService.supprimerMaintenance(id);
    }

    @GetMapping("/getMaintenance/{id}")
    public Maintenance afficherMaintenanceByID(@PathVariable Long id) {
        return maintenanceService.afficherMaintenanceByID(id);
    }

    @GetMapping("/allMaintenances")
    public List<Maintenance> afficherMaintenances() {
        return maintenanceService.afficherMaintenances();
    }
}
