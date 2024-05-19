# Projet Catan

Bienvenue dans notre projet Catan ! Ce projet a été développé par :

- Lùkas Koltes
- Amael Mangeant Sastre Campos
- Lucas Verriere-Aboud
- Léo Vernicos
- Eva Herson

## Description du Projet

Ce projet est une implémentation numérique du célèbre jeu de société Catan. Nous avons conçu et codé le plateau de jeu, les mécanismes de jeu, et l'interaction utilisateur pour recréer l'expérience de Catan sur un ordinateur.

## Lancement du Projet

Pour lancer le projet, suivez les instructions ci-dessous en fonction de votre système d'exploitation.
**Assurez-vous de disposer de [java version 21.](https://www.oracle.com/java/technologies/downloads/#java21)**

### Sur Linux

1. Ouvrez un terminal.
2. Placez-vous à la racine du projet avec la commande `cd catan`.
3. Exécutez le script `launch.sh` en utilisant la commande suivante :
   ```bash
   ./launch.sh
   ```
4. Suivez les instructions à l'écran pour commencer à jouer 

### Sur Windows

1. Ouvrez une fenêtre de commande
2. Placez-vous à la racine du projet avec la commande cd \chemin\vers\le\projet.
3. Exécutez le script launch.bat en utilisant la commande suivante :
    ```cmd 
    launch.bat
    ```
4. Suivez les instructions à l'écran pour commencer à jouer 

## Fonctionnalités

- **Pavage Hexagonal** : Un plateau de jeu composé d'hexagones, chacun représentant une ressource différente.
- **Système de Coordonnées Cubiques** : Utilisation de coordonnées cubiques pour une gestion précise des hexagones, routes et villes.
- **Placement de Routes et de Villes** : Interaction utilisateur pour placer des routes et des villes en respectant les règles du jeu.
- **Gestion des Ressources** : Distribution des ressources en fonction des valeurs de dés et des positions des colonies et villes.
- **Jeu en Réseau** : Possibilité de jouer en ligne avec d'autres joueurs, permettant des parties multijoueurs.
- **Cartes de Jeu** : Intégration des cartes de développement et de progression du jeu, ajoutant des éléments stratégiques et de variété aux parties.
- **Dé 20** : Un dé à 20 faces est lancé une fois par tour, générant des événements aléatoires qui peuvent influencer le cours de la partie.
- **Animation en jeu** : Les menus de ressources et d'achat de bâtiments sont animés. Il y a également une animation quand on reçoit des ressources. 
- **Jeu en local contre des bots** : Le joueur peut lancer une partie seul et jouer contre des robots.

## Exécution des Tests

Pour exécuter les tests, utilisez la commande suivante :

```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path "bin;lib/*" --scan-class-path
```
Assurez-vous d'être à la racine du projet lors de l'exécution de cette commande.
## Contribuer

Les contributions sont les bienvenues ! Si vous souhaitez améliorer ce projet, veuillez soumettre une pull request avec une description détaillée des modifications proposées.

## Remerciements

Nous tenons à remercier chaleureusement tous les contributeurs, qui ont participé à ce projet, notamment DALL-E, qui a généré toutes les images utilisées pour l'aspect visuel du jeu.
