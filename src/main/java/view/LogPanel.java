package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class LogPanel extends JPanel {
    private ActionPlayerPanel actionPlayerPanel;
    private JTextPane chatArea;

    public LogPanel(ActionPlayerPanel actionPlayerPanel) {
        this.actionPlayerPanel = actionPlayerPanel;
        setLayout(new BorderLayout());
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessageColor(String message, Color color) {

        StyledDocument doc = chatArea.getStyledDocument();

        // Création d'un ensemble d'attributs et définition de la couleur du texte
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);

        try {
            // Ajout du texte au document avec les attributs de couleur
            doc.insertString(doc.getLength(), message, attrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void addEventLog(int eventNo) {
        String pName = actionPlayerPanel.getApp().getGame().getCurrentPlayer().getName();
        System.out.println("caca");
        switch (eventNo) {
            case 1: addMessageColor("Terrible! Tous les moutons viennent d'être tués "
                    + "par une mystérieuse maladie...\n", Color.RED); break;
            case 2: addMessageColor("Voici les cartes de développement"
                    + " actuellement possédées :\n", Color.BLACK); break;
            case 3: addMessageColor("C'est Noël, chacun reçoit "
                    + "deux cartes ressources aléatoires!\n", Color.BLACK); break;
            case 4: addMessageColor("La case sur laquelle le voleur est actuellement "
                    + "génère exceptionnellement ses ressources!\n", Color.GREEN); break;
            case 5: addMessageColor("Le Joueur avec le plus de points de victoire "
                    + "échange sa main avec celui en ayant le moins.\n", Color.BLACK); break;
            case 6: addMessageColor("Affreux! Tous les joueurs perdent une ressource !\n", Color.RED);
            case 7: addMessageColor("De terribles inondations se sont produites dans les "
                    + "ports : ils sont tous désactivés pendant 2 tours !\n", Color.BLUE); break;
            case 8: addMessageColor("La chance est chamboulée! "
                    + "Toutes les cases prennent les valeurs inverses de celles actuelles ! (Les numéros "
                    + "les moins probables deviennent"
                    + "les plus probables et inversement)\n", Color.BLACK); break;
            case 9: addMessageColor("Les chevaliers ont bien travaillé! "
                    + "Chaque joueur pioche son nombre de "
                    + "chevaliers en carte ressources !\n", Color.GREEN); break;
            case 10: addMessageColor("Tout est chamboulé! "
                    + "L'ordre de jeu est inversé !\n", Color.BLACK); break;
            case 11: addMessageColor("Un terrible brouillard tombe sur la carte et fait "
                    + "disparaitre le contenu des tuiles pendant 2 tours.\n", Color.RED); break;
            case 12: addMessageColor("Un accord est tombé entre les puissances de la carte : un trade ne "
                    + "peut être effectué que s'il est "
                    + "accepté par chacun des joueurs.\n", Color.BLACK); break;
            case 13: addMessageColor("Le capitalisme a encore frappé! Les joueurs en dessous du "
                    + "seuil du voleur doivent abandonner la moitié de leurs cartes !\n", Color.RED); break;
            case 14: addMessageColor("C'est le jour de chance du joueur avec le moins de "
                    + "points de victoire! Il gagne un point de victoire !\n", Color.GREEN); break;
            case 15: addMessageColor("Un terrible incendie fait rage dans les fôrets de la carte! "
                    + "Tout les joueurs perdent leur bois !\n", Color.RED); break;
            case 16: addMessageColor("Les contrôleurs fiscaux arrivent!"
                    + "Perdez une ressource aléatoire par bâtiment sur la carte !\n", Color.RED); break;
            case 17: addMessageColor("C'est l'anniversaire de " + pName
                    + "Chaque autre joueur lui donne une ressource aléatoire !\n", Color.GREEN); break;
            case 18: addMessageColor("Chaque joueur mise une ressource dans un pot commun! "
                    + "Lorsque le dé à 20 faces tombera sur 19, le contenu du pot "
                    + "sera récupéré par le joueur ayant lancé le dé !\n", Color.BLACK); break;
            case 19: addMessageColor("Le contenu du pot commun est "
                    + "raflé par " + pName + "\n", Color.GREEN); break;
            case 20: addMessageColor("Une magie inconnue relance les dés classiques : "
                    + "la valeur tombée ce tour retombe !\n", Color.GREEN); break;
            default: return;
        }
    }

}
