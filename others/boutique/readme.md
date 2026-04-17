# Boutique Peluches

![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=white)
![Webpack](https://img.shields.io/badge/Webpack-5-8DD6F9?logo=webpack&logoColor=black)
![Babel](https://img.shields.io/badge/Babel-7-F9DC3E?logo=babel&logoColor=black)
![License](https://img.shields.io/badge/License-ISC-green)

Application e-commerce de peluches et figurines collectibles développée en React. Elle permet de parcourir un catalogue de produits, de filtrer par nom, d'ajouter au panier et de gérer les quantités en temps réel.

---

## Fonctionnalités

- Catalogue de 16 peluches avec image, description, poids et prix
- Filtre de recherche en temps réel (insensible à la casse)
- Ajout au panier / suppression avec restauration du stock
- Sélection de quantité dans le panier via menu déroulant
- Calcul automatique du prix total et du poids total
- Synchronisation bidirectionnelle panier ↔ liste de produits

---

## Architecture

```
boutique/
├── src/
│   ├── components/
│   │   ├── app.jsx             # Composant racine — gestion état global
│   │   ├── productList.jsx     # Liste produits + filtre + logique stock
│   │   ├── forSaleProduct.jsx  # Carte produit individuelle
│   │   ├── shoppingCart.jsx    # Panier — totaux et gestion articles
│   │   ├── productInCart.jsx   # Ligne produit dans le panier
│   │   └── filter.jsx          # Input de filtrage
│   ├── data/
│   │   └── products.js         # Catalogue statique (16 produits)
│   ├── assets/
│   │   ├── style/              # CSS par composant
│   │   └── images/             # Icônes panier / poubelle
│   ├── scripts/
│   │   └── main.js             # Point d'entrée React
│   └── index.html              # Template HTML
├── webpack.config.js
└── package.json
```

### Flux de données

```
App (state: sProduct, dProduct, qtyChanged)
 ├── ProductList
 │    ├── Filter ──────────── filterText
 │    └── ForSaleProduct ──── addToCart → App.productToAdd
 └── ShoppingCart
      └── ProductInCart ───── updateQte → App.changeQty
                              DeleteFromCart → App.productToDelete
```

---

## Stack technique

| Technologie | Rôle |
|---|---|
| React 18 | UI — composants de classe |
| Webpack 5 | Bundling (JS + CSS + images) |
| Babel 7 | Transpilation ES6+/JSX |
| MiniCssExtractPlugin | Extraction CSS en fichiers séparés |
| CopyWebpackPlugin | Copie des assets statiques |
| webpack-dev-server | Serveur de développement HMR |

---

## Getting Started

### Prérequis

- Node.js >= 16
- npm >= 8

### Installation

```bash
git clone https://github.com/<username>/github.git
cd "others/boutique"
npm install
```

### Développement

```bash
npm run dev-server
# Ouvre automatiquement http://localhost:3000
```

### Build de production

```bash
# Linux / macOS
NODE_ENV=production npm run build

# Windows PowerShell
$env:NODE_ENV="production"; npm run build
```

Les fichiers sont générés dans `dist/`.

---

## Corrections apportées (audit)

- `productList.jsx` : suppression des mutations directes (`item.added = true/false`) avant spread — remplacé par `{...item, added: true/false}` pour respecter l'immutabilité React
- `shoppingCart.jsx` : `DeleteFromCart` et `productAdded` utilisent désormais `setState(prevState => ...)` pour éviter les lectures d'état périmé lors de mises à jour rapides
- `productList.jsx` : suppression du `.bind(this)` superflu dans le JSX pour `filterChanged` (déjà bindé dans le constructeur)
- `forSaleProduct.jsx` : suppression du constructeur vide inutile
- `main.js` : suppression du `console.log` de debug, code commenté nettoyé, ajout de `React.StrictMode`
- `index.html` : suppression des scripts CDN React redondants (causaient un double chargement React avec le bundle Webpack), ajout `lang`, `charset`, `viewport`, `meta description`
- `webpack.config.js` : `PRODUCTION` dynamique via `process.env.NODE_ENV` (était hardcodé à `false`), `devtool` changé de `inline-source-map` à `eval-source-map` (builds dev ~3x plus rapides), suppression du `TerserPlugin` manuel (Webpack 5 le gère nativement en production), configuration `devServer` complétée
- `package.json` : nom, description et auteur corrigés (valeurs placeholder)
- `products.js` : correction des typos (`pnada` → `panda`, `migon` → `mignon`)
