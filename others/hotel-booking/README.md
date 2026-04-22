# South Motel & Spa — Réservation en ligne

![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=flat-square&logo=angular)
![PHP](https://img.shields.io/badge/PHP-8+-777BB4?style=flat-square&logo=php)
![SCSS](https://img.shields.io/badge/SCSS-Design-CC6699?style=flat-square&logo=sass)
![REST API](https://img.shields.io/badge/API-REST-009688?style=flat-square)

Application web complète de réservation hôtelière avec un front Angular luxueux et un back-end PHP REST API.

---

## Architecture

```
motel reservation/
├── south-motel/              # Front-end Angular
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/
│   │   │   │   ├── home/         # Page d'accueil (hero, chambres, amenities)
│   │   │   │   ├── rooms/        # Catalogue des chambres
│   │   │   │   ├── book/         # Formulaire de réservation (stepper 3 étapes)
│   │   │   │   └── reservations/ # CRUD réservations + stats
│   │   │   ├── core/
│   │   │   │   ├── models/       # Interfaces TypeScript
│   │   │   │   ├── services/     # ReservationService (HttpClient)
│   │   │   │   └── data/         # Données statiques des chambres
│   │   │   ├── app.ts            # Root component (navbar + router)
│   │   │   └── app-routing-module.ts
│   │   └── styles.scss           # Design system (CSS variables, utilitaires)
│   └── proxy.conf.json           # Proxy /api → localhost:8080
└── api/
    ├── index.php                 # REST API (GET/POST/PUT/DELETE)
    └── data.json                 # Stockage JSON (base de données fichier)
```

---

## Stack technique

| Couche | Technologie | Rôle |
|--------|-------------|------|
| Front-end | Angular 21 (NgModule) | SPA avec routing, reactive forms |
| Style | SCSS + CSS variables | Design system luxe cohérent |
| HTTP | Angular HttpClient | Communication avec l'API |
| Back-end | Node.js + Express | REST API |
| Stockage | JSON file | Persistance légère |
| Fonts | Cormorant Garamond + Inter | Typographie hôtelière |

---

## Fonctionnalités

- **Accueil** : Hero plein écran, section À propos, chambres en avant, services, bannière CTA
- **Chambres** : 4 types (Standard, Familiale, Deluxe, Suite Prestige) avec images, équipements, prix
- **Réservation** : Stepper 3 étapes (choix chambre → formulaire → confirmation)
- **Mes réservations** : Liste avec filtres par type, édition inline, suppression avec modal, tableau de statistiques
- **Navbar transparente** sur la home, opaque sur les autres pages, sticky au scroll
- **Responsive** : adaptatif mobile avec menu hamburger

---

## Lancer le projet

### Prérequis
- Node.js 18+ et npm
- Angular CLI : `npm install -g @angular/cli`

### 1. Démarrer l'API Node.js

```bash
cd "motel reservation/api"
npm install
npm start
```

> L'API tourne sur `http://localhost:8080`. Le proxy Angular redirige automatiquement `/api` → `http://localhost:8080/`.

### 2. Démarrer Angular (dans un second terminal)

```bash
cd "motel reservation/south-motel"
npm install
ng serve
```

Accéder à [http://localhost:4200](http://localhost:4200)

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
  "chambre": "Chambre Familiale",
  "dateArrivee": "2025-06-15"
}
```

**Validation serveur** : `taille` 1–4, `sejour` 1–10, `nom` ≥ 2 chars, `commentaire` ≤ 120 chars.  
**Sécurité** : `htmlspecialchars()` + suppression du délimiteur `|` sur toutes les entrées.

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
