# Requirements Document

## Introduction

Ce document définit les exigences pour la fondation technique du projet **AutoLoc API**, une API REST de gestion de location de véhicules construite avec Spring Boot. L'objectif de cet atelier est de mettre en place le squelette technique complet connecté à une base de données MySQL, incluant la première entité domaine `Vehicule`. Cette fondation servira de base pour tous les ateliers futurs.

---

## Glossary

- **AutoLoc_API** : L'application Spring Boot centrale gérant les ressources de location de véhicules.
- **Vehicule** : Entité domaine représentant un véhicule disponible à la location.
- **VehiculeRepository** : Composant d'accès aux données pour l'entité `Vehicule`, étendant `JpaRepository`.
- **VehiculeService** : Couche de logique métier responsable des opérations CRUD sur les véhicules.
- **VehiculeController** : Contrôleur REST exposant les endpoints HTTP pour la gestion des véhicules.
- **Categorie** : Enumération définissant le type de véhicule (`CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE`, `LUXE`).
- **DDL_Auto** : Configuration Hibernate pour la gestion automatique du schéma de base de données.
- **CRUD** : Ensemble des opérations Create, Read, Update, Delete.

---

## Requirements

### Requirement 1 : Configuration de la source de données

**User Story:** En tant que développeur, je veux configurer la connexion MySQL dans `application.properties`, afin que l'application puisse communiquer avec la base de données au démarrage.

#### Acceptance Criteria

1. THE `AutoLoc_API` SHALL définir la propriété `spring.datasource.url` avec l'URL JDBC de la base de données MySQL cible.
2. THE `AutoLoc_API` SHALL définir les propriétés `spring.datasource.username` et `spring.datasource.password` pour l'authentification à la base de données.
3. THE `AutoLoc_API` SHALL définir `spring.jpa.hibernate.ddl-auto=update` afin que Hibernate mette à jour le schéma automatiquement sans supprimer les données existantes.
4. THE `AutoLoc_API` SHALL définir `spring.jpa.show-sql=true` pour afficher les requêtes SQL générées dans les logs.
5. THE `AutoLoc_API` SHALL définir le dialecte Hibernate `spring.jpa.properties.hibernate.dialect` correspondant à la version MySQL utilisée.
6. WHEN l'application démarre avec une configuration de datasource valide, THE `AutoLoc_API` SHALL établir une connexion à la base de données MySQL sans erreur.
7. IF la connexion à la base de données échoue au démarrage, THEN THE `AutoLoc_API` SHALL lever une exception et interrompre le démarrage avec un message d'erreur descriptif.

---

### Requirement 2 : Correction des dépendances Maven

**User Story:** En tant que développeur, je veux que le fichier `pom.xml` référence des dépendances de test valides, afin que le projet compile et que les tests s'exécutent correctement.

#### Acceptance Criteria

1. THE `AutoLoc_API` SHALL remplacer la dépendance `spring-boot-starter-data-jpa-test` (inexistante) par `spring-boot-starter-test` dans le `pom.xml`.
2. THE `AutoLoc_API` SHALL supprimer la dépendance `spring-boot-starter-webmvc-test` (inexistante) du `pom.xml`.
3. WHEN la commande `mvn compile` est exécutée, THE `AutoLoc_API` SHALL compiler sans erreur de résolution de dépendances.
4. WHEN la commande `mvn test` est exécutée, THE `AutoLoc_API` SHALL exécuter les tests sans erreur liée aux dépendances manquantes.

---

### Requirement 3 : Entité Vehicule

**User Story:** En tant que développeur, je veux définir l'entité JPA `Vehicule`, afin que la table correspondante soit créée automatiquement dans la base de données MySQL.

#### Acceptance Criteria

1. THE `Vehicule` SHALL posséder un champ `idVehicule` de type `Long`, annoté comme clé primaire avec une stratégie de génération automatique (`IDENTITY`).
2. THE `Vehicule` SHALL posséder un champ `marque` de type `String` annoté `@Column(nullable = false)`.
3. THE `Vehicule` SHALL posséder un champ `modele` de type `String` annoté `@Column(nullable = false)`.
4. THE `Vehicule` SHALL posséder un champ `immatriculation` de type `String` annoté `@Column(unique = true, nullable = false)`.
5. THE `Vehicule` SHALL posséder un champ `couleur` de type `String`.
6. THE `Vehicule` SHALL posséder un champ `kilometrage` de type `int`.
7. THE `Vehicule` SHALL posséder un champ `disponible` de type `boolean` avec la valeur par défaut `true`.
8. THE `Vehicule` SHALL posséder un champ `prixParJour` de type `double` annoté `@Column(nullable = false)`.
9. THE `Vehicule` SHALL posséder un champ `categorie` de type énuméré `Categorie` (`CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE`, `LUXE`), persisté comme chaîne de caractères (`@Enumerated(EnumType.STRING)`).
10. WHEN Hibernate initialise le contexte de persistance avec `ddl-auto=update`, THE `Vehicule` SHALL générer une table `vehicule` dans la base de données avec toutes les colonnes correspondant aux champs définis.
11. IF deux enregistrements `Vehicule` ont la même valeur d'`immatriculation`, THEN THE `VehiculeRepository` SHALL lever une exception d'intégrité lors de la tentative de persistance du doublon.

---

### Requirement 4 : Couche d'accès aux données (Repository)

**User Story:** En tant que développeur, je veux un `VehiculeRepository` étendant `JpaRepository`, afin de bénéficier des opérations CRUD standard sans code boilerplate.

#### Acceptance Criteria

1. THE `VehiculeRepository` SHALL étendre `JpaRepository<Vehicule, Long>`.
2. THE `VehiculeRepository` SHALL hériter des méthodes `save`, `findById`, `findAll` et `deleteById` fournies par `JpaRepository`.
3. WHEN `VehiculeRepository.findAll()` est appelé sur une base de données vide, THE `VehiculeRepository` SHALL retourner une liste vide.
4. WHEN `VehiculeRepository.save(vehicule)` est appelé avec un `Vehicule` valide, THE `VehiculeRepository` SHALL persister l'entité et retourner l'instance avec `idVehicule` renseigné.

---

### Requirement 5 : Couche service (Interface et Implémentation)

**User Story:** En tant que développeur, je veux une interface `IVehiculeService` et son implémentation `VehiculeServiceImpl`, afin d'encapsuler la logique métier des opérations sur les véhicules.

#### Acceptance Criteria

1. THE `IVehiculeService` SHALL déclarer une méthode `addVehicule(Vehicule vehicule)` retournant `Vehicule`.
2. THE `IVehiculeService` SHALL déclarer une méthode `updateVehicule(Vehicule vehicule)` retournant `Vehicule`.
3. THE `IVehiculeService` SHALL déclarer une méthode `deleteVehicule(Long idVehicule)` retournant `void`.
4. THE `IVehiculeService` SHALL déclarer une méthode `getVehicule(Long idVehicule)` retournant `Vehicule`.
5. THE `IVehiculeService` SHALL déclarer une méthode `getAllVehicules()` retournant `List<Vehicule>`.
6. THE `VehiculeServiceImpl` SHALL implémenter toutes les méthodes déclarées dans `IVehiculeService` en déléguant les opérations de persistance au `VehiculeRepository`.
7. WHEN `VehiculeServiceImpl.getVehicule(idVehicule)` est appelé avec un identifiant inexistant, THE `VehiculeService` SHALL lever une exception `NoSuchElementException`.

---

### Requirement 6 : Couche contrôleur REST

**User Story:** En tant que client de l'API, je veux accéder aux endpoints REST du `VehiculeController`, afin d'effectuer des opérations CRUD sur les véhicules via HTTP.

#### Acceptance Criteria

1. THE `VehiculeController` SHALL exposer un endpoint `POST /vehicule` qui accepte un corps JSON de type `Vehicule` et retourne le véhicule créé avec le statut HTTP 200.
2. THE `VehiculeController` SHALL exposer un endpoint `PUT /vehicule` qui accepte un corps JSON de type `Vehicule` et retourne le véhicule mis à jour avec le statut HTTP 200.
3. THE `VehiculeController` SHALL exposer un endpoint `DELETE /vehicule/{idVehicule}` qui supprime le véhicule identifié par `idVehicule` et retourne le statut HTTP 200.
4. THE `VehiculeController` SHALL exposer un endpoint `GET /vehicule/{idVehicule}` qui retourne le véhicule correspondant à `idVehicule` avec le statut HTTP 200.
5. THE `VehiculeController` SHALL exposer un endpoint `GET /vehicule` qui retourne la liste de tous les véhicules avec le statut HTTP 200.
6. WHEN une requête `POST /vehicule` est reçue avec un corps JSON valide, THE `VehiculeController` SHALL déléguer la création au `VehiculeService` et retourner la représentation JSON du véhicule persisté.
7. WHEN une requête `GET /vehicule/{idVehicule}` est reçue avec un identifiant inexistant, THE `VehiculeController` SHALL retourner le statut HTTP 404.
8. WHEN une requête `POST /vehicule` est reçue avec un champ `immatriculation` déjà existant, THE `VehiculeController` SHALL retourner le statut HTTP 409.
