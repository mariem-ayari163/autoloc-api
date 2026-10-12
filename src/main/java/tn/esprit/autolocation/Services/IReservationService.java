package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Reservation;

import java.util.List;

public interface IReservationService {
    Reservation ajouterReservation (Reservation reservation);
    void supprimerReservation (Long id) ;
    List<Reservation> afficherReservations ();
    Reservation modifierReservation (Reservation reservation);
    Reservation AfficherReservationByID (Long id);
}
