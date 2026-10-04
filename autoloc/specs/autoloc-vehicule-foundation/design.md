# Design Document — AutoLoc Vehicule Foundation

## Overview

Ce document décrit la conception technique de la fondation de l'API REST **AutoLoc**, une application Spring Boot 4.1.1 / Java 17 permettant la gestion de la location de véhicules. L'objectif de cet atelier est de mettre en place le projet complet : configuration de la source de données MySQL, définition des entités JPA du domaine métier, et exposition d'une API REST CRUD pour l'entité `Vehicule` via le patron en couches classique (Repository → Service → Controller).

### Stack technique

| Élément              | Valeur                                                           |
|----------------------|------------------------------------------------------------------|
| Framework            | Spring Boot 4.1.1                                                |
| Langage              | Java 17                                                          |
| Build                | Maven                                                            |
| Base de données      | MySQL 8.x — base `autoloc_db`                                    |
| Base de données test | H2 in-memory (tests automatisés)                                 |
| ORM                  | Spring Data JPA / Hibernate 6.x                                  |
| Utilitaires          | Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) |
| PBT                  | jqwik 1.9.2                                                      |
| Package racine       | `tn.esprit.autoloc`                                              |

> **Note :** `spring.jpa.properties.hibernate.dialect` n'est pas configuré : Hibernate 6.x (Spring Boot 4.x) détecte automatiquement le dialecte depuis le driver JDBC.

---

## Architecture

L'application suit le patron **MVC en couches** standard de Spring Boot :

```
Client HTTP
     │
     ▼
┌─────────────────────────────────────────┐
│   Controller Layer                      │  @RestController
│   web/controller/VehiculeController     │  /vehicule
└────────────┬────────────────────────────┘
             │ injecte IVehiculeService
             ▼
┌─────────────────────────────────────────┐
│   Service Layer                         │  @Service
│   service/IVehiculeService              │  interface contrat métier
│   service/impl/VehiculeServiceImpl      │  implémentation + règles
└────────────┬────────────────────────────┘
             │ injecte VehiculeRepository
             ▼
┌─────────────────────────────────────────┐
│   Repository Layer                      │  extends JpaRepository
│   repository/VehiculeRepository         │  accès données JPA
└────────────┬────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────┐
│   Database                              │  MySQL — autoloc_db
│   Tables : vehicule, agence, client,    │
│   employe, equipement, reservation,     │
│   contrat, paiement, maintenance        │
└─────────────────────────────────────────┘
```

### Structure des packages

```
tn.esprit.autoloc
├── AutolocApiApplication.java
├── domain/                        ← entités JPA et enums
│   ├── Vehicule.java
│   ├── Agence.java
│   ├── Client.java
│   ├── Employe.java
│   ├── Equipement.java
│   ├── Reservation.java
│   ├── Contrat.java
│   ├── Paiement.java
│   ├── Maintenance.java
│   ├── CategorieVehicule.java
│   ├── StatutVehicule.java
│   ├── RoleEmploye.java
│   ├── StatutReservation.java
│   └── ModePaiement.java
├── repository/
│   └── VehiculeRepository.java
├── service/
│   ├── IVehiculeService.java
│   └── impl/
│       └── VehiculeServiceImpl.java
└── web/
    ├── controller/
    │   └── VehiculeController.java
    └── dto/                       ← placeholder (ateliers futurs)
```

### Flux de données

1. Le client HTTP envoie une requête REST au `VehiculeController`.
2. Le contrôleur délègue la logique métier à `IVehiculeService` (injecté via l'interface).
3. `VehiculeServiceImpl` orchestre les opérations et délègue la persistance à `VehiculeRepository`.
4. `VehiculeRepository` (JpaRepository) exécute les requêtes SQL sur MySQL via Hibernate.
5. La réponse remonte en sens inverse jusqu'au client sous forme JSON.

---

## Components and Interfaces

### 1. `application.properties` — Configuration datasource

```properties
spring.application.name=autoloc-api

# DataSource MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/autoloc_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.tn.esprit.autoloc=INFO
```

### 2. `pom.xml` — Dépendances

Dépendances de test effectives :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>net.jqwik</groupId>
    <artifactId>jqwik</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

La configuration `maven-surefire-plugin` utilise `<forkCount>0</forkCount>` pour éviter les problèmes de forking JVM avec Java 23 / jqwik.

### 3. Enums du domaine

**Package :** `tn.esprit.autoloc.domain`

| Enum                | Valeurs                                           |
|---------------------|---------------------------------------------------|
| `CategorieVehicule` | `CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE`        |
| `StatutVehicule`    | `DISPONIBLE`, `LOUE`, `MAINTENANCE`               |
| `RoleEmploye`       | `AGENT`, `MANAGER`                                |
| `StatutReservation` | `EN_ATTENTE`, `CONFIRMEE`, `ANNULEE`, `TERMINEE`  |
| `ModePaiement`      | `CARTE`, `ESPECES`, `VIREMENT`                    |

Tous les enums sont persistés via `@Enumerated(EnumType.STRING)`.

### 4. `Vehicule` — Entité JPA principale

**Package :** `tn.esprit.autoloc.domain`

Annotations Lombok : `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` (pas `@Data`).

| Champ             | Type                | Annotations JPA                                                    |
|-------------------|---------------------|--------------------------------------------------------------------|
| `idVehicule`      | `Long`              | `@Id`, `@GeneratedValue(IDENTITY)`                                 |
| `immatriculation` | `String`            | `@Column(nullable=false, unique=true, length=20)`                  |
| `marque`          | `String`            | `@Column(nullable=false, length=50)`                               |
| `modele`          | `String`            | `@Column(nullable=false, length=50)`                               |
| `categorie`       | `CategorieVehicule` | `@Enumerated(EnumType.STRING)`, `@Column(nullable=false, length=20)` |
| `tarifJournalier` | `BigDecimal`        | `@Column(nullable=false, precision=10, scale=2)`                   |
| `statut`          | `StatutVehicule`    | `@Enumerated(EnumType.STRING)`, `@Column(nullable=false, length=20)` |

### 5. `VehiculeRepository` — Interface Repository

**Package :** `tn.esprit.autoloc.repository`

Étend `JpaRepository<Vehicule, Long>` — fournit automatiquement `save`, `findById`, `findAll`, `deleteById`, `existsById`, etc.

```java
package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
}
```

### 6. `IVehiculeService` — Interface Service

**Package :** `tn.esprit.autoloc.service`

```java
package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;
import java.util.List;

public interface IVehiculeService {
    Vehicule addVehicule(Vehicule vehicule);
    Vehicule updateVehicule(Vehicule vehicule);
    void deleteVehicule(Long idVehicule);
    Vehicule getVehicule(Long idVehicule);
    List<Vehicule> getAllVehicules();
}
```

### 7. `VehiculeServiceImpl` — Implémentation Service

**Package :** `tn.esprit.autoloc.service.impl`

- `@Service` pour la détection automatique Spring
- `@AllArgsConstructor` pour l'injection par constructeur de `VehiculeRepository`
- `getVehicule` utilise `findById(...).orElseThrow()` → lève `NoSuchElementException` si absent

```java
@Service
@AllArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;

    @Override public Vehicule addVehicule(Vehicule v)     { return vehiculeRepository.save(v); }
    @Override public Vehicule updateVehicule(Vehicule v)  { return vehiculeRepository.save(v); }
    @Override public void deleteVehicule(Long id)         { vehiculeRepository.deleteById(id); }
    @Override public Vehicule getVehicule(Long id)        { return vehiculeRepository.findById(id).orElseThrow(); }
    @Override public List<Vehicule> getAllVehicules()      { return vehiculeRepository.findAll(); }
}
```

### 8. `VehiculeController` — Contrôleur REST

**Package :** `tn.esprit.autoloc.web.controller`

- `@RestController` + `@RequestMapping("/vehicule")`
- `@AllArgsConstructor` pour l'injection de `IVehiculeService` (interface)
- Gestion locale des erreurs via `@ExceptionHandler`

| Méthode HTTP | URL                       | Corps          | Statut retour               |
|--------------|---------------------------|----------------|-----------------------------|
| `POST`       | `/vehicule`               | `Vehicule` JSON| 200 — `Vehicule` persisté   |
| `PUT`        | `/vehicule`               | `Vehicule` JSON| 200 — `Vehicule` mis à jour |
| `DELETE`     | `/vehicule/{idVehicule}`  | —              | 200                         |
| `GET`        | `/vehicule/{idVehicule}`  | —              | 200 ou 404                  |
| `GET`        | `/vehicule`               | —              | 200 — `List<Vehicule>`      |

**Gestion des erreurs HTTP :**

```java
@ExceptionHandler(NoSuchElementException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String handleNotFound(NoSuchElementException ex) { return ex.getMessage(); }

@ExceptionHandler(DataIntegrityViolationException.class)
@ResponseStatus(HttpStatus.CONFLICT)
public String handleConflict(DataIntegrityViolationException ex) { return ex.getMessage(); }
```

---

## Data Models

### Entité `Vehicule` — Table MySQL générée par Hibernate

```sql
CREATE TABLE vehicule (
    id_vehicule      BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    immatriculation  VARCHAR(20)   NOT NULL UNIQUE,
    marque           VARCHAR(50)   NOT NULL,
    modele           VARCHAR(50)   NOT NULL,
    categorie        VARCHAR(20)   NOT NULL,  -- 'CITADINE'|'BERLINE'|'SUV'|'UTILITAIRE'
    tarif_journalier DECIMAL(10,2) NOT NULL,
    statut           VARCHAR(20)   NOT NULL   -- 'DISPONIBLE'|'LOUE'|'MAINTENANCE'
);
```

> **Note :** Hibernate 6.x génère des noms de colonnes en `snake_case` par défaut à partir des champs Java en `camelCase`.

### Entités du domaine métier (fondation)

Toutes les entités se trouvent dans `tn.esprit.autoloc.domain` et utilisent `@Getter`/`@Setter`/`@NoArgsConstructor`/`@AllArgsConstructor`.

#### `Agence`

| Champ       | Type     | Contraintes                       |
|-------------|----------|-----------------------------------|
| `idAgence`  | `Long`   | `@Id`, `IDENTITY`                 |
| `nom`       | `String` | `nullable=false`, `length=100`    |
| `ville`     | `String` | `nullable=false`, `length=100`    |
| `adresse`   | `String` | `length=255`                      |
| `telephone` | `String` | `length=20`                       |

#### `Client`

| Champ             | Type        | Contraintes                                    |
|-------------------|-------------|------------------------------------------------|
| `idClient`        | `Long`      | `@Id`, `IDENTITY`                              |
| `nom`             | `String`    | `nullable=false`, `length=50`                  |
| `prenom`          | `String`    | `nullable=false`, `length=50`                  |
| `email`           | `String`    | `nullable=false`, `unique=true`, `length=100`  |
| `telephone`       | `String`    | `length=20`                                    |
| `numPermis`       | `String`    | `nullable=false`, `unique=true`, `length=30`   |
| `dateInscription` | `LocalDate` | `nullable=false`                               |

#### `Employe`

| Champ       | Type          | Contraintes                                             |
|-------------|---------------|---------------------------------------------------------|
| `idEmploye` | `Long`        | `@Id`, `IDENTITY`                                       |
| `nom`       | `String`      | `nullable=false`, `length=50`                           |
| `prenom`    | `String`      | `nullable=false`, `length=50`                           |
| `role`      | `RoleEmploye` | `@Enumerated(STRING)`, `nullable=false`, `length=20`    |

#### `Equipement`

| Champ          | Type     | Contraintes                    |
|----------------|----------|--------------------------------|
| `idEquipement` | `Long`   | `@Id`, `IDENTITY`              |
| `libelle`      | `String` | `nullable=false`, `length=100` |

#### `Reservation`

| Champ           | Type                | Contraintes                                              |
|-----------------|---------------------|----------------------------------------------------------|
| `idReservation` | `Long`              | `@Id`, `IDENTITY`                                        |
| `dateDebut`     | `LocalDate`         | `nullable=false`                                         |
| `dateFin`       | `LocalDate`         | `nullable=false`                                         |
| `statut`        | `StatutReservation` | `@Enumerated(STRING)`, `nullable=false`, `length=20`     |

#### `Contrat`

| Champ           | Type        | Contraintes                               |
|-----------------|-------------|-------------------------------------------|
| `idContrat`     | `Long`      | `@Id`, `IDENTITY`                         |
| `dateSignature` | `LocalDate` | `nullable=false`                          |
| `montantTotal`  | `BigDecimal`| `nullable=false`, `precision=10, scale=2` |
| `valide`        | `boolean`   | `nullable=false`                          |

#### `Paiement`

| Champ          | Type           | Contraintes                                           |
|----------------|----------------|-------------------------------------------------------|
| `idPaiement`   | `Long`         | `@Id`, `IDENTITY`                                     |
| `montant`      | `BigDecimal`   | `nullable=false`, `precision=10, scale=2`             |
| `datePaiement` | `LocalDate`    | `nullable=false`                                      |
| `modePaiement` | `ModePaiement` | `@Enumerated(STRING)`, `nullable=false`, `length=20`  |

#### `Maintenance`

| Champ            | Type        | Contraintes                    |
|------------------|-------------|--------------------------------|
| `idMaintenance`  | `Long`      | `@Id`, `IDENTITY`              |
| `dateDebut`      | `LocalDate` | `nullable=false`               |
| `dateFin`        | `LocalDate` | nullable (fin optionnelle)     |
| `description`    | `String`    | `length=500`                   |

### Exemple de payload JSON (POST /vehicule)

```json
{
  "immatriculation": "123TU4567",
  "marque": "Renault",
  "modele": "Clio",
  "categorie": "CITADINE",
  "tarifJournalier": 45.00,
  "statut": "DISPONIBLE"
}
```

---

## Correctness Properties

*Une propriété est une caractéristique ou un comportement qui doit rester vrai pour toutes les exécutions valides d'un système — autrement dit, un énoncé formel sur ce que le système est censé faire. Les propriétés servent de pont entre les spécifications lisibles par l'humain et les garanties de correction vérifiables par la machine.*

### Property 1 : Persistence round-trip préserve tous les champs

*Pour tout* `Vehicule` valide (immatriculation, marque, modèle non nuls dans leurs limites de longueur ; tarifJournalier > 0 ; categorie et statut valides), persister l'entité via `VehiculeRepository.save()` puis la relire via `findById()` doit retourner une entité avec des valeurs de champs identiques à celles de l'entité d'origine : `immatriculation`, `marque`, `modele`, `categorie`, `tarifJournalier`, `statut`.

**Validates: Requirements 3.1, 3.2, 3.3, 3.4, 4.4**

---

### Property 2 : Unicité de l'immatriculation

*Pour toute* valeur d'`immatriculation` déjà persistée en base, tenter de sauvegarder un second `Vehicule` avec la même valeur doit lever une `DataIntegrityViolationException`.

**Validates: Requirements 3.4, 3.11**

---

### Property 3 : Identifiant non nul après persistance

*Pour tout* `Vehicule` valide sauvegardé via `VehiculeRepository.save()`, l'entité retournée doit avoir un `idVehicule` non nul et strictement positif.

**Validates: Requirements 3.1, 4.4**

> **Réflexion sur la redondance :** La Property 3 est distincte de la Property 1 : elle vérifie spécifiquement que la stratégie `IDENTITY` génère bien un identifiant, indépendamment de la préservation des autres champs. Les deux propriétés sont complémentaires et non redondantes.

---

### Property 4 : getVehicule avec id inexistant lève NoSuchElementException

*Pour tout* identifiant `Long` non présent dans la base de données, appeler `VehiculeServiceImpl.getVehicule(id)` doit lever une `NoSuchElementException`.

**Validates: Requirements 5.7**

---

### Property 5 : GET /vehicule/{id} retourne 404 pour id inexistant

*Pour tout* identifiant `Long` non présent dans la base de données, une requête `GET /vehicule/{id}` doit retourner le statut HTTP 404.

**Validates: Requirements 6.7**

> **Réflexion sur la redondance :** La Property 5 valide la couche HTTP (@ExceptionHandler dans le contrôleur), tandis que la Property 4 valide la couche service. Elles testent des composants différents et sont toutes deux nécessaires.

---

### Property 6 : POST /vehicule avec immatriculation dupliquée retourne 409

*Pour toute* immatriculation déjà persistée en base, une requête `POST /vehicule` avec la même immatriculation doit retourner le statut HTTP 409 (Conflict).

**Validates: Requirements 6.8**

---

### Property 7 : Délégation service → repository

*Pour tout* `Vehicule` valide, appeler `VehiculeServiceImpl.addVehicule(vehicule)` doit produire le même résultat (entité persistée avec id attribué) que d'appeler directement `VehiculeRepository.save(vehicule)`.

**Validates: Requirements 5.6**

---

## Error Handling

| Scénario d'erreur                             | Couche détectrice    | Exception levée                      | Réponse HTTP       |
|-----------------------------------------------|----------------------|--------------------------------------|--------------------|
| `idVehicule` inexistant (lecture/suppression) | Service              | `NoSuchElementException`             | 404 Not Found      |
| `immatriculation` dupliquée                   | Repository/DB        | `DataIntegrityViolationException`    | 409 Conflict       |
| Champ `nullable=false` absent                 | Repository/DB        | `DataIntegrityViolationException`    | 409 Conflict       |
| Connexion DB échouée au démarrage             | Spring Boot Context  | `ApplicationContextException`        | Arrêt application  |

La distinction 400 / 409 pour les violations de contraintes non-null pourra être affinée dans un atelier futur avec Bean Validation (`@Valid`).

### Stratégie de gestion des exceptions dans le contrôleur

```
NoSuchElementException           → 404 Not Found
DataIntegrityViolationException  → 409 Conflict
```

---

## Testing Strategy

### Approche duale : Tests unitaires + Tests de propriétés

La stratégie de test combine deux approches complémentaires :

1. **Tests unitaires** (exemples concrets) — scénarios spécifiques, chemins heureux, cas limites
2. **Tests de propriétés** (property-based testing) — propriétés universelles validées sur 100+ inputs générés aléatoirement

### Bibliothèque de property-based testing

**[jqwik](https://jqwik.net/)** version `1.9.2`, intégré à JUnit 5 (`forkCount=0` dans maven-surefire-plugin).

Chaque test de propriété est configuré avec `@Property(tries = 100)` au minimum.

### Infrastructure de test

| Annotation                    | Usage                                                        |
|-------------------------------|--------------------------------------------------------------|
| `@SpringBootTest`             | Smoke test de démarrage du contexte complet (H2 override)    |
| `@DataJpaTest`                | Tests repository et propriétés JPA (H2 in-memory)            |
| `@WebMvcTest` + `@MockBean`   | Tests contrôleur avec MockMvc                                |

### Tests de smoke / configuration

| Test                                                            | Type  | Requirement      |
|-----------------------------------------------------------------|-------|------------------|
| `mvn compile` sans erreur de dépendance                         | Smoke | 2.1, 2.2, 2.3    |
| `AutolocApiApplicationTests.contextLoads()` avec H2             | Smoke | 1.6              |

Le smoke test utilise `@TestPropertySource` pour remplacer MySQL par H2 in-memory :

```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AutolocApiApplicationTests {
    @Test
    void contextLoads() { }
}
```

### Tests unitaires (exemples)

**Couche Repository (`@DataJpaTest`) :**

| Test                                          | Résultat attendu  |
|-----------------------------------------------|-------------------|
| `findAll()` sur repository vide → liste vide  | `[]`              |
| `save()` + `findById()` → entité retrouvée    | présent           |
| `deleteById()` → entité absente               | `Optional.empty`  |

**Couche Controller (`@WebMvcTest` + `@MockBean`) :**

| Test                                              | Endpoint               | Statut attendu |
|---------------------------------------------------|------------------------|----------------|
| POST avec payload valide (service mocké)          | POST `/vehicule`       | 200            |
| PUT avec payload valide (service mocké)           | PUT `/vehicule`        | 200            |
| DELETE avec id (service mocké)                    | DELETE `/vehicule/{id}`| 200            |
| GET avec id existant (service mocké)              | GET `/vehicule/{id}`   | 200            |
| GET liste (service mocké)                         | GET `/vehicule`        | 200            |

### Tests de propriétés (property-based)

Chaque propriété du document est implémentée avec un unique test `@Property`.

**Tag format :** `Feature: autoloc-vehicule-foundation, Property {N}: {texte}`

| Property | Titre                                                    | Tries | Requirements   |
|----------|----------------------------------------------------------|-------|----------------|
| 1        | Persistence round-trip préserve tous les champs          | 100   | 3.1–3.4, 4.4   |
| 2        | Unicité de l'immatriculation                             | 100   | 3.4, 3.11      |
| 3        | Identifiant non nul après persistance                    | 100   | 3.1, 4.4       |
| 4        | getVehicule avec id inexistant → NoSuchElementException  | 100   | 5.7            |
| 5        | GET /vehicule/{id} inexistant → 404                      | 100   | 6.7            |
| 6        | POST /vehicule immatriculation dupliquée → 409           | 100   | 6.8            |
| 7        | Délégation service → repository                          | 100   | 5.6            |

**Exemple de squelette de test (Property 1) :**

```java
// Feature: autoloc-vehicule-foundation, Property 1: Persistence round-trip préserve tous les champs
@Property(tries = 100)
void persistenceRoundTripPreservesFields(
    @ForAll @StringLength(min = 1, max = 20) @AlphaChars String immatriculation,
    @ForAll @StringLength(min = 1, max = 50) @AlphaChars String marque,
    @ForAll @StringLength(min = 1, max = 50) @AlphaChars String modele,
    @ForAll CategorieVehicule categorie,
    @ForAll @BigDecimalRange(min = "1.00", max = "9999.99") BigDecimal tarifJournalier,
    @ForAll StatutVehicule statut
) {
    Vehicule v = new Vehicule(null, immatriculation, marque, modele,
                               categorie, tarifJournalier, statut);
    Vehicule saved = vehiculeRepository.save(v);
    Vehicule found = vehiculeRepository.findById(saved.getIdVehicule()).orElseThrow();

    assertThat(found.getImmatriculation()).isEqualTo(immatriculation);
    assertThat(found.getMarque()).isEqualTo(marque);
    assertThat(found.getModele()).isEqualTo(modele);
    assertThat(found.getCategorie()).isEqualTo(categorie);
    assertThat(found.getTarifJournalier()).isEqualByComparingTo(tarifJournalier);
    assertThat(found.getStatut()).isEqualTo(statut);
}
```

**Exemple de squelette de test (Property 4) :**

```java
// Feature: autoloc-vehicule-foundation, Property 4: getVehicule avec id inexistant → NoSuchElementException
@Property(tries = 100)
void getVehiculeWithAbsentIdThrows(
    @ForAll @LongRange(min = 100_000L, max = Long.MAX_VALUE) Long absentId
) {
    assertThatThrownBy(() -> vehiculeService.getVehicule(absentId))
        .isInstanceOf(NoSuchElementException.class);
}
```

**Exemple de squelette de test (Property 5) :**

```java
// Feature: autoloc-vehicule-foundation, Property 5: GET /vehicule/{id} inexistant → 404
@Property(tries = 100)
void getEndpointReturns404ForAbsentId(
    @ForAll @LongRange(min = 100_000L, max = Long.MAX_VALUE) Long absentId
) throws Exception {
    given(vehiculeService.getVehicule(absentId))
        .willThrow(new NoSuchElementException());

    mockMvc.perform(get("/vehicule/{id}", absentId))
           .andExpect(status().isNotFound());
}
```
