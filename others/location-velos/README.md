# Projet de conception orientée objet V'lille🚲 — *JAVA*
Projet de la matière Conception orienté objet en java portant sur la création d'un réseau de location des vélos et autre véhicules.
## 📋 Travail du binôme:

👤 *Amine CHBARI*  
GRP 1 / L3 INFORMATIQUE / UNIVERSITE DE LILLE  
👤 *Mohamed Amine BENHAMMANE*  
GRP 1 / L3 INFORMATIQUE / UNIVERSITE DE LILLE


## 📋 How to

### 1. RECUPERER SOURCE DEPUIS DEPOT:
Cloner le depot en local:  
```bash 
git clone git@gitlab-etu.fil.univ-lille.fr:amine.chbari.etu/projet-coo-g1.git projet
```

### 2. GENERER LA DOCUMENTATION:
Générer la doc:   
```bash 
mvn javadoc:javadoc
```  

📋 Voir le rapport de la documentation de code:
- Sur Windows:
```bash 
start target/site/apidocs/index.html
```
- Sur Linux:
```bash 
xdg-open target/site/apidocs/index.html
```

### 3. COMPILER ET EXECUTER LES SOURCES:
Compiler:
```bash 
mvn compile
```
Exécuter le main:
```bash 
mvn exec:java -Dexec.mainClass="coo.vlille.Main"
```

### 4. COMPILER ET EXECUTER LES TESTS:
Compiler et executer les tests:
```bash 
mvn test
```
### 5. RAPPORT DE TEST:
Voir le rapport de couverture de code par les tests:
- Sur Windows:
```bash 
start target/site/jacoco/index.html
```
- Sur Linux:
```bash 
xdg-open target/site/jacoco/index.html
```

**View du rapport:** 📊
![Rapport couverture de tests](coverage/image.png)

### 6. NETTOYER LE PROJET:   
```bash 
mvn clean
```
### 7.GENERER UN JAR EXECUTABLE:
```bash 
mvn clean package
```
then:
```bash 
java -jar target/vlille-1.0-SNAPSHOT.jar
```

## 📋 Presentation d'élémemts de code

### Principes de conception utilisés:
DESIGN PATTERN UTILISÉS:

👉 **OBSERVER** : centre de controle est un observer qui notifié chaque fois que l'état d'une station (observable) change. Il est responsable de gérer tous les élements du réseau vlille, il est notifié lorsque une action se produit dans une station et réagit en appliquant le changement nécessaires.

👉 **STRATEGY** : On utilise ce design pattern pour suggerer plusieurs strategies de redistribution de Vehicle lorsqu'on souhaite le faire, on trouve la stratégie round Robin et une autre stratégie aleatoire, avec la possibilite de rejouter d'autres stratégies.

👉 **DECORATEUR** : pour décorer les velos avec des objets (ex: Porte bagages, panier.. )

👉 **STATE** : pour déterminer l'état d'un vehicule lorsqu'on souhaite executer une action dessus.

👉 **VISITOR** : On utilisé ce design pattern pour le personnel de réseau, dans notre exemple cela se produit avec le répérateur qui répare chaque véhicule avec une maniére différente (comme le scooter on doit rénitialiser la batterie à 100 %), et change le state à available to use.

### Elements de conception importants:
- Pour le centre de Contrôle, nous somme partis sur le design pattern MEDIATOR au début, car il est notifié par les stations et le vélos, et applique aussi les changements necessaires en appelant les fonctions convenables, mais finalement on a choisi OBSERVER, car c'est plus proche de son vrai rôle,  il doit être notifié lorsque un station envoie un alerte poue sa redisribution et c'est la station même qui notifie lorsqu'un véhicule doit être réparé.
- L'ensemble de projet est en anglais notamment les noms des classes, méthodes et la javadoc aussi. 
- On fait deux versions de main, une avec un affichage sur le terminal avec des icônes et un autre avec une interface graphique basique.

### Scenarios de l'execution:
SCENARIO 1️⃣ :  
- Initialization normale avec des retraits et depots de différents véhicules.  
- On trouve 4 Stations avec capacité [3, 3, 4, 5] puis faire des retraits and depots. 

SCÉNARIO 2️⃣ :
- Déclenchement de la redistribution lorsque la station est full

SCÉNARIO 3️⃣ :
- Déclenchement de la redistribution lorsque la station est vide
  
SCÉNARIO 4️⃣ :  
- Véhicule cassé puis réparé.  
- Création de 2 stations avec une capacité de 3, pour chacune on donne un véhicule, on le reprends puis on le met dans l'autre station, aisin de suite, après un nombre d'utilisation il sera cassé et le réparateur devrait venir le réparer avec un délai de réparation de 3 secondes.
  
SCÉNARIO 5️⃣ :
- Véhicule volé parce qu'il est seul pendant 2 périodes de temps.

- Création une station avec une capacité de trois et 2 véhicules, en prendre un et laisser l'autre pendant deux périodes, le réparateur devrait venir le réparer.


SCÉNARIO 6️⃣ :
- Une interface graphique


## 🎋 UML

### Le lien vers le diagramme UML : [UML]
https://lucid.app/lucidchart/081c345f-2551-4436-9657-afb5ce774f46/edit?view_items=Eg5~qenwvypp&invitationId=inv_d8ae35f0-19f1-4d4a-bd69-6a1512089d76 
## REFERENCES

### Sujet du projet de vlille : [vlille.pdf]

### Consignes pour le rendu du  projet : [consignes.pdf]







[vlille.pdf]: https://www.fil.univ-lille.fr/~quinton/coo/projet/vlille.pdf "sujet projet"

[consignes.pdf]: https://www.fil.univ-lille.fr/~quinton/coo/projet/consignesRenduProjet.pdf "consignes de rendu"

[UML]: https://lucid.app/lucidchart/081c345f-2551-4436-9657-afb5ce774f46/edit?view_items=Eg5~qenwvypp&invitationId=inv_d8ae35f0-19f1-4d4a-bd69-6a1512089d76 "link UML"