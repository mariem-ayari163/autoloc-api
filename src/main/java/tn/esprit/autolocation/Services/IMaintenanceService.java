package tn.esprit.autolocation.Services;

import tn.esprit.autolocation.Entities.Maintenance;

import java.util.List;

public interface IMaintenanceService {
    Maintenance ajouterMaintenance(Maintenance maintenance);
    void supprimerMaintenance(Long id);
    List<Maintenance> afficherMaintenances();
    Maintenance modifierMaintenance(Maintenance maintenance);
    Maintenance afficherMaintenanceByID(Long id);
}
