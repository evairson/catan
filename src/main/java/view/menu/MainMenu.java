package view.menu;

import others.Constants;
import view.utilities.ButtonImage;
import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

    private JButton playBtn;
    private JButton optionsBtn;
    private JButton quitBtn;
    private Image backgroundImage;

    public MainMenu() {
        setLayout(null); // Disposer les boutons verticalement
        loadBackgroundImage("src/main/resources/mainMenu.png");
        initializeButtons();
    }

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

    private void initializeButtons() {
        String basePath = "src/main/resources/";
        playBtn = new ButtonImage(basePath + "playButton.png", basePath + "playButtonHover.png",
                500, 100, 1, this::startGame, null);
        optionsBtn = new ButtonImage(basePath + "optionsButton.png", basePath + "optionsButtonHover.png",
                550, 330, 1, this::startOptions, null);
        quitBtn = new ButtonImage(basePath + "quitButton.png", basePath + "quitButtonHover.png",
                558, 450, 1, this::quitGame, null);
        add(playBtn);
        add(optionsBtn);
        add(quitBtn);
    }

    public void startGame() {
        System.out.println("Lancement du jeu...");
    }

    public void startOptions() {
        System.out.println("Options du jeu...");
    }

    public void quitGame() {
        System.exit(0);
    }
}
