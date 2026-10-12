package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Contrat;
import tn.esprit.autolocation.Services.IContratService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Contrat")
public class ContratController {

    private final IContratService contratService;

    @PostMapping("/ajouterContrat")
    public Contrat ajouterContrat(@RequestBody Contrat contrat) {
        return contratService.ajouterContrat(contrat);
    }

    @PutMapping("/modifierContrat")
    public Contrat modifierContrat(@RequestBody Contrat contrat) {
        return contratService.modifierContrat(contrat);
    }

    @DeleteMapping("/supprimerContrat/{id}")
    public void supprimerContrat(@PathVariable Long id) {
        contratService.supprimerContrat(id);
    }

    @GetMapping("/getContrat/{id}")
    public Contrat afficherContratByID(@PathVariable Long id) {
        return contratService.AfficherContratByID(id);
    }

    @GetMapping("/allContrats")
    public List<Contrat> afficherContrats() {
        return contratService.afficherContrats();
    }
}
