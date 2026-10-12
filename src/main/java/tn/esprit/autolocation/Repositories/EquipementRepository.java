package tn.esprit.autolocation.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.autolocation.Entities.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
}
