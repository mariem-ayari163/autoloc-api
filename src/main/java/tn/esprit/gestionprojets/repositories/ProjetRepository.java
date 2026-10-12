package tn.esprit.gestionprojets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.gestionprojets.entities.Projet;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
    // Spring Data JPA génère automatiquement les implémentations de :
    // findAll()        -> récupère tous les projets
    // findById(id)     -> récupère un projet par son id
    // save(projet)     -> insère ou met à jour un projet
    // deleteById(id)   -> supprime un projet par son id
}
