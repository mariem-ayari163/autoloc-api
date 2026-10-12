package tn.esprit.autolocation.Services;


import tn.esprit.autolocation.Entities.Contrat;

import java.util.List;

public interface IContratService {
    Contrat ajouterContrat (Contrat contart);
    void supprimerContrat (Long id) ;
    List<Contrat> afficherContrats ();
    Contrat modifierContrat (Contrat contrat);
    Contrat AfficherContratByID (Long id);
}
