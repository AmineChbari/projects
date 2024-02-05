# l2s4-projet-2023

# Equipe
https://docs.google.com/document/d/1fsCg4oogQN5Nwmr-GFMYIJdasQhdysO0iAH830k9c8I/edit?usp=sharing

- yassin amazrayin
- Amine Chbari
- Fabio Vandewaeter
- Glyn Iganze

# Sujet

[Le sujet 2023](https://www.fil.univ-lille1.fr/portail/index.php?dipl=L&sem=S4&ue=Projet&label=Documents)

# commande 
(Trouvables dans le fichier Makefile)

# pour compiler manuellement suivre les etapes suivantes.
## classpath
Il faut au préalable ajouter le jar au CLASSPATH :<br/>
* `export CLASSPATH=$CLASSPATH:src:classes:./jars/json-20220924.jar`

## Compiler les classes

* `make cls`

## Générer la javadoc

* `make doc`

## Compiler les tests

* `make test`

## Exécuter les tests

Sous la forme :
* `java -jar test4poo.jar pandemic.disease.DiseaseTest`

## Créer le jar

* `make jeu.jar`

## Exécuter le jar

* `java -jar jars/jeu.jar`
    * si pas d'argument defaul map miniMap.json et 4 joueurs.
    * ajouter <fileName.json> en 1er argument si vous voulez charger une autre carte
    * ajouter un int entre 2 et 4 pour choisir le nombre de joueurs voulu pour la partie.


---

# Description des classes

## package board

### Board

Représente le plateau de jeu, et prend en charge les villes, le taux d'infection, le nombre de cubes etc.<br/>
Le Board s'occupe notamment de lire le fichier JSON et d'initialiser les villes, leurs maladies et leurs voisines.

### Deck

Représente la forme du deck du jeu.


## package city

### City

Représente une ville, qui possède une maladie de départ, qui a des villes voisines et qui peut avoir un laboratoire de recherche et un certain nombre de cubes de maladies.<br/>
La ville gère notamment les principes de contamination, d'éclosion et de traitement des maladies.

## package disease

### Disease

## package player 
represente les players avec ses roles 
## package Action
Represente tous les Actions normales et speciles


Une classe enum qui représente les maladies du jeu ; chacune a trois attributs :

- value : leur numéro
- cured : vrai s'il existe un remède
- erradicated : vrai si les joueurs ont erradiqué la maladie grâce au remède

## package player.action
On choisi de représenter les actions sous la forme de classes qui héritent d'une classe abstraite Action.<br/>
Le joueur aura une liste de 5 actions avec à chaque fois une des actions qui lui sera propre, et qui sera une version améliorée d'une action de base.<br/>
Par exemple le rôle Scientist aura l'action CureScientist, qui permet de trouver un remède pour seulement 4 cartes, au lieu de Cure.

## package Game
classe game permet de gere la boucle de jeu dans une partie
## package Game.listchooser
class permettant de faire un choix parmis une liste d'objet avec un arg permetant de choisir la version random choose ou interactive choose.
---

# Livrables

https://lucid.app/documents/view/fcde23dd-54d1-42b8-802d-3b8f572e879a

## Livrable 1
![Livrable1](../img/Livrable1.png)


### Atteinte des objectifs
  * Les differents objectif sont atteint pour se premier livrable, nous avons deja reflechi a comment le projet pourrais s'orienter par la suite.
  
  * objectif remplis: 
    * créer les villes et une carte 
    * lecture des données à partir d'un fichier JSON , 
    * livrable1Main.jar réalisera des infections de villes, et sur la ville-7 déclenchera 2 éclosion separer par des print(step) qui represente les etapes d'infection et l'etats des villes modification.

  
     
### Difficultés pour se livrable.
* Nous avons rencontrer des difficultés avec la classe board, Principalement sur ses tests et ses methodes. Nous avons reussi a fixer les problemes rencontrées aupour permettre l'executable  niveaux des attentes du livrables. 


* Probleme avec le logiciel eclipse pendant toute la periode du livrable1. Les erreurs eclipse nous ont pris pas mal de temps pendant les cours ce qui nous a ralentis dans le devloppement de code.



## Livrable2
![Livrable2](../img/livrable2.BOARD,CITY,DISEASE.png)
![Livrable2](../img/livrable2.card.png)
![Livrable2](../img/Livrable2.player.png)


### Atteinte des objectifs
  * Les differents objectif sont atteint pour se livrable
  
  * objectif remplis: 
    * créer un plateau de jeu à partir d'un fichier JSON passé en argument sur la ligne de commande
    * créer 4 joueurs, chacun ayant un rôle différent, les placer sur une ville (peu importe laquelle)
    * tirer 2 cartes de la pile des cartes infection et réaliser l'infection associée
    * pour chaque joueur tirer 2 cartes de la pile des cartes joueurs et les ajouter à la main du joueur, le cas échéant déclencher une épidémie

  
     
### Difficultés pour se livrable.
* De nombreuses fonctions à implementer, donc de nombreuses erreurs, même si elles ont rapidement pu être corrigées.


### Choix de modélisation
#### Deck
Le Deck repésente à la fois un paquet de carte (ex: paquet de cartes joueur) et sa défausse. On crée ainsi un type paramétré qui pourra gérer tout type de cartes durant la partie.<br/>
On représente les cartes accessibles par une pile comme dans le jeu, et la défausse par une simple liste.<br/>
Les decks seront remplis en appelant la méthode Board.init(List<Player> players) dans un main.
#### Cartes
Les cartes héritent toutes d'une classe abstraite Card, car cela permet de créer des listes les contenant toutes.<br/>
Les InfectionCard et PlayerCard ont la particularité d'être liée à une ville et une maladie, ce qui nous pousse à créer une classe abstraite CityCard qui hérite de Card et dont elle hérite, afin de leur donner à chacunes les attributs et méthodes adaptés.<br/>
Les cartes seront créées lors de l'appel de la méthode Board.init(List<Player> players).
#### Joueur
Toutes les joueurs connaissent la ville où ils se trouvent, possèdent une main de PlayerCard et ont un nom.<br/>
Les joueurs seront crée dans le main et passé en paramètre lors de l'appel de la maéthode Board.init(List<Player> players).


## Livrable 3
![Livrable3](../img/livrable3_player.png)
![Livrable3](../img/livrable3_Action.png)
![Livrable3](../img/livrable3_action_chooser_et_game.png)

### Atteinte des objectifs

### Difficultés restant à résoudre
---
- Lorsqu'une maladie est erradiquée, elle doit s'afficher en -1 cubes pour toutes les villes, mais cela ne se fait que dans la ville où le joueur a erradiqué la maladie. (il faut propager l'information dans toutes les villes)
- Lors d'une épidémie on se retrouve toutes les villes qui ont la maladie qui a éclos à 3 cubes. (pourtant il n'y avait pas de problème au Livrable 2 et les tests sur l'éclosion passent)
---

## Livrable 4
![Livrable4](../img/livrable4.svg)
   
### Difficultés pour se livrable.
les test de game.play et main/livrable{1,4} pour augmenter notre couvertures dans le src


### Atteinte des objectifs

- Pouvoir terminer le jeu avec une condition d'arrêt pour win ou lose.
- Avoir une couverture maximale qui couvre l'ensemble des méthodes ajoutées.
- Avoir un bon affichage modélisant tous les éléments du jeu.
- Pouvoir choisir le nombre de joueurs au début du jeu à l'aide de la ligne de commande.
- Faire une documentation qui explique bien le rôle de chaque méthode.




---





# Journal de bord

## Semaine 1

we play at the game for analyse rules and have an idea how to play.
we fork the projet and install git repository to eclipse ide.
create  a temporary uml diagram on lucid website visible at this link:

https://lucid.app/lucidchart/fcde23dd-54d1-42b8-802d-3b8f572e879a/edit?view_items=mStqZxR1-e7g&invitationId=inv_2c73f7bc-6a95-49c0-b690-7db023d58805

the 27/01 we work on lucid for upgrade the uml and we thought about how to model the map

    

## Semaine 2

we update uml and repository, making test ; we also added a disease.Disease class and its tests, in order to represent the diseases.`<br/>`
we start to create the methods necessary to read the JSON, such as AbstractBoard, Board and City.



## Semaine 3

after correcting some folders errors, we add the classes Building and Laboratory, in order to represent research laboratories ; Building is an abstract class from wich Laboratory inherits.`<br/>`
we endend up restructuring the project by creating a pandemic package, in wich all the other game packages will be found.

## Semaine 4 rendu livrable1

the objective is to solve the problems of the City and Board classes, but we also take the opportunity to restructure the project : there are only pandemic.board.Board, pandemic.board.Laboratory, pandemic.city.City and pandemic.diseaseDiseases classes left.

the uml diagramm for board, city and disease was available with the L1.img at ./img/L1.img

### en raison du nombre de ligne que l'on vas ajouter nous allons continuer le readme pour ce livrable en francais.
une correction sera faite par la suite.
## Semaine 5 semaine du 27 au 6

debriefing of rendering "livrable1", we upgrade uml and we talk about how we create the card and deck.

nous reflechisson a comment faire les deck et avons cree un nouveau package pour les les cartes joueur et infenous avon completer un le test du board.
dans la class city nous avons remonter une constante qui etait dans le code en attribut static de city. 

## Semaine 6  semaine du 6 au 13
nous avons commenez a reflechir à comment implementer les role des differents joueur .
nous avons aussi commencer a reflechir au methodes qui seront dans les deck. 


nous avons ajouter plusieur classe qui reprensente les differentes carte et par la suite adapter le deck par rapport au carte que nous avons repenser.
    
pour la classe EpidemicCard qui extend de Card nous avon ajouter plusieur de ses methodes ( prend en parmatre le board du jeux pour pour pouvoir utiliser son ability (increase , infect, intensify)) 


nous reflechission deja a comment mettre les actions , si l'on devais les inclure dans les role ou cree tout hierachie un peu comme les carte.


## Semaine 7 semaine du 13  au 20 mars rajout d'une semaine de delai due au examens
nous avions nos examens intermediare et finaux nous n'avons travaillez qu'apres la fin des examens.

nous avons modifier la classe board pour ajouter une methode qui recupere le nombre de ville dans le board.
retrait du getDiseaseColor() et ajout de init() qui gere l'initialisation du board au lancement.

quelque modification dans les classe de test 
    
Pour player on a inclue une methode treat disease(disease)
reflexion a propos des types generique.



## Semaine 8 rendu livrable 2 semaine du 20 au 27 mars
le lundi 20 mars greve de metro ilevia, impossble de se deplacer au cours pour poser nos derniere interogations avant le rendu du vendredi 24 par rapport au methode de cards 

cette semaine est charger en commit car nous travaillons en meme temps sur tous le projet et sur differente classe.
nous nous somme organiser sur plusieur jours avec plusieur deadline pour que nous puissions finir les attentes du rendus en temps et en heure.

nous avons separé plusieur taches tel que ecrire les test de cards, completer le board pour que l'init fonctionne bien.
mais nous somme restez quelque temps bloqué sur des erreurs avec les test en rapport en jsonObject.
l'erreur etait du a nos modification de l'init de board.

nous avons modifier pour que nbofinfection rate soit correct lors de son incrementation (2,2,2,3,3,4,4)     
changement des assertSame en assertEquals car source d'erreur (les deux n'ont pas la meme utilité)


nous nous somme apercus que nos deck pouvais etres fusionais ( InfectionDeck and PlayerDeck) en deck.
nous avons bcp changer le code de board et du deck au fur et a mesure que nous avancion car nous reflechision en mm temps a l'implementation des extensions.

nous nous somme partager les docs et les test.

cette methode de travaille nous a generé pas mal de merge mais cela n'etait pas genant car on modifié chacun un fichier different.

nous avons pu passer au main une fois les test et et la doc completé.   

nous avons ajouter a l'init la fonctionnalité du depart de la ville aleatoire.

nous avons continuer le main et ajuster les derniere detail ainsi que des courte modification de test.

(pour le coverage les main et livrable ne sont pas couvert ce qui reduit la couverture total).

derniere modification de l'uml et nous passons au readme pour les commandes de compilation et d'excution du projet.

ajout de couleur dans le main 
    
et test sur les pc de la fac pour etre certain que tous fonctionne.



## Semaine 9 semaine du 27 au 3 avril

correction par rapport au livrable 2 pour supprimé l'instance of du main 

move all cards methods from Board to the Card classes
ajout de la disease en argument de card
modification des test correspondant au changement.
modification du main
remove cards ability du board.

reflextion pour comment implementer les actions. interface ou abstract.

dans notre cas nous avons interet a utiliser une interface .

nous aurons au cours de la semaine les methodes pour mettre en place un choix d'un joueur et cela nous aidera a mieu implementer les actions par la suite.

ajout du package action et des differente classe herité qui corresponde au action du jeux.
nous hesiton entre nous si nous devons considerer piocher et defausser comme action.

nous avons ajouter l'action do nothing.
action : move, treatdisesas, construct, cure, noneAction


   

## Semaine 10 3 au 10 avril 

ajout du Listchooser et correction des actions pour avoir une methode doSomething() et to string que chaque action heritera et cela permet de doubler les action avec leur actionSpeciale et modifie le dosomthing() 
ajout d'un current player dans Board

complete the code of action

ajout de construct test 




## Semaine 11 rendu livrable 3  semaine du 10 au 17
* nous completons a posteriori le contenue de chaque semaine precedente. comme nous discutions principalement via notre channel discord des choix d'implementation et de l'orientation du projet nous rajoutons les semaines manquante a partir de nos discusion,notre organisation et nos commit  

    pour faire les 4 action on vas boucler lors du tour du joueur.

    nous devons faire attention au action qui ne coute pas de point d'action
    on revois l'infect city 
    on ajoute la carte de 12 ville pour un affichage plus cours du  main.
    on modifie city et city test pour faire un erradicated et un cure qui fonctionne avec les cubes.

    ajout du main et mise a jour de l'uml
    
## Interuption pedagogique avril(deux semaine.)

## Semaine 12
Nous avons résolu les problèmes que nous avions avant les vacances concernant le nombre de cubes après l'infection, et nous avons modifié l'affichage pour minimiser les informations que nous montrons à chaque étape.

## Semaine 13 rendu livrable 4
Dans ce rendu final, nous avons modifié le livrable de sorte que nous puissions terminer le jeu. La grande partie du travail était faite lors des derniers livrables, mais ce livrable nous a permis d'organiser le jeu avec un message de "Win" ou "Lose".  
Nous avons également essayé de tester toutes les méthodes pour obtenir une bonne couverture du code. La documentation du code est entièrement terminée.  
Il nous reste à faire la présentation du projet lors de la soutenance.

