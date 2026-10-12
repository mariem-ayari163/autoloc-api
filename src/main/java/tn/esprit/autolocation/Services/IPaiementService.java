package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Paiement;

import java.util.List;

public interface IPaiementService {
    Paiement ajouterPaiement (Paiement paiement);
    void supprimerPaiement (Long id) ;
    List<Paiement> afficherAgences ();
    Paiement modifierPaiement (Paiement paiement);
    Paiement AfficherPaiementByID (Long id);
}
