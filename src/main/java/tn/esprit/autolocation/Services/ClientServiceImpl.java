package tn.esprit.autolocation.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocation.Entities.Client;
import tn.esprit.autolocation.Repositories.ClientRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements IClientService {
    private ClientRepository clientRepository ;
    @Override
    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void supprimerClient(Long id) {
clientRepository.deleteById(id);
    }

    @Override
    public List<Client> afficherClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client modifierClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client AfficherClientByID(Long id) {
        return  clientRepository.findById(id).orElse(null);
    }
}
