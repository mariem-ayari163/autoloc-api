package tn.esprit.autolocation.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.autolocation.Entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {


}
