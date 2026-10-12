
package tn.esprit.autolocation.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import tn.esprit.autolocation.Entities.Client;
import tn.esprit.autolocation.Services.IClientService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Client")
public class ClientController {

    private final IClientService clientService;

    @PostMapping("/ajouterClient")
    public Client ajouterClient(@RequestBody Client client) {
        return clientService.ajouterClient(client);
    }

    @PutMapping("/modifierClient")
    public Client modifierClient(@RequestBody Client client) {
        return clientService.modifierClient(client);
    }

    @DeleteMapping("/supprimerClient/{id}")
    public void supprimerClient(@PathVariable Long id) {
        clientService.supprimerClient(id);
    }

    @GetMapping("/getClient/{id}")
    public Client afficherClientByID(@PathVariable Long id) {
        return clientService.AfficherClientByID(id);
    }

    @GetMapping("/allClient")
    public List<Client> afficherClients() {
        return clientService.afficherClients();
    }
}


