package tn.esprit.autolocation.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.autolocation.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


}
