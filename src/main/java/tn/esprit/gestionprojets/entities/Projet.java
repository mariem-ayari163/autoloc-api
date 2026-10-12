package tn.esprit.gestionprojets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjet;

    private String nomProjet;
    private String description;
    private float budget;

    // --- Constructeurs ---

    public Projet() {
    }

    public Projet(Long idProjet, String nomProjet, String description, float budget) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.description = description;
        this.budget = budget;
    }

    // --- Getters & Setters ---

    public Long getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(Long idProjet) {
        this.idProjet = idProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "idProjet=" + idProjet +
                ", nomProjet='" + nomProjet + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                '}';
    }
}
