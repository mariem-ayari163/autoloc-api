package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Equipement;

import java.util.List;
import java.util.Optional;

public interface IEquipementService {
    Equipement ajouterEquipement (Equipement equipement);
    void supprimerEquipement (Long id) ;
    List<Equipement> afficherEquipements ();
    Equipement modifierEquipement (Equipement equipement);
    Equipement  AfficherEquipementByID (Long id);
}
