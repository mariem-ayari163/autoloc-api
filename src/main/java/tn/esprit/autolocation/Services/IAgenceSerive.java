package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Agence;

import java.util.List;

public interface IAgenceSerive {
Agence ajouterAgence (Agence agence);
void supprimerAgence (Long id) ;
List <Agence> afficherAgences ();
    Agence modifierAgence (Agence agence);
    Agence AfficherAgenceByID (Long id);
}
