# Plan d'implémentation : AutoLoc Vehicule Foundation

## Aperçu

Mise en place de la fondation technique complète de l'API AutoLoc : correction des dépendances Maven, configuration MySQL, création de l'entité `Vehicule` avec sa chaîne en couches complète (Repository → Service → Controller), et tests de propriétés jqwik couvrant les 7 propriétés de correction définies dans le design.

## Tâches

- [x] 1. Corriger les dépendances Maven dans `pom.xml`
  - Remplacer `spring-boot-starter-data-jpa-test` par `spring-boot-starter-test` (scope test)
  - Supprimer `spring-boot-starter-webmvc-test`
  - Ajouter `net.jqwik:jqwik:1.9.2` (scope test) pour les tests de propriétés
  - Ajouter `com.h2database:h2` (scope test) pour `@DataJpaTest` en mémoire
  - _Requirements: 2.1, 2.2_

- [x] 2. Configurer `application.properties`
  - Définir `spring.datasource.url`, `username` et `password` pour MySQL (`autoloc_db`)
  - Définir `spring.jpa.hibernate.ddl-auto=update`
  - Définir `spring.jpa.show-sql=true` et `spring.jpa.properties.hibernate.format_sql=true`
  - Définir les niveaux de logging Hibernate et applicatif
  - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5_

- [x] 3. Créer l'enum `Categorie` et l'entité JPA `Vehicule`
  - [x] 3.1 Créer l'enum `Categorie` dans `tn.esprit.autoloc.entity`
    - Valeurs : `CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE`, `LUXE`
    - _Requirements: 3.9_

  - [x] 3.2 Créer la classe `Vehicule` dans `tn.esprit.autoloc.entity`
    - Annotations : `@Entity`, `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
    - Champ `idVehicule` : `Long` avec `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`
    - Champ `marque` : `String` avec `@Column(nullable = false)`
    - Champ `modele` : `String` avec `@Column(nullable = false)`
    - Champ `immatriculation` : `String` avec `@Column(unique = true, nullable = false)`
    - Champ `couleur` : `String` sans contrainte
    - Champ `kilometrage` : `int` (défaut `0`)
    - Champ `disponible` : `boolean` avec `@Column(columnDefinition = "boolean default true")`
    - Champ `prixParJour` : `double` avec `@Column(nullable = false)`
    - Champ `categorie` : `Categorie` avec `@Enumerated(EnumType.STRING)`
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9_

- [x] 4. Créer la couche Repository
  - [x] 4.1 Créer l'interface `VehiculeRepository` dans `tn.esprit.autoloc.repository`
    - Étendre `JpaRepository<Vehicule, Long>`
    - _Requirements: 4.1, 4.2_

  - [ ]* 4.2 Écrire les tests de propriétés pour `VehiculeRepository` (Properties 1, 2, 3)
    - Créer `VehiculeRepositoryPropertyTest` annoté `@DataJpaTest` dans `tn.esprit.autoloc.repository`
    - **Property 1 : Persistence round-trip préserve tous les champs**
      - `@Property(tries = 100)` — générer marque, modèle, immatriculation, prixParJour, categorie
      - Sauvegarder, relire via `findById`, vérifier l'égalité champ par champ
      - **Valide : Requirements 3.1–3.9, 4.4**
    - **Property 2 : Unicité de l'immatriculation**
      - `@Property(tries = 100)` — persister deux `Vehicule` avec la même immatriculation
      - Vérifier qu'une `DataIntegrityViolationException` est levée
      - **Valide : Requirements 3.11**
    - **Property 3 : Identifiant non nul après persistance**
      - `@Property(tries = 100)` — sauvegarder un `Vehicule` valide
      - Vérifier que `idVehicule` retourné est non nul et strictement positif
      - **Valide : Requirements 3.1, 4.4**

- [x] 5. Checkpoint — S'assurer que les tests compilent et passent
  - S'assurer que `mvn test` compile sans erreur de dépendances et que les tests de propriétés du Repository passent. Poser la question à l'utilisateur si des questions se posent.

- [x] 6. Créer la couche Service
  - [x] 6.1 Créer l'interface `IVehiculeService` dans `tn.esprit.autoloc.service`
    - Déclarer `addVehicule(Vehicule)`, `updateVehicule(Vehicule)`, `deleteVehicule(Long)`, `getVehicule(Long)`, `getAllVehicules()`
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [x] 6.2 Créer `VehiculeServiceImpl` dans `tn.esprit.autoloc.service.impl`
    - Annotation `@Service` + `@AllArgsConstructor` (Lombok)
    - Injecter `VehiculeRepository` par constructeur
    - Implémenter chaque méthode en déléguant au repository
    - `getVehicule` : `findById(...).orElseThrow()` → lève `NoSuchElementException`
    - _Requirements: 5.6, 5.7_

  - [ ]* 6.3 Écrire les tests de propriétés pour `VehiculeServiceImpl` (Properties 4, 7)
    - Créer `VehiculeServicePropertyTest` annoté `@DataJpaTest` + instanciation manuelle du service
    - **Property 4 : getVehicule avec id inexistant lève NoSuchElementException**
      - `@Property(tries = 100)` — générer un `Long` absent de la base (plage haute)
      - Vérifier que `vehiculeService.getVehicule(absentId)` lève `NoSuchElementException`
      - **Valide : Requirements 5.7**
    - **Property 7 : Délégation service → repository**
      - `@Property(tries = 100)` — appeler `addVehicule` et `VehiculeRepository.save` sur le même `Vehicule`
      - Vérifier que les deux résultats ont le même `idVehicule` non nul et les mêmes champs
      - **Valide : Requirements 5.6**

- [x] 7. Créer la couche Controller
  - [x] 7.1 Créer `VehiculeController` dans `tn.esprit.autoloc.controller`
    - Annotation `@RestController` + `@RequestMapping("/vehicule")`
    - Injecter `IVehiculeService` (l'interface)
    - Endpoint `POST /vehicule` → `addVehicule` → 200
    - Endpoint `PUT /vehicule` → `updateVehicule` → 200
    - Endpoint `DELETE /vehicule/{idVehicule}` → `deleteVehicule` → 200
    - Endpoint `GET /vehicule/{idVehicule}` → `getVehicule` → 200
    - Endpoint `GET /vehicule` → `getAllVehicules` → 200
    - `@ExceptionHandler(NoSuchElementException.class)` → `@ResponseStatus(HttpStatus.NOT_FOUND)` → 404
    - `@ExceptionHandler(DataIntegrityViolationException.class)` → `@ResponseStatus(HttpStatus.CONFLICT)` → 409
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8_

  - [ ]* 7.2 Écrire les tests unitaires MockMvc pour `VehiculeController`
    - Créer `VehiculeControllerTest` annoté `@WebMvcTest(VehiculeController.class)` + `@MockBean IVehiculeService`
    - Test : `POST /vehicule` avec payload valide → 200
    - Test : `PUT /vehicule` avec payload valide → 200
    - Test : `DELETE /vehicule/{id}` avec id existant → 200
    - Test : `GET /vehicule/{id}` avec id existant → 200
    - Test : `GET /vehicule` → 200 + liste vide
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

  - [ ]* 7.3 Écrire les tests de propriétés pour `VehiculeController` (Properties 5, 6)
    - Créer `VehiculeControllerPropertyTest` annoté `@WebMvcTest(VehiculeController.class)` + `@MockBean IVehiculeService`
    - **Property 5 : GET /vehicule/{id} inexistant retourne 404**
      - `@Property(tries = 100)` — stub `getVehicule` pour lancer `NoSuchElementException`
      - Vérifier que `GET /vehicule/{id}` retourne le statut HTTP 404
      - **Valide : Requirements 6.7**
    - **Property 6 : POST /vehicule immatriculation dupliquée retourne 409**
      - `@Property(tries = 100)` — stub `addVehicule` pour lancer `DataIntegrityViolationException`
      - Vérifier que `POST /vehicule` retourne le statut HTTP 409
      - **Valide : Requirements 6.8**

- [x] 8. Checkpoint final — S'assurer que tous les tests passent
  - Exécuter `mvn test` et vérifier que tous les tests (smoke, unitaires, propriétés) passent sans erreur. Poser la question à l'utilisateur si des questions se posent.

## Notes

- Les tâches marquées `*` sont optionnelles et peuvent être ignorées pour un MVP rapide
- Chaque tâche référence les exigences correspondantes pour la traçabilité
- Les tests de propriétés jqwik utilisent `@Property(tries = 100)` et requièrent la dépendance `net.jqwik:jqwik:1.9.2`
- `@DataJpaTest` utilise une base H2 en mémoire (dépendance `com.h2database:h2` scope test requise)
- Le dialecte Hibernate n'est pas requis dans `application.properties` : Spring Boot 4.x / Hibernate 6.x l'auto-détecte
