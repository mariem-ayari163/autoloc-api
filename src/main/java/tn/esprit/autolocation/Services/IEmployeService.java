package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Employe;

import java.util.List;

public interface IEmployeService {
    Employe ajouterEmploye(Employe employe);
    void supprimerEmploye(Long id);
    List<Employe> afficherEmployes();
    Employe modifierEmploye(Employe employe);
    Employe afficherEmployeByID(Long id);
}
