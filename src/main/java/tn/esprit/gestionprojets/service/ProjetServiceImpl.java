package tn.esprit.gestionprojets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionprojets.entities.Projet;
import tn.esprit.gestionprojets.repositories.ProjetRepository;

import java.util.List;

@Service
public class ProjetServiceImpl implements IProjetService {

    @Autowired
    ProjetRepository projetRepository;

    @Override
    public List<Projet> retrieveAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public Projet retrieveProjet(Long projetId) {
        return projetRepository.findById(projetId).orElse(null);
    }

    @Override
    public Projet addProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    @Override
    public void removeProjet(Long projetId) {
        projetRepository.deleteById(projetId);
    }

    @Override
    public Projet modifyProjet(Projet projet) {
        return projetRepository.save(projet);
    }
}
