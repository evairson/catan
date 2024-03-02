package view;

import model.App;
import model.Player;
import others.Constants;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    private App app;
    private Image backgroundImage;
    private boolean playerWon;
    private JButton returnBt;
    private JButton quitBtn;
    private final Font bigFont = new Font("Serif", Font.PLAIN, (int) (96 / Resolution.divider()));
    private final Font smallFont = new Font("Serif", Font.PLAIN, (int) (72 / Resolution.divider()));
    private Player player;
    public EndPanel(App app, boolean playerWon, Player player) {
        this.playerWon = true;
        this.app = app;
        this.player = player;
        setLayout(null);
        updatePanel();
        initializeButtons();
        initializeLabels();
        setVisible(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);

    }

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        System.out.println(width);
        System.out.println(height);
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
    private void initializeButtons() {
        String basePath = "src/main/resources/";
        returnBt = new ButtonImage(basePath + "backMainMenu.png", basePath + "backMainMenu.png",
                350, 460, 1, this::returnToMainMenu, null);
        quitBtn = new ButtonImage(basePath + "quitButton.png", basePath + "quitButtonHover.png",
                750, 450, 1, this::quitapp, null);
        add(returnBt);
        add(quitBtn);
    }

    private void initializeLabels() {
        if (playerWon) {
            createEndLabel("Vous avez gagné", 485, 50, returnBt.getWidth(), bigFont);
        }
        createEndLabel("Vos points : " + player.getPoints(), 445, 150,
                (int) (returnBt.getWidth() * 1.2), smallFont);

    }
    private JLabel createEndLabel(String txt, int x, int y, int width, Font font) {
        JLabel winLabel = new JLabel(txt, SwingConstants.CENTER);
        int[] coords = Resolution.calculateResolution(x, y);
        winLabel.setBounds(coords[0], coords[1], width, (int) (108 / Resolution.divider()));
        winLabel.setFont(font);
        winLabel.setForeground(Color.WHITE);
        add(winLabel);
        return winLabel;
    }

    public void updatePanel() {
        if (player != null) {
            this.playerWon = player.hasWon();
        }
        if (playerWon) {
            loadBackgroundImage("src/main/resources/winBackgroundB.png");
        } else {
            loadBackgroundImage("src/main/resources/loseBackgroundB.png");
        }
    }
    private void returnToMainMenu() {
        Container contentPane = this.app.getGameWindow().getContentPane();
        CardLayout layout = this.app.getGameWindow().getLayout();
        layout.show(contentPane, "mainMenu");
    }
    private void quitapp() {
        System.exit(0);
    }
}
