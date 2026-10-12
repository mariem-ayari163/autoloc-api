package tn.esprit.autolocation.Repositories;

import tn.esprit.autolocation.Entities.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
