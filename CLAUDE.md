# Portfolio Audit — Context for Claude Code

## 👤 Profil
- **Amine Chbari** — M1 Ingénierie Logiciel, ISCOD
- Objectif : réviser, corriger et améliorer tous les mini-projets du portfolio GitHub pour renforcer la recherche d'alternance
- Email : aminechbaibar@gmail.com

---

## ✅ Projets terminés

### 1. `others/vote` — Vote en temps réel (Node.js + Socket.IO)
**Tous les bugs fixés + redesign UI complet. Pushé sur GitHub.**

Corrections apportées :
- `server/app.js` : port hardcodé → `process.env.PORT || 8080`
- `server/controllers/io.controller.js` : listener `disconnect` dupliqué supprimé, `forEach` remplace la boucle `for`
- `server/controllers/requestController.js` : double `response.end()` supprimé, `this.request.url` → `this.#request.url`, BASE URL dynamique
- `server/package.json` : dépendance `express` inutilisée supprimée
- `client/scripts/admin.js` : ajout listener `END_VOTE`, fusion des listeners `BYE` dupliqués
- `client/scripts/voter.js` : sélecteur `button[data-vote]`, validation `VALID_VOTES`, `enableVoteButtons(false)` à l'init
- `client/voter.html` : ajout `type="button"` et `data-vote="pour/contre/nppv/abstention"`
- `client/admin.html` : `#admin` div caché au démarrage, bouton Start désactivé, CSS `button:disabled`
- Tous les fichiers `public/` (bundles + HTML) patchés en conséquence
- Design glassmorphism appliqué sur toutes les pages (index, about, voter, admin)
- `README.md` réécrit professionnellement avec badges, architecture ASCII, tableau tech stack

---

### 2. `games/pandemic game` — Jeu Pandemic (Java OOP)
**Tous les bugs fixés + README réécrit. Pushé sur GitHub.**

Corrections apportées :
- `City.java` : `infect()` — décrément `disease.nbOfCubes` uniquement quand un cube est posé (pas pendant un outbreak) ; ajout `hashCode()` basé sur `this.name`
- `Cure.java` : `doSomething()` — collecte les cartes matching, retire 5 cartes de la main ET les défausse avant d'appeler `cure()`
- `Board.java` : `init()` — `playerCards.remove(i)` → `playerCards.remove(0)` (évite le saut de cartes)
- `Board.java` : `setMapCities()` — `this.cities()` appelé une seule fois (cache du parsing JSON)
- `Board.java` : `setNeighborsCities()` — O(n³) → O(n) via `HashMap<String, City>`
- `Board.java` : paramètre `Filemane` → `filename` (typo)
- `Board.java` : renommages camelCase — `InitCityDiseases` → `initCityDiseases`, `InfectCity` → `infectCity`, `Reset_SameWave` → `resetSameWave`, `Reset_PreviousInfector` → `resetPreviousInfector`
- `Game.java` : `pls.get(0)` → `player` pour les cartes infection (bug : seul le joueur 1 était infecté)
- `Game.java` : champ `lc` rendu `private` + getter `getLc()` ajouté
- `Game.java` : `NbTour` → `nbTour` (convention camelCase)
- `Livrable4Main.java` : `System.exit(1)` → `System.exit(0)` pour la victoire
- `Player.java` : `Game.lc` → `Game.getLc()` (accès via getter)
- Tous les call sites mis à jour dans `Livrable1Main.java`, `InfectionCard.java`, `EpidemicCard.java`
- `README.md` réécrit avec badges, architecture, design patterns, commandes Windows + Linux

---

## 🔲 Projets restants à traiter

### 3. `others/boutique` — Boutique en ligne (React + Webpack)
- Auditer le code React (composants, state management, props)
- Vérifier la config Webpack/Babel
- Corriger les bugs, améliorer la qualité du code
- Réécrire le README

### 4. `others/motel reservation` — Réservation Motel (PHP)
- Auditer le code PHP (sécurité SQL injection, XSS, sessions)
- Corriger les bugs
- Améliorer la structure (séparation logique/affichage si pas fait)
- Réécrire le README

### 5. `games/pong` — Pong (JS + Canvas)
- Auditer le code (game loop, collision, rendu Canvas)
- Corriger les bugs
- Réécrire le README

### 6. `games/hit the target` — Hit the Target (JS + Canvas)
- Auditer le code
- Corriger les bugs
- Réécrire le README

---

## 🛠️ Conventions et approche

- **Approche** : lire les fichiers → identifier les bugs → appliquer les fixes → réécrire le README → push git
- **README style** : badges shields.io, architecture ASCII ou tableau, section "Getting Started", highlights techniques
- **Git** : commits conventionnels (`fix(project): description`), push depuis PowerShell Windows
- **Windows** : chemins avec espaces dans OneDrive — toujours utiliser des guillemets dans les commandes

### Commandes git utiles (PowerShell, depuis le dossier `github`)
```powershell
cd "C:\Users\User\OneDrive - LYCEE GASTON BERGER\Bureau\Amine_Univ\github"
git add "chemin/vers/fichier"
git commit -m "fix(projet): description"
git push
```

---

## 📌 Pour reprendre le travail

Lancer Claude Code depuis le dossier du repo :
```powershell
cd "C:\Users\User\OneDrive - LYCEE GASTON BERGER\Bureau\Amine_Univ\github"
claude
```

Puis dire : **"Continue l'audit du portfolio, prochain projet : boutique"**
