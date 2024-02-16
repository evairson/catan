package start;

import view.RollingDice;

import javax.swing.*;
import java.awt.*;

public class test extends JFrame {
    public test() {
        setTitle("Ma JFrame avec JPanel"); // Titre de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Action à la fermeture de la fenêtre
        setSize(300, 300); // Taille de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        JPanel panel = new RollingDice(); // Création d'un JPanel
        panel.setBackground(Color.WHITE); // Couleur de fond du JPanel

        // Ajoutez vos composants au JPanel ici

        getContentPane().add(panel); // Ajout du JPanel à la JFrame

        // Autres initialisations ou configurations de la JFrame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            test frame = new test();
            frame.setVisible(true); // Rendre la JFrame visible
        });
    }
}