package others;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class Tutorial extends JPanel {
    private Image backgroundImage;
    private int i = 1;
    private String basePath = "src/main/resources/";
    private Image[] images = new Image[2];
    private JLabel texte;
    private String[] textes =
        {"<html> Le jeu se présente de cette manière, les noms des joueurs sont en haut de l'écran. </br>"
            + "Le joueur dont c'est le tour est souligner. </br>"
            + "Sur la gauche il y a le plateau de jeu et sur la droite un chat avec les autres joueurs"
            + "Nous verrons le reste du menu plus tard. </br> Appuyer sur suivant pour continuer</html>",
        "Le jeu commence par le positionnement de deux colonie et d'une route collée à ces colonies.",
        "Le joueur suivant place sa colinie etc...\n"
            + "Une fois arrivé au dernier joueur cela recommence mais cette fois en partant de la fin",
        "C'est donc au tour du joueur 1 de placé sa colonie",
        "Une fois le placement des colonies de départ la partie peut commencer."
            + "Il faut lancer les dés",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15"};

    public Tutorial() {
        setLayout(null);
        setVisible(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        JButton quitBtn = new ButtonImage(basePath + "backMainMenu.png", basePath + "backMainMenu.png",
                650, 150, 2, this::backToMenu, null);
        add(quitBtn);

        JButton nextBtn = new ButtonImage(basePath + "acceptButton.png", basePath + "acceptButton.png",
                850, 120, 0.5, this::next, null);
        add(nextBtn);


        JButton prevBtn = new ButtonImage(basePath + "acceptButton.png", basePath + "acceptButton.png",
                850, 60, 0.5, this::previous, null);
        add(prevBtn);

        texte = new JLabel(textes[0]);
        int[] coords = Resolution.calculateResolution(450, 450);
        texte.setLocation(coords[0], coords[1]);
        texte.setBounds(coords[0], coords[1], 600, 200);
        texte.setBackground(Color.GRAY);
        texte.setOpaque(true);
        texte.setHorizontalAlignment(SwingConstants.CENTER); // Aligner le texte au centre
        texte.setVerticalAlignment(SwingConstants.TOP); // Aligner le texte en haut
        texte.setVerticalTextPosition(SwingConstants.TOP); // Positionner le texte en haut
        texte.setHorizontalTextPosition(SwingConstants.CENTER);

        ImageIcon iconSheep = new ImageIcon(basePath + "tutoriel/sheep.png");
        ImageIcon icon = new ImageIcon(basePath + "tutoriel/1.png");
        ImageIcon iconBubble = new ImageIcon(basePath + "tutoriel/speechbubble.png");

        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        images[0] = iconSheep.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        images[1] = iconBubble.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        add(texte);
    }

    public void backToMenu() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "optionPanel");
    }

    public void next() {
        i++;
        if (i > 16) {
            i = 1;
            backToMenu();
        }
        updateImage();
        updateText();
    }

    public void previous() {
        i--;
        if (i <= 0) {
            i = 1;
            backToMenu();
        }
        updateImage();
        updateText();
    }

    public void updateText() {
        texte.setText(textes[i-1]);
    }

    public void updateImage() {
        ImageIcon icon = new ImageIcon(basePath + "tutoriel/" + i + ".png");
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        g.drawImage(images[0], 185, 700, null);
        //g.drawImage(images[1], 500, 300, null);
    }
}
