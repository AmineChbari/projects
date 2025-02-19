# Système de Vote en Ligne

Une application de vote en ligne en temps réel construite avec Node.js, Socket.IO et Webpack.

## Structure du Projet

```
vote/
├── client/                 # Application frontend
│   ├── scripts/           # JavaScript côté client
│   ├── style/            # Fichiers CSS
│   ├── images/           # Ressources images
│   └── html/             # Templates HTML
└── server/               # Application backend
    ├── controllers/      # Contrôleurs serveur
    └── public/          # Fichiers client compilés
```

## Prérequis

- Node.js (version 14 ou supérieure)
- npm (Gestionnaire de paquets Node)

## Installation

1. Cloner le dépôt :
```bash
git clone <url-du-depot>
cd vote
```

2. Installer les Dépendances Client :
```bash
cd client
npm install
```

3. Installer les Dépendances Serveur :
```bash
cd server
npm install
```

## Développement

### Construction du Client

```bash
cd client
npm run build     # Pour la construction en production
npm run watch     # Pour le développement avec rechargement automatique
```

### Démarrage du Serveur

```bash
cd server
npm run start    # Pour la production
npm run dev      # Pour le développement avec rechargement automatique
```

## Utilisation

Après avoir démarré le client et le serveur :

1. Ouvrir http://localhost:8080/ pour l'acceuil  
2. Ouvrir http://localhost:8080/admin-vote pour l'interface administrateur
3. Ouvrir http://localhost:8080/votant pour l'interface votant

## Fonctionnalités

- Mises à jour des votes en temps réel avec Socket.IO
- Interfaces séparées pour les administrateurs et les votants
- Comptage sécurisé des votes et affichage des résultats
- Design responsive pour mobile et ordinateur

## Technologies Utilisées

- Frontend :
  - Webpack
  - Babel
  - React
  - Socket.IO Client

- Backend :
  - Node.js
  - Socket.IO
  - Serveur HTTP

## Auteur

Amine Chbari

