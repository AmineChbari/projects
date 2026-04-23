# Gestion Étudiants et Groupes

Une application de gestion des étudiants en ligne construite avec Node.js, Express.js, MongoDB et Webpack.

---

## Structure du Projet

```
gestionEtudiants/
├── client/                 # Application frontend
│   ├── scripts/            # JavaScript côté client
│   ├── style/              # Fichiers CSS
│   ├── images/             # Ressources images
│   └── webpack.config.js   # Configuration Webpack
├── server/                 # Application backend
│    ├── config/            # Configuration de la base de données
│    ├── controllers/       # Contrôleurs serveur
│    ├── models/            # Modèles Mongoose
│    ├── routes/            # Routes Express
│    ├── views/             # Templates Pug
│    ├── misc/              # Fichiers de données
│    └── public/            # Fichiers client compilés
└── readme.md               # Description du projet
```

---

## Lancer le Projet

### **1. Lancer MongoDB Database**

#### **Démarrer MongoDB**
```bash
mongod
```
- Le serveur MongoDB démarre sur le port par défaut (`27017`).

#### **Importer les données initiales**
```bash
cd server
npm run import-students-data
```
- Cette commande importe les données initiales dans la base de données `gestionEtudiants` et la collection `students`.

---

### **2. Côté Client**

#### **Installer les dépendances pour le client et le construire pour la production**
```bash
cd client
npm install
npm run build
```
- Cette commande utilise Webpack pour compiler les fichiers JavaScript, CSS et images dans le dossier `server/public`.

- EN MODE DEVELOPEMENT:
#### **Démarrer le client en mode développement avec rechargement automatique**
```bash
npm run watch
```

#### **Nettoyer les fichiers générés**
```bash
cd client
npm run clean
```
---

### **3. Démarrer le Serveur Express**

#### **Installer les dépendances pour le serveur et le démarrer en production**
```bash
cd server
npm install
npm start
```

- EN MODE DEVELOPEMENT:
#### **Démarrer le serveur en mode développement avec rechargement automatique**
```bash
cd server
npm run dev
```

---

### **4. Utilisation**

Après avoir démarré le client et le serveur :

1. Ouvrir `http://localhost:3000/` pour accéder à la page d'accueil.
2. Ouvrir `http://localhost:3000/students` pour gérer les étudiants.
3. Ouvrir `http://localhost:3000/groups` pour gérer les groupes.

- Pour obtenir les données au format JSON via les routes qui renvoient normalement des pages HTML, ajoutez **?json=true** à l'URL.
    - Exemple: **GET /students?json=true** renvoie les données brutes au format JSON au lieu de la page HTML.

---

## API REST

L'application expose les API REST suivantes:

### API Étudiants

| Méthode | URL                | Description                                         | Corps de la Requête                                                        | Réponse                                                 |
|---------|--------------------|----------------------------------------------------|---------------------------------------------------------------------------|--------------------------------------------------------|
| GET     | `/students`        | Récupère tous les étudiants                         | -                                                                         | Liste d'objets étudiant avec leur groupe si assigné     |
| POST    | `/students`        | Crée un nouvel étudiant                             | `{studentNumber, name, surnames}`                                          | `{message: "Étudiant créé avec succès"}`                |
| PUT     | `/students/:id`    | Met à jour un étudiant existant                     | `{studentNumber, name, surnames}`                                          | `{message: "Étudiant mis à jour avec succès"}`          |
| DELETE  | `/students/:id`    | Supprime un étudiant                                | -                                                                         | `{message: "Étudiant supprimé avec succès"}`            |

### API Groupes

| Méthode | URL                  | Description                                          | Corps de la Requête                         | Réponse                                                      |
|---------|----------------------|------------------------------------------------------|----------------------------------------------|-------------------------------------------------------------|
| GET     | `/groups`            | Récupère tous les étudiants avec leurs groupes       | -                                            | Liste des groupes avec leurs étudiants associés             |
| POST    | `/groups/assign`     | Assigne un étudiant à un groupe                      | `{studentId, groupNumber}`                    | `{message: "Student assigned to group successfully"}`        |
| POST    | `/groups/auto-assign`| Distribue automatiquement les étudiants non assignés | -                                            | `{message: "X étudiant(s) ont été distribués..."}`          |
| DELETE  | `/groups/:id`        | Retire un étudiant d'un groupe                       | -                                            | `{message: "Student removed from group successfully"}`       |
| DELETE  | `/groups/empty-all`  | Vide tous les groupes                                | -                                            | `{message: "Tous les groupes ont été vidés..."}`            |

### Format des données

#### Objet Étudiant
```json
{
  "_id": "string", 
  "studentNumber": "string", // flexible par rapport au type (peut contenir des chiffres et des caractères) ex: "S001"
  "name": "string", // en majuscules
  "surnames": ["string"], // tableau de prénoms
  "groupNumber": number // facultatif, présent uniquement si l'étudiant est assigné à un groupe
}
```

#### Objet Groupe
```json
{
  "_id": "string",
  "studentId": {
    "_id": "string",
    "studentNumber": "string",
    "name": "string",
    "surnames": "string"
  },
  "groupNumber": number // entre 1 et 6
}
```

---

## Commandes Résumées

| Commande                  | Description                                                                 |
|---------------------------|-----------------------------------------------------------------------------|
| `mongod`                  | Démarre le serveur MongoDB.                                                |
| `cd server && npm run import-data` | Importe les données initiales dans MongoDB.                                |
| `cd server && npm install` | Installe les dépendances pour le serveur.                                  |
| `cd client && npm install` | Installe les dépendances pour le client.                                   |
| `cd client && npm run build` | Compile les fichiers client pour la production.                           |
| `cd client && npm run watch` | Démarre le client en mode développement avec rechargement automatique.     |
| `cd client && npm run clean` | Supprime les fichiers générés dans le dossier `server/public`.             |
| `cd server && npm start`   | Démarre le serveur Express en mode production.                            |
| `cd server && npm run dev` | Démarre le serveur Express en mode développement avec rechargement.        |

---

## Auteur

Amine Chbari