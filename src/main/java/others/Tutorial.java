package others;

import java.awt.CardLayout;
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
    private Image[] images = new Image[4];
    private JLabel texte;
    private int[] c1;
    private int[] c2;
    private int[] c3;
    private int[] c4;
    private int[] c5;

    public Tutorial() {
        setLayout(null);
        setVisible(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        JButton quitBtn = new ButtonImage(basePath + "backMainMenu.png", basePath + "backMainMenu.png",
                650, 150, 2, this::backToMenu, null);
        add(quitBtn);

        JButton nextBtn = new ButtonImage(basePath + "continueGame.png", basePath + "continueGame.png",
                850, 120, 1, this::next, null);
        add(nextBtn);


        JButton prevBtn = new ButtonImage(basePath + "backGame.png", basePath + "backGame.png",
                850, 60, 1, this::previous, null);
        add(prevBtn);

        texte = new JLabel(readLine(0));
        texte.setVerticalAlignment(SwingConstants.TOP); // Aligner le texte en haut*
        texte.setVerticalTextPosition(SwingConstants.TOP); // Positionner le texte en haut

        ImageIcon iconSheep = new ImageIcon(basePath + "tutoriel/sheep.png");
        ImageIcon icon = new ImageIcon(basePath + "tutoriel/1.png");
        ImageIcon iconBubble = new ImageIcon(basePath + "tutoriel/speechbubble.png");

        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        //taille de l'image en fonction de la résolution

        int[] image0 = Resolution.calculateResolution(66, 60);
        int[] image1 = Resolution.calculateResolution(600, 280);
        int[] image2 = Resolution.calculateResolution(400, 240);
        int[] image3 = Resolution.calculateResolution(575, 130);
        images[0] = iconSheep.getImage().getScaledInstance(image0[0], image0[1], Image.SCALE_SMOOTH);
        images[1] = iconBubble.getImage().getScaledInstance(image1[0], image1[1], Image.SCALE_SMOOTH);
        images[2] = iconBubble.getImage().getScaledInstance(image2[0], image2[1], Image.SCALE_SMOOTH);
        images[3] = iconBubble.getImage().getScaledInstance(image3[0], image3[1], Image.SCALE_SMOOTH);


        //positions de la bulle de texte et du mouton

        c1 = Resolution.calculateResolution(450, 285);
        c2 = Resolution.calculateResolution(445, 520);
        c3 = Resolution.calculateResolution(400, 410);
        c4 = Resolution.calculateResolution(380, 630);
        c5 = Resolution.calculateResolution(380, 500);

        add(texte);
        updateText();
        updateImage();
        revalidate();
        repaint();
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
            int[] coo = Resolution.calculateResolution(480, 300);
            int[] coo2 = Resolution.calculateResolution(310, 200);
            texte.setLocation(coo[0], coo[1]);
            texte.setBounds(coo[0], coo[1], coo2[0], coo2[1]);
        } else {
            int[] coords = Resolution.calculateResolution(440, 430);
            texte.setLocation(coords[0], coords[1]);
            int[] coords2 = Resolution.calculateResolution(535, 300);
            texte.setBounds(coords[0], coords[1], coords2[0], coords2[1]);

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
            g.drawImage(images[2], c1[0], c1[1], null);
            g.drawImage(images[0], c2[0], c2[1], null);
        } else if (i >= 8 && i <= 10) {
            g.drawImage(images[3], c3[0], c3[1], null);
            g.drawImage(images[0], c5[0], c5[1], null);
        } else {
            g.drawImage(images[1], c3[0], c3[1], null);
            g.drawImage(images[0], c4[0], c4[1], null);
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
