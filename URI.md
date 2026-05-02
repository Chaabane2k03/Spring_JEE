# URIs des Requêtes (Exigences Énoncé)

Ce document répertorie les points d'entrée de l'API REST correspondant aux exigences de l'énoncé, avec des exemples de contenu JSON.

## 1. Ajouter un utilisateur (II.a)
**Action :** Créer un nouvel utilisateur.
- **Méthode :** `POST`
- **URI :** `/api/utilisateurs`
- **Contenu (JSON) :**
```json
{
  "prenom": "Jean",
  "nom": "Dupont",
  "password": "securePassword123"
}
```

## 2. Ajouter une classe (II.b)
**Action :** Créer une nouvelle classe.
- **Méthode :** `POST`
- **URI :** `/api/classes`
- **Contenu (JSON) :**
```json
{
  "titre": "Classe IGL4",
  "niveau": "DEUXIEME"
}
```
*Note : Les niveaux possibles sont `PREMIERE`, `DEUXIEME`, `TROISIEME`.*

## 3. Ajouter un cours et l'affecter à une classe (II.c)
**Action :** Créer un cours et le lier directement à une classe existante.
- **Méthode :** `POST`
- **URI :** `/api/cours-classrooms/{codeClasse}` (Ex: `/api/cours-classrooms/1`)
- **Contenu (JSON) :**
```json
{
  "nom": "Développement JEE",
  "specialite": "INFORMATIQUE",
  "nbHeures": 42,
  "archive": false
}
```
*Note : Les spécialités possibles sont `INFORMATIQUE`, `GENIE_CIVIL`, `ELECTRIQUE`.*

## 4. Affecter un utilisateur à une classe (II.d)
**Action :** Lier un utilisateur existant à une classe existante.
- **Méthode :** `PUT`
- **URI :** `/api/utilisateurs/{idUtilisateur}/classes/{codeClasse}` (Ex: `/api/utilisateurs/1/classes/2`)
- **Contenu :** (Aucun corps de requête requis)

## 5. Calculer le nombre d'utilisateurs par niveau (II.e)
**Action :** Compter le nombre d'étudiants inscrits dans un niveau donné.
- **Méthode :** `GET`
- **URI :** `/api/utilisateurs/niveau/{nv}/count` (Ex: `/api/utilisateurs/niveau/DEUXIEME/count`)

## 6. Désaffecter un cours de sa classe (II.f)
**Action :** Retirer le lien entre un cours et sa classe (mise à `NULL` de la clé étrangère).
- **Méthode :** `PUT`
- **URI :** `/api/cours-classrooms/{idCours}/desaffecter` (Ex: `/api/cours-classrooms/1/desaffecter`)
- **Contenu :** (Aucun corps de requête requis)

## 7. Archiver périodiquement les cours (II.g - Bonus)
**Action :** Déclencher manuellement le processus d'archivage (également géré par le Scheduler toutes les 60s).
- **Méthode :** `POST`
- **URI :** `/api/cours-classrooms/archiver`
- **Contenu :** (Aucun corps de requête requis)

## 8. Somme des heures par spécialité et niveau (II.h)
**Action :** Calculer le volume horaire total pour une spécialité dans un niveau spécifique.
- **Méthode :** `GET`
- **URI :** `/api/cours-classrooms/heures?sp={sp}&nv={nv}`
- **Exemple :** `/api/cours-classrooms/heures?sp=INFORMATIQUE&nv=DEUXIEME`
