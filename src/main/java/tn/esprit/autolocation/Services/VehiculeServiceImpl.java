package tn.esprit.autolocation.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocation.Entities.Vehicule;
import tn.esprit.autolocation.Repositories.VehiculeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService{
VehiculeRepository vehiculeRepository ;
    @Override
    public Vehicule ajouterVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public void supprimerVehicule(Long id) {
        vehiculeRepository.deleteById(id);

    }

    @Override
    public List<Vehicule> afficherVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule modifierVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule AfficherVehiculeByID(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }
}
