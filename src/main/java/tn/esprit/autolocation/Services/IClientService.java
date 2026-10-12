package tn.esprit.autolocation.Services;


import tn.esprit.autolocation.Entities.Client;

import java.util.List;

public interface IClientService {
    Client ajouterClient (Client client);
    void supprimerClient (Long id) ;
    List<Client> afficherClients ();
    Client modifierClient (Client client);
    Client AfficherClientByID (Long id);
}
