package view.menu;

import others.Constants;
import view.OptionPanel;
import view.utilities.ButtonImage;
import javax.swing.*;

import model.App;

import java.awt.*;

public class MainMenu extends JPanel {

    private JButton playBtn;
    private JButton optionsBtn;
    private JButton quitBtn;
    private Image backgroundImage;
    private App app;

    public MainMenu(App app) {
        this.app = app;
        setLayout(null); // Disposer les boutons verticalement
        loadBackgroundImage("src/main/resources/mainMenu.png");
        initializeButtons();
        setVisible(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        OptionPanel optionPanel = new OptionPanel();
        add(optionPanel);
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
                500, 100, 1, this::startapp, null);
        optionsBtn = new ButtonImage(basePath + "optionsButton.png", basePath + "optionsButtonHover.png",
                550, 330, 1, this::startOptions, null);
        quitBtn = new ButtonImage(basePath + "quitButton.png", basePath + "quitButtonHover.png",
                558, 450, 1, this::quitapp, null);
        add(playBtn);
        add(optionsBtn);
        add(quitBtn);
    }

    public void startapp() {
        System.out.println("Lancement du jeu...");
        Container parent = getParent();
        app.createNewGame();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        app.addPanels();
        app.setPlaying(true);
        parentLayout.show(parent, "actionPlayerPanel");
        app.getActionPlayerPanel().revalidate();
        app.getActionPlayerPanel().repaint();
        app.update();
    }

    public void startOptions() {
        Container parent = getParent();
        CardLayout parentLayout = (CardLayout) parent.getLayout();
        parentLayout.show(parent, "optionPanel");
        System.out.println("caca");
        app.update();
    }

    public void quitapp() {
        System.exit(0);
    }
}
