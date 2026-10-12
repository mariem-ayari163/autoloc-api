package tn.esprit.autolocation.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocation.Entities.Equipement;
import tn.esprit.autolocation.Repositories.EquipementRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EquipementService implements IEquipementService{
    EquipementRepository equipementRepository;
    @Override
    public Equipement ajouterEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public void supprimerEquipement(Long id) {
        equipementRepository.deleteById(id);
    }

    @Override
    public List<Equipement> afficherEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement modifierEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement AfficherEquipementByID(Long id) {
        return equipementRepository.findById(id).orElse(null);
    }
}
