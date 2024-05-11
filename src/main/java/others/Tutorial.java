package others;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import view.utilities.ButtonImage;

public class Tutorial extends JPanel {
    private Image backgroundImage;
    private int i = 1;
    private String basePath = "src/main/resources/";

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

        ImageIcon icon = new ImageIcon(basePath + "tutoriel/1.png");
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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
    }

    public void previous() {
        i--;
        if (i <= 0) {
            i = 1;
            backToMenu();
        }
        updateImage();
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
    }
}
