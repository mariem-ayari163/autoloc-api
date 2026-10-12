package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autolocation.Entities.Employe;
import tn.esprit.autolocation.Services.IEmployeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Employe")
public class EmployeController {

    private final IEmployeService employeService;

    @PostMapping("/ajouterEmploye")
    public Employe ajouterEmploye(@RequestBody Employe employe) {
        return employeService.ajouterEmploye(employe);
    }

    @PutMapping("/modifierEmploye")
    public Employe modifierEmploye(@RequestBody Employe employe) {
        return employeService.modifierEmploye(employe);
    }

    @DeleteMapping("/supprimerEmploye/{id}")
    public void supprimerEmploye(@PathVariable Long id) {
        employeService.supprimerEmploye(id);
    }

    @GetMapping("/getEmploye/{id}")
    public Employe afficherEmployeByID(@PathVariable Long id) {
        return employeService.afficherEmployeByID(id);
    }

    @GetMapping("/allEmployes")
    public List<Employe> afficherEmployes() {
        return employeService.afficherEmployes();
    }
}
