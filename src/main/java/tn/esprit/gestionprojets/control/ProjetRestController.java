package tn.esprit.gestionprojets.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionprojets.entities.Projet;
import tn.esprit.gestionprojets.service.IProjetService;

import java.util.List;

@RestController
@RequestMapping("/projet")
public class ProjetRestController {

    @Autowired
    IProjetService projetService;

    // GET http://localhost:8089/gestionprojets/projet/retrieve-all-projets
    @GetMapping("/retrieve-all-projets")
    public List<Projet> retrieveAllProjets() {
        return projetService.retrieveAllProjets();
    }

    // GET http://localhost:8089/gestionprojets/projet/retrieve-projet/1
    @GetMapping("/retrieve-projet/{projetId}")
    public Projet retrieveProjet(@PathVariable Long projetId) {
        return projetService.retrieveProjet(projetId);
    }

    // POST http://localhost:8089/gestionprojets/projet/add-projet
    @PostMapping("/add-projet")
    public Projet addProjet(@RequestBody Projet projet) {
        return projetService.addProjet(projet);
    }

    // PUT http://localhost:8089/gestionprojets/projet/modify-projet
    @PutMapping("/modify-projet")
    public Projet modifyProjet(@RequestBody Projet projet) {
        return projetService.modifyProjet(projet);
    }

    // DELETE http://localhost:8089/gestionprojets/projet/remove-projet/1
    @DeleteMapping("/remove-projet/{projetId}")
    public void removeProjet(@PathVariable Long projetId) {
        projetService.removeProjet(projetId);
    }
}
