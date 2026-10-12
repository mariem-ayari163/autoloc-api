package tn.esprit.autolocation.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.autolocation.Entities.Employe;


import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {


}
