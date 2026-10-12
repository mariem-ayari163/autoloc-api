package tn.esprit.autolocation.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.autolocation.Entities.Enums.CategorieVehicule;
import tn.esprit.autolocation.Entities.Enums.StatutVehicule;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    private String marque;

    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;
    @ManyToOne(fetch = FetchType.LAZY)
    private Agence agence;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Equipement> equipements = new HashSet<>();
}
