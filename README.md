# Classroom CRB

Application Spring Boot de gestion simplifiee des classes, utilisateurs et cours (enonce TP), avec API REST, UI Thymeleaf, tests H2 et dockerisation Jib.

## 1. Prerequis

1. Java 21
2. Maven 3.9+
3. Docker Desktop (pour conteneurs)
4. MySQL (si execution locale hors Docker)

## 2. Lancer le projet en local (MySQL)

1. Creer une base MySQL `classroom_crb` (ou laisser `createDatabaseIfNotExist=true` la creer).
2. Verifier/adapter les variables suivantes (ou les valeurs par defaut dans `application.properties`) :
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
3. Executer :
   ```bash
   mvn spring-boot:run
   ```
4. URLs utiles :
   - UI Thymeleaf : `http://localhost:8089/classroom/ui`
   - Swagger UI : `http://localhost:8089/classroom/swagger-ui.html`
   - OpenAPI JSON : `http://localhost:8089/classroom/api-docs`

## 3. Lancer les tests (profil H2)

1. Executer :
   ```bash
   mvn test
   ```
2. Les tests utilisent H2 fichier avec `src/test/resources/application-test.properties`.
3. Fichier BD H2 genere : `target/h2/classroom_test.mv.db`

## 4. Dockerisation avec Google Jib

1. Construire l'image Docker de l'application :
   ```bash
   mvn -DskipTests jib:dockerBuild
   ```
2. Image creee : `classroom-crb:latest`

## 5. Deploiement Docker Compose (App + MySQL)

Le projet contient `docker-compose.yml` avec 2 conteneurs :

1. `mysql` (stockage persistant via volume `mysql_data`)
2. `app` (image `classroom-crb:latest`)

Execution :
```bash
docker compose up -d
```

Arret :
```bash
docker compose down
```

## 6. Architecture du projet

Architecture en couches :

1. **Controller**
   - REST : exposition des endpoints metier + CRUD
   - MVC : page Thymeleaf de test
2. **Service**
   - Interfaces : contrats (`IUtilisateurService`, `IClasseService`, `ICoursClassroomService`)
   - Implementations : logique metier (`services/Impl`)
3. **Repository**
   - `JpaRepository` pour acces BD
4. **Entity**
   - Modeles JPA (`Classe`, `Utilisateur`, `CoursClassroom`) + enums (`Niveau`, `Specialite`)

## 7. Structure des dossiers

```text
src/main/java/tn/fst/classroom_crb
|- controllers
|  |- UtilisateurRestController
|  |- ClasseRestController
|  |- CoursClassroomRestController
|  |- DashboardController
|- services
|  |- IUtilisateurService, IClasseService, ICoursClassroomService
|  |- Impl/
|- repositories
|- entities
|- config (SecurityConfig)
|- ClassroomCrbApplication

src/main/resources
|- application.properties
|- templates/dashboard.html
|- static/css/dashboard.css
|- static/js/dashboard.js

src/test/java/tn/fst/classroom_crb
|- ClassroomCrbApplicationTests
|- ClassroomPartTwoExamplesTest
|- ClassroomCrudServicesTest
```

## 8. URI des controleurs

| Controleur | URI de base |
|---|---|
| `UtilisateurRestController` | `/classroom/api/utilisateurs` |
| `ClasseRestController` | `/classroom/api/classes` |
| `CoursClassroomRestController` | `/classroom/api/cours-classrooms` |
| `DashboardController` | `/classroom/ui` |

## 9. Fonctionnalites principales

1. Exigences enonce (partie II) :
   - Ajout utilisateurs, classes, cours + affectations
   - Nombre utilisateurs par niveau
   - Desaffectation d'un cours
   - Archivage des cours (manuel + scheduler 60s)
   - Nombre d'heures par specialite et niveau
2. CRUD complet sur Utilisateur / Classe / CoursClassroom
3. UI Thymeleaf interactive pour tester les operations rapidement

## 10. Commandes utiles

```bash
# Build + tests
mvn clean test

# Lancement local
mvn spring-boot:run

# Build image Docker via Jib
mvn -DskipTests jib:dockerBuild
# Lancer l'application avec Docker Compose
docker-compose up -d

# Build image tar via Jib
mvn -DskipTests jib:buildTar
```

## 11. Mesures de securite ajoutees

1. **Rate Limiting API**  
   Limitation par IP sur les endpoints `/api/**` (HTTP `429 Too Many Requests` + header `Retry-After`).
2. **Retry automatique service (@Retryable)**  
   Retry sur erreurs transitoires d'acces BD (`TransientDataAccessException`) dans les services metier.

Configuration (`application.properties`) :

```properties
security.rate-limit.enabled=true
security.rate-limit.max-requests=60
security.rate-limit.window-seconds=60

app.retry.max-attempts=3
app.retry.delay-ms=200
app.retry.multiplier=2.0
app.retry.max-delay-ms=2000
```

