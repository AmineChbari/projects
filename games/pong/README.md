# PONG — Jeu 2 Joueurs

![JavaScript](https://img.shields.io/badge/JavaScript-ES2022-F7DF1E?style=flat-square&logo=javascript)
![Canvas](https://img.shields.io/badge/Canvas-2D-007ACC?style=flat-square)
![Webpack](https://img.shields.io/badge/Webpack-5-8DD6F9?style=flat-square&logo=webpack)

Jeu Pong classique 2 joueurs développé en JavaScript vanilla avec Canvas API et architecture orientée objet.

---

## Gameplay

- **Premier à 7 points** remporte la partie
- La balle accélère légèrement à chaque rebond sur une raquette
- La vitesse est plafonnée pour garder le jeu jouable

### Contrôles

| Joueur | Monter | Descendre |
|--------|--------|-----------|
| Joueur 1 (gauche) | `W` | `S` |
| Joueur 2 (droite) | `↑` | `↓` |

**Start / Pause / Rejouer** : bouton START en bas

---

## Architecture

```
src/
├── index.html
├── scripts/
│   ├── main.js                  # Point d'entrée, initialisation
│   ├── ball.js                  # Classe Ball — mouvement, collision, rendu
│   ├── obstacle.js              # Classe Obstacle — raquette de base
│   ├── obstL.js                 # Joueur 1 (touches W/S)
│   ├── obstR.js                 # Joueur 2 (flèches)
│   ├── keyManager.js            # État des touches pressées
│   ├── animation.js             # Classe Animation — game loop de base
│   ├── animationWithObstacle.js # Jeu complet — score, game over
│   └── assets/
│       ├── images/ball.png
│       └── style/style-balles.css
└── style/
    └── style-canvas.css
```

### Hiérarchie des classes

```
Animation
└── AnimationWithObstacle   (score, game over, paddles)

Obstacle
├── ObstacleL   (contrôles gauche : W/S)
└── ObstacleR   (contrôles droite : ↑/↓)
```

---

## Lancer le projet

```bash
cd games/pong
npm install
npm run dev-server
```

Ouvre automatiquement [http://localhost:9000](http://localhost:9000)

### Build de production

```bash
NODE_ENV=production npm run build
```

---

## Points techniques

- **Game loop** : `requestAnimationFrame` avec `cancelAnimationFrame` pour pause/reprise
- **Collision AABB** : détection via intersection de rectangles (Axis-Aligned Bounding Box)
- **Score** : `Ball.move()` retourne `'left'`/`'right'`/`true` pour identifier quel camp marque
- **Vitesse** : augmentation de 8% par rebond, plafonnée à 14px/frame
- **État touches** : `KeyManager` découple la détection clavier du rendu
- **OOP** : héritage ES6, champs privés (`#method`)

---

## Auteur

**Amine Chbari** — M1 Ingénierie Logiciel, ISCOD  
[aminechbaibar@gmail.com](mailto:aminechbaibar@gmail.com)
