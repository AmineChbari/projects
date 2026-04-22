# South Motel & Spa — Réservation en ligne

![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=flat-square&logo=angular)
![Node.js](https://img.shields.io/badge/Node.js-20-339933?style=flat-square&logo=nodedotjs)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker)
![SCSS](https://img.shields.io/badge/SCSS-Design-CC6699?style=flat-square&logo=sass)
![REST API](https://img.shields.io/badge/API-REST-009688?style=flat-square)

Application web complète de réservation hôtelière — front Angular 21 + API Express containerisés via Docker.

---

## Architecture

```
hotel-booking/
├── south-motel/              # Front-end Angular 21
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/
│   │   │   │   ├── home/         # Page d'accueil (hero, chambres, services)
│   │   │   │   ├── rooms/        # Catalogue des chambres
│   │   │   │   ├── book/         # Formulaire de réservation (stepper 3 étapes)
│   │   │   │   └── reservations/ # CRUD réservations + stats
│   │   │   ├── core/
│   │   │   │   ├── models/       # Interfaces TypeScript
│   │   │   │   ├── services/     # ReservationService (HttpClient)
│   │   │   │   └── data/         # Données statiques des chambres
│   │   │   └── app.ts            # Root component (navbar + router)
│   │   └── styles.scss           # Design system (CSS variables)
│   ├── nginx.conf                # Reverse proxy /api → service api:8080
│   ├── proxy.conf.json           # Proxy dev server /api → localhost:8080
│   └── Dockerfile                # Build Angular + serve via nginx
├── api/
│   ├── server.js                 # REST API Express (GET/POST/PUT/DELETE)
│   ├── data.json                 # Stockage JSON
│   └── Dockerfile                # Image Node.js 20 Alpine
└── docker-compose.yml            # Orchestration multi-conteneurs
```

---

## Stack technique

| Couche | Technologie | Rôle |
|--------|-------------|------|
| Front-end | Angular 21 (NgModule) | SPA avec routing, reactive forms |
| Style | SCSS + CSS variables | Design system luxe cohérent |
| HTTP | Angular HttpClient | Communication avec l'API |
| Back-end | Node.js + Express | REST API |
| Reverse proxy | nginx | Sert le front + proxy vers l'API |
| Stockage | JSON file | Persistance légère |
| Déploiement | Docker + Docker Compose | Containerisation multi-services |
| Fonts | Cormorant Garamond + Inter | Typographie hôtelière |

---

## Fonctionnalités

- **Accueil** : Hero plein écran, section À propos, chambres en avant, services, bannière CTA
- **Chambres** : 4 types (Standard, Double, Deluxe, Suite Prestige) avec images, équipements, prix
- **Réservation** : Stepper 3 étapes (choix chambre → formulaire → confirmation)
- **Mes réservations** : Liste avec filtres par type, édition inline, suppression, tableau de statistiques
- **Navbar transparente** sur la home, opaque sur les autres pages, sticky au scroll
- **Responsive** : adaptatif mobile avec menu hamburger

---

## Lancer le projet

### Méthode recommandée — Docker (un seul terminal)

**Prérequis** : [Docker Desktop](https://www.docker.com/products/docker-desktop/)

```bash
cd hotel-booking
docker compose up --build
```

Accéder à [http://localhost:4200](http://localhost:4200)

> Le build Angular est effectué dans le container (~2 min au premier lancement). Les fois suivantes, le cache Docker accélère le démarrage.

Pour arrêter :
```bash
docker compose down
```

---

### Méthode alternative — Sans Docker (2 terminaux)

**Prérequis** : Node.js 18+

**Terminal 1 — API :**
```bash
cd hotel-booking/api
npm install
node server.js
```

**Terminal 2 — Angular :**
```bash
cd hotel-booking/south-motel
npm install
npx ng serve
```

Accéder à [http://localhost:4200](http://localhost:4200)

> Le proxy Angular redirige automatiquement `/api` → `http://localhost:8080`.

---

## API REST

| Méthode | URL | Description |
|---------|-----|-------------|
| `GET` | `/api` | Liste toutes les réservations |
| `POST` | `/api` | Crée une réservation |
| `PUT` | `/api?id=1` | Modifie la réservation #1 |
| `DELETE` | `/api?id=1` | Supprime la réservation #1 |

### Modèle de réservation

```json
{
  "id": 1,
  "nom": "Jean Dupont",
  "taille": 2,
  "sejour": 3,
  "commentaire": "Chambre haute souhaitée",
  "chambre": "Chambre Double",
  "dateArrivee": "2025-06-15"
}
```

**Validation serveur** : `taille` 1–4, `sejour` 1–10, `nom` ≥ 2 chars, `commentaire` ≤ 120 chars.

---

## Design system

Les couleurs et tokens sont définis dans `src/styles.scss` :

```scss
--navy:      #0D1B2A   // Fond principal foncé
--gold:      #C9A84C   // Accent doré
--cream:     #F7F3ED   // Fond secondaire chaud
--text:      #1A1A2E   // Texte principal
```

Typographie :
- **Cormorant Garamond** (serif) — titres, prix, logo
- **Inter** (sans-serif) — corps de texte, navigation

---

## Auteur

**Amine Chbari** — M1 Ingénierie Logiciel, ISCOD  
[aminechbaibar@gmail.com](mailto:aminechbaibar@gmail.com)
