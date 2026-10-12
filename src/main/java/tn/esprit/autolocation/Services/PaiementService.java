package tn.esprit.autolocation.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocation.Entities.Paiement;
import tn.esprit.autolocation.Repositories.PaiementRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PaiementService implements IPaiementService {
    PaiementRepository paiementRepository ;
    @Override
    public Paiement ajouterPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public void supprimerPaiement(Long id) {
        paiementRepository.deleteById(id);

    }

    @Override
    public List<Paiement> afficherAgences() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement modifierPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement AfficherPaiementByID(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }
}
