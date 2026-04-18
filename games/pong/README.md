# 🕹️ PONG — ARCADE

![JavaScript](https://img.shields.io/badge/JavaScript-ES2022-F7DF1E?style=flat-square&logo=javascript)
![Canvas](https://img.shields.io/badge/Canvas-2D-007ACC?style=flat-square)
![Webpack](https://img.shields.io/badge/Webpack-5-8DD6F9?style=flat-square&logo=webpack)

Jeu Pong 2 joueurs en pur JavaScript vanilla, thème **arcade pixel** avec effets néon et CRT.

```
██████╗  ██████╗ ███╗   ██╗ ██████╗
██╔══██╗██╔═══██╗████╗  ██║██╔════╝
██████╔╝██║   ██║██╔██╗ ██║██║  ███╗
██╔═══╝ ██║   ██║██║╚██╗██║██║   ██║
██║     ╚██████╔╝██║ ╚████║╚██████╔╝
╚═╝      ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝
```

---

## Gameplay

- **Premier à 7 points** remporte la partie
- La balle accélère de **8% à chaque rebond** sur une raquette (plafonnée à 14px/frame)
- Raquettes en néon cyan (P1) et rose (P2), balle en jaune pixel

### Contrôles

| Joueur | Monter | Descendre |
|--------|--------|-----------|
| **P1** (gauche, cyan) | `W` | `S` |
| **P2** (droite, rose) | `↑` | `↓` |

`START` — lancer / pause / rejouer

---

## Visuels

- Police **Press Start 2P** (Google Fonts) pour l'interface et le canvas
- Effets **néon** (glow CSS + shadowBlur Canvas) sur raquettes, balle, textes
- **Scanlines CRT** en overlay (repeating-linear-gradient)
- Animation **flicker** simulant un vieux moniteur arcade
- Texte **INSERT COIN** clignotant

---

## Architecture

```
src/
├── index.html
├── scripts/
│   ├── main.js                  # Point d'entrée
│   ├── ball.js                  # Mouvement, collision AABB
│   ├── obstacle.js              # Raquette de base
│   ├── obstL.js                 # P1 (W/S)
│   ├── obstR.js                 # P2 (↑/↓)
│   ├── keyManager.js            # État des touches
│   ├── animation.js             # Game loop de base
│   ├── animationWithObstacle.js # Score, game over, rendu néon
│   └── assets/style/
│       └── style-balles.css     # Thème arcade pixel
└── style/style-canvas.css
```

### Hiérarchie des classes

```
Animation
└── AnimationWithObstacle   (score, paddles néon, game over)

Obstacle
├── ObstacleL   (W / S)
└── ObstacleR   (↑ / ↓)
```

---

## Lancer le projet

```bash
cd games/pong
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
|---------|---------------|
| Game loop | `requestAnimationFrame` / `cancelAnimationFrame` |
| Collision | AABB (Axis-Aligned Bounding Box) |
| Score | `Ball.move()` retourne `'left'`\|`'right'`\|`true` |
| Vitesse | +8% par rebond, plafon 14px/frame |
| Touches | `KeyManager` découple clavier et rendu |
| OOP | Héritage ES6, champs privés `#method` |
| Style | CSS custom properties, animations `@keyframes` |

---

## Auteur

**Amine Chbari** — M1 Ingénierie Logiciel, ISCOD  
[aminechbaibar@gmail.com](mailto:aminechbaibar@gmail.com)
