# 🦠 Pandemic Game — Java Simulation

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=java&logoColor=white)
![Design Patterns](https://img.shields.io/badge/Design-OOP%20%2B%20Design%20Patterns-4CAF50?style=flat)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen?style=flat)

A fully object-oriented implementation of the **Pandemic** board game in Java, built as a collaborative software engineering project at Université de Lille. Players cooperate to cure four diseases spreading across the globe before outbreaks spiral out of control.

---

## 🎮 Gameplay Overview

- 2 to 4 players collaborate on a shared board of interconnected cities
- Each turn: play 4 actions → draw 2 player cards → draw infection cards
- **Win**: cure all 4 diseases before the game ends
- **Lose**: 8+ outbreaks, any disease runs out of cubes, or the player deck is exhausted

---

## 🏗️ Architecture

```
src/
├── main/
│   ├── Livrable1Main.java      # Board + infection demo
│   ├── Livrable2Main.java      # Cards + deck demo
│   ├── Livrable3Main.java      # Actions + roles demo
│   └── Livrable4Main.java      # Full game ← main entry point
│
└── pandemic/
    ├── board/
    │   ├── Board.java           # Game state, city graph, deck management
    │   └── Deck.java            # Generic deck (draw, discard, shuffle)
    │
    ├── city/
    │   ├── City.java            # City node: infection & outbreak propagation
    │   └── Card/
    │       ├── Card.java            # Abstract card base class
    │       ├── PlayerCard.java      # City card in player hand
    │       ├── InfectionCard.java   # Infects a city when drawn
    │       └── EpidemicCard.java    # Triggers epidemic event
    │
    ├── disease/
    │   └── Disease.java         # Enum: JAUNE/ROUGE/BLEU/NOIR with cure/eradicate state
    │
    ├── player/
    │   ├── Player.java          # Abstract player (Template Method pattern)
    │   ├── Globetrotter.java    # Role: extended movement
    │   ├── Medecine.java        # Role: treats all cubes at once
    │   ├── Scientist.java       # Role: cures with 4 cards instead of 5
    │   ├── Expert.java          # Role: builds labs for free
    │   └── Action/
    │       ├── Action.java          # Strategy interface
    │       ├── Move.java            # Move to adjacent city
    │       ├── Treat.java           # Remove a disease cube
    │       ├── Cure.java            # Cure a disease (5 matching cards)
    │       ├── Construct.java       # Build a research station
    │       └── NoneAction.java      # Skip action (0 action points)
    │
    ├── game/
    │   ├── Game.java            # Main game loop
    │   └── listchooser/         # Strategy: random AI or interactive input
    │
    └── util/
        ├── GameOverException.java
        ├── GameWinException.java
        └── Color.java           # ANSI terminal colors
```

---

## 🎭 Design Patterns

| Pattern | Location | Purpose |
|---|---|---|
| **Strategy** | `ListChooser` interface | Swap between random AI and interactive player input at runtime |
| **Template Method** | `Player` abstract class | Shared turn structure with role-specific action overrides |
| **Command** | `Action` interface | Encapsulate each game action as a first-class object |
| **Composite** | `City` graph + `infect()` | Recursive outbreak propagation through the city network |

---

## 👥 Player Roles

| Role | Special Ability |
|---|---|
| `Globetrotter` | Move to any city on the board in one action |
| `Medecine` | Remove all cubes of a disease in one action |
| `Scientist` | Cure a disease using only 4 matching cards |
| `Expert` | Build a research station without discarding the city card |

---

## 🚀 Getting Started

### Prerequisites

- Java 11+
- JSON library (`jars/json-20220924.jar` included)

### Compile

**Linux / macOS:**
```bash
find src/pandemic -name "*.java" > src.txt
javac -cp "src:jars/json-20220924.jar" -d classes @src.txt src/main/Livrable4Main.java
rm src.txt
```

**Windows (PowerShell):**
```powershell
Get-ChildItem -Path "src\pandemic" -Filter "*.java" -Recurse | ForEach-Object { $_.FullName.Replace($PWD.Path + "\", "") } | Out-File src.txt -Encoding ascii
javac -cp "src;jars/json-20220924.jar" -d classes `@src.txt src/main/Livrable4Main.java
Remove-Item src.txt
```

### Run (without building a jar)

```bash
# Linux / macOS — default: 4 players, Maps/MapInit.json
java -cp "classes:jars/json-20220924.jar" main.Livrable4Main

# Windows PowerShell
java -cp "classes;jars/json-20220924.jar" main.Livrable4Main

# Custom map + player count (2 to 4 players)
java -cp "classes;jars/json-20220924.jar" main.Livrable4Main Maps\MapInit.json 3
```

### Build & run the jar

```bash
# Linux / macOS
jar cvfm jeu.jar manifest.txt -C classes .
mv jeu.jar jars/
java -jar jars/jeu.jar
```

```powershell
# Windows PowerShell
jar cvfm jeu.jar manifest.txt -C classes .
Move-Item jeu.jar .\jars\ -Force
java -jar jars\jeu.jar
```

### Tests

```bash
# Linux / macOS
find ./test -name "*.java" > test.txt && javac -cp test4poo.jar @test.txt && rm test.txt
java -jar test4poo.jar pandemic.disease.DiseaseTest
```

---

## 🗺️ Map Format (JSON)

The board is loaded from a JSON file defining cities and their connections:

```json
{
  "cities": {
    "Paris":    2,
    "London":   2,
    "Cairo":    0,
    "Beijing":  3
  },
  "neighbors": {
    "Paris":  ["London", "Madrid"],
    "London": ["Paris", "New York"],
    "Cairo":  ["Istanbul", "Baghdad"]
  }
}
```

Disease color codes: `0` = JAUNE, `1` = ROUGE, `2` = BLEU, `3` = NOIR

---

## 🔬 Key Technical Highlights

**Outbreak Propagation** — `City.infect()` uses `sameWave` and `previousInfector` flags to prevent infinite loops in circular city graphs. When a city already has 3 cubes, the infection cascades recursively to all neighbors without bouncing back.

**Card Distribution** — `Board.init()` always removes from index `0` of the shuffled deck, avoiding the classic index-shift bug that would cause cards to be skipped during initial deal.

**Graph Setup Performance** — `setNeighborsCities()` builds a `HashMap<String, City>` once for O(1) city lookups, reducing board initialization from O(n³) to O(n).

---

## 👨‍💻 Team

| Name | Role |
|---|---|
| **Amine Chbari** | Developer |
| Yassin Amazrayin | Developer |
| Fabio Vandewaeter | Developer |
| Glyn Iganze | Developer |

Project built at **Université de Lille** — L2 S4 Software Project (2023).
