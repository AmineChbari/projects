# 🚲 V'Lille — Réseau de location de véhicules

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit-5.10-25A162?style=flat-square&logo=junit5&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Coverage-yellow?style=flat-square)
![Design Patterns](https://img.shields.io/badge/Design%20Patterns-6-6C63FF?style=flat-square)
![OOP](https://img.shields.io/badge/OOP-Advanced-blue?style=flat-square)

Simulation d'un réseau de location de vélos et scooters en Java — projet académique L3 Informatique, Université de Lille. Implémente 6 design patterns OOP avancés dans un système de gestion de stations avec centre de contrôle, redistribution automatique, détection de vols et réparations.

---

## 📋 Table des matières

- [Architecture](#-architecture)
- [Design Patterns](#-design-patterns)
- [Fonctionnalités](#-fonctionnalités)
- [Scénarios](#-scénarios)
- [Lancer le projet](#-lancer-le-projet)
- [Tests & Couverture](#-tests--couverture)
- [Structure du projet](#-structure-du-projet)
- [Auteurs](#-auteurs)

---

## 🏗️ Architecture

Le système est organisé autour de quatre domaines :

| Package | Rôle |
|---------|------|
| `controlCenter` | Centre de contrôle — orchestre les stations, pilote la redistribution |
| `station` | Stations physiques — stockage des véhicules, notifications |
| `vehicle` | Véhicules — vélos classiques, électriques, scooters, décorateurs |
| `staff` | Réparateurs — visite et répare les véhicules hors service |
| `timer` | Gestionnaire de temps simulé pour les scénarios |
| `ui` | Interface graphique Swing interactive |

### Cycle de vie d'un véhicule

```
  [Créé]
    │
    ▼
[Available] ──take()──▶ [InUse] ──put()──▶ [Available]
    │                                            │
    │                              (nbUsed > 3) │
    │                                            ▼
    │                                        [Broken] ──repair()──▶ [Available]
    │
    └──(seul en station > 6s)──▶ [Stolen]
```

---

## 🎯 Design Patterns

| Pattern | Classe(s) | Rôle dans le système |
|---------|-----------|----------------------|
| **Observer** | `ControlCenter`, `Station` | La station notifie le centre de contrôle à chaque prise/retour de véhicule |
| **Strategy** | `RoundRobinStrategy`, `RandomStrategy` | Algorithme de redistribution des véhicules interchangeable à chaud |
| **State** | `Available`, `InUse`, `Broken`, `Stolen` | Chaque état encapsule son propre comportement et ses transitions |
| **Decorator** | `BasketDecorator`, `LuggageRackDecorator` | Ajout d'accessoires à un véhicule sans modifier sa classe |
| **Visitor** | `ConcreteRepairer` | Réparation polymorphe : comportement différent selon le type de véhicule |
| **Mediator** | `ControlCenter` | Coordination centralisée entre toutes les stations |

---

## ✨ Fonctionnalités

- **Gestion de stations** : capacité configurable, ajout/retrait de véhicules
- **Véhicules variés** : vélo classique, vélo électrique (batterie), scooter (batterie + recharge auto)
- **Accessoires** (Decorator) : panier 🧺, porte-bagages 🧳
- **États des véhicules** : disponible, en cours d'utilisation, hors service, volé
- **Redistribution automatique** : déclenchée si une station est pleine ou vide (2 stratégies)
- **Détection des vols** : un véhicule seul en station depuis 6 secondes est marqué volé
- **Réparations** : véhicule cassé après 3 utilisations → réparé par un technicien (3s)
- **Interface graphique** Swing interactive : voir les stations, prendre/rendre des véhicules, log en direct
- **Interface console** colorée (ANSI) avec 5 scénarios automatiques

---

## 🎬 Scénarios

| # | Nom | Description | Durée |
|---|-----|-------------|-------|
| 1 | Initialisation & distribution | 4 stations, 11 véhicules, 3 étapes de prise/retour, redistribution automatique | ~12s |
| 2 | Redistribution (station PLEINE) | Station 1 remplie → centre de contrôle déclenche une redistribution | ~6s |
| 3 | Redistribution (station VIDE) | Station 2 vide → centre de contrôle déclenche une redistribution | ~6s |
| 4 | Véhicule hors service → réparation | Scooter utilisé 3 fois → passe Broken → réparé automatiquement | ~11s |
| 5 | Véhicule volé | Vélo seul en station depuis >6s → passe à l'état Stolen | ~17s |
| 6 | Interface graphique | Lance l'interface Swing interactive | — |

---

## 🚀 Lancer le projet

**Prérequis :** Java 11+, Maven 3.x

### Cloner le dépôt
```bash
git clone https://github.com/AmineChbari/projects.git
cd projects/others/location-velos
```

### Compiler
```bash
mvn compile
```

### Lancer l'application (menu console + GUI)
```bash
mvn exec:java
```

### Lancer les tests
```bash
mvn test
```

### Générer le rapport de couverture (JaCoCo)
```bash
mvn verify
```
Rapport disponible dans : `target/site/jacoco/index.html`

### Générer la documentation Javadoc
```bash
mvn javadoc:javadoc
```
Documentation disponible dans : `target/site/apidocs/index.html`

### Construire le JAR exécutable
```bash
mvn package
java -jar target/vlille-1.0-SNAPSHOT.jar
```

---

## 🧪 Tests & Couverture

Le projet inclut **21 classes de tests** (JUnit 5) couvrant :

- Cycle de vie des véhicules (états, transitions, exceptions)
- Opérations des stations (put, take, isFull, isEmpty)
- Centre de contrôle (redistribution, réparations, détection de vols)
- Décorateurs (BasketDecorator, LuggageRackDecorator)
- Stratégies de redistribution (RoundRobin, Random)
- Réparateur (ConcreteRepairer, Repairer)

```bash
mvn test                  # Lancer tous les tests
mvn verify                # Tests + rapport JaCoCo
```

---

## 📁 Structure du projet

```
location-velos/
├── src/
│   ├── main/
│   │   ├── java/coo/vlille/
│   │   │   ├── Main.java                    # Point d'entrée console (menu + 5 scénarios)
│   │   │   ├── MainV2.java                  # Interface Swing (scénarios automatiques)
│   │   │   ├── controlCenter/
│   │   │   │   ├── ControlCenter.java       # Médiateur + Observer
│   │   │   │   ├── Observer.java            # Interface Observer
│   │   │   │   └── redistribution/
│   │   │   │       ├── RedistributionStrategy.java
│   │   │   │       ├── RoundRobinStrategy.java
│   │   │   │       └── RandomStrategy.java
│   │   │   ├── station/
│   │   │   │   ├── Station.java             # Station physique (Observable)
│   │   │   │   └── Observable.java
│   │   │   ├── vehicle/
│   │   │   │   ├── Vehicle.java             # Classe abstraite parente
│   │   │   │   ├── bike/
│   │   │   │   │   ├── classicBike/ClassicBike.java
│   │   │   │   │   └── electricBike/ElectricBike.java
│   │   │   │   ├── scooter/Scooter.java
│   │   │   │   ├── state/                   # STATE pattern (4 états)
│   │   │   │   ├── decorator/               # DECORATOR pattern
│   │   │   │   └── util/Color.java
│   │   │   ├── staff/repairer/
│   │   │   │   ├── Repairer.java
│   │   │   │   └── ConcreteRepairer.java    # VISITOR pattern
│   │   │   ├── timer/TimeManager.java
│   │   │   └── ui/VLilleApp.java            # Interface interactive Swing
│   │   └── resources/image/
│   │       ├── logo.png
│   │       └── background.jpg
│   └── test/java/coo/vlille/               # 21 classes de tests JUnit 5
├── pom.xml
└── README.md
```

---

## 👥 Auteurs

**Amine Chbari** — L3 Informatique, Université de Lille  
**Mohamed Amine Benhammane** — L3 Informatique, Université de Lille  
Groupe 1 — Projet COO (Conception Orientée Objet)
