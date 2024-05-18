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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class Tutorial extends JPanel {
    private Image backgroundImage;
    private int i = 1;
    private String basePath = "src/main/resources/";
    private Image[] images = new Image[2];
    private JLabel texte;

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

        texte = new JLabel(readLine(0));
        int[] coords = Resolution.calculateResolution(450, 450);
        texte.setLocation(coords[0], coords[1]);
        texte.setBounds(coords[0], coords[1], 600, 200);
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
        images[1] = iconBubble.getImage().getScaledInstance(820, 600, Image.SCALE_SMOOTH);

        add(texte);
    }

    public void backToMenu() {
        i = 1;
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "optionPanel");
    }

    public void next() {
        i++;
        if (i >= 16) {
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
        texte.setText(readLine(i - 1));
        if (i >= 13) {
            int[] coo = Resolution.calculateResolution(450, 350);
            texte.setLocation(coo[0], coo[1]);
        } else {
            int[] coords = Resolution.calculateResolution(450, 450);
            texte.setLocation(coords[0], coords[1]);
        }
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
        if (i >= 13) {
            g.drawImage(images[0], 185, 500, null);
            g.drawImage(images[1], 385, 150, null);
        } else {
            g.drawImage(images[0], 185, 700, null);
            g.drawImage(images[1], 380, 350, null);
        }
    }

    public String readLine(int n) {
        try {
            String line = Files.readAllLines(Paths.get(basePath + "texteTutoriel.txt")).get(n);
            return "<html>" + line + "</html>";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
