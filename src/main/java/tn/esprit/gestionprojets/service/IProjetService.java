package tn.esprit.gestionprojets.service;

import tn.esprit.gestionprojets.entities.Projet;

import java.util.List;

public interface IProjetService {

    List<Projet> retrieveAllProjets();

    Projet retrieveProjet(Long projetId);

    Projet addProjet(Projet projet);

    void removeProjet(Long projetId);

    Projet modifyProjet(Projet projet);
}
