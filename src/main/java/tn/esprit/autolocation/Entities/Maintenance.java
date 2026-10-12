package tn.esprit.autolocation.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entite Maintenance : N Maintenance -> 1 Vehicule, cascade = PERSIST.
 */
@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)

    private Vehicule vehicule;
}
