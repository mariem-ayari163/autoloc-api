package tn.esprit.autolocation.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.autolocation.Entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {


}
