package tn.esprit.autolocation.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocation.Entities.Contrat;
import tn.esprit.autolocation.Repositories.ContratRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ContratServiceImpl implements IContratService{
    private ContratRepository contratRepository ;

    @Override
    public Contrat ajouterContrat(Contrat contart) {
        return contratRepository.save(contart);
    }

    @Override
    public void supprimerContrat(Long id) {
contratRepository.deleteById(id);
    }

    @Override
    public List<Contrat> afficherContrats() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat modifierContrat(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat AfficherContratByID(Long id) {
        return contratRepository.findById(id).orElse(null);
    }
    }

