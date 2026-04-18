# 🎯 HIT THE TARGET — ARCADE

![JavaScript](https://img.shields.io/badge/JavaScript-ES2022-F7DF1E?style=flat-square&logo=javascript)
![Canvas](https://img.shields.io/badge/Canvas-2D-007ACC?style=flat-square)
![Webpack](https://img.shields.io/badge/Webpack-5-8DD6F9?style=flat-square&logo=webpack)

Jeu de tir à l'arc en JavaScript vanilla, thème **arcade pixel** avec effets néon et CRT.

```
██╗  ██╗██╗████████╗    ████████╗██╗  ██╗███████╗
██║  ██║██║╚══██╔══╝    ╚══██╔══╝██║  ██║██╔════╝
███████║██║   ██║           ██║   ███████║█████╗
██╔══██║██║   ██║           ██║   ██╔══██║██╔══╝
██║  ██║██║   ██║           ██║   ██║  ██║███████╗
╚═╝  ╚═╝╚═╝   ╚═╝           ╚═╝   ╚═╝  ╚═╝╚══════╝
████████╗ █████╗ ██████╗  ██████╗ ███████╗████████╗
╚══██╔══╝██╔══██╗██╔══██╗██╔════╝ ██╔════╝╚══██╔══╝
   ██║   ███████║██████╔╝██║  ███╗█████╗     ██║
   ██║   ██╔══██║██╔══██╗██║   ██║██╔══╝     ██║
   ██║   ██║  ██║██║  ██║╚██████╔╝███████╗   ██║
   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝
```

---

## Gameplay

- Déplacez l'archer et tirez sur les **cibles** pour marquer des points
- Chaque cible rapporte **10 à 50 pts** selon la précision (zone centrale = 50 pts)
- Les **oiseaux voleurs** volent vos flèches et vous font perdre une vie au contact
- Ramassez les **paquets de flèches** (3 flèches bonus) qui tombent du ciel
- Partie terminée quand vous n'avez **plus de vies** ou **plus de flèches**

### Système de points

| Zone | Points |
|------|--------|
| Centre (bullseye) | **50** |
| Anneau intérieur  | **30** |
| Anneau milieu     | **20** |
| Bord              | **10** |

### Contrôles

| Action | Touche |
|--------|--------|
| Déplacer gauche  | `←` |
| Déplacer droite  | `→` |
| Tirer une flèche | `ESPACE` ou `↑` ou **clic** |
| Start / Pause    | Bouton **START** |

---

## Visuels

- Police **Press Start 2P** (Google Fonts) pour l'interface et le canvas
- Ciel nocturne avec **dégradé étoilé** (55 étoiles générées aléatoirement)
- Effets **néon** (shadowBlur Canvas) sur flèches, archer, flash de score
- **Flash messages** flottants (+50, VOLEE!, -1 VIE…) avec fade-out
- **Scanlines CRT** en overlay + animation **flicker** simulant un vieux moniteur

---

## Architecture

```
src/
├── index.html
├── scripts/
│   ├── main.js      # Point d'entrée
│   └── game.js      # Toute la logique : classes + Game
└── style/
    └── style.css    # Thème arcade pixel
```

### Classes

```
Arrow        — flèche tirée par l'archer (monte à vy=-11, AABB rect)
Target       — cible mobile qui rebondit sur les murs, scoreAt() par distance
Bird         — oiseau traversant la scène, vole les flèches, prend une vie
ArrowBundle  — paquet de flèches tombant du ciel (+3 flèches)
Flash        — message flottant avec fade-out (score, événements)
Game         — orchestrateur principal (champs privés #field)
```

---

## Lancer le projet

```bash
cd "games/hit the target"
npm install
npm run dev-server
# → http://localhost:9000
```

### Build de production

```bash
NODE_ENV=production npm run build
```

---

## Points techniques

| Concept | Implémentation |
|---------|----------------|
| Game loop | `requestAnimationFrame` / `cancelAnimationFrame` |
| Collision | AABB (overlap) + distance euclidienne (cible) |
| Score zone | `Math.hypot()` → 4 anneaux (50/30/20/10) |
| Spawn | Compteurs `#tTarget`, `#tBird`, `#tBundle` par frame |
| Rendu | Sky gradient, étoiles, glow Canvas, flash messages |
| OOP | Champs privés ES2022 (`#field`, `#method()`) |
| Style | Press Start 2P, CSS variables neon, scanlines CRT |

---

## Auteur

**Amine Chbari** — M1 Ingénierie Logiciel, ISCOD  
[aminechbaibar@gmail.com](mailto:aminechbaibar@gmail.com)
