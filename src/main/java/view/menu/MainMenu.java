package view.menu;

import view.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private GameWindow game;
    private JButton playBtn;
    private JButton optionsBtn;
    private JButton quitBtn;
    private Image backgroundImage;

    public MainMenu(GameWindow game) {
        this.game = game;
        setLayout(null); // Disposer les boutons verticalement
        loadBackgroundImage("src/main/resources/mainMenu.png");
        initializeButtons();
    }

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

    private void initializeButtons() {
        createButton("src/main/resources/playButton.png", 500, 100, 2);
        createButton("src/main/resources/optionsButton.png", 550, 330, 2);
        createButton("src/main/resources/quitButton.png", 558, 450, 2);
    }

    private void createButton(String imagePath, int xCoord, int yCoord, int divider) {
        try {
            Image image = ImageIO.read(new File(imagePath));
            int scaledWidth = image.getWidth(null) / divider;
            int scaledHeight = image.getHeight(null) / divider;
            Image buttonImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            JButton button = new JButton(new ImageIcon(buttonImage)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(buttonImage, 0, 0, this.getWidth(), this.getHeight(), null);
                    g2d.dispose();
                    super.paintComponent(g);
                }
                @Override
                public boolean contains(int x, int y) {
                    int width = this.getWidth();
                    int height = this.getHeight();
                    Area area = new Area(new Rectangle2D.Double(0, 0, width, height));
                    return area.contains(x, y);
                }
            };

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] list = imagePath.split("/");
                    String action = list[list.length - 1].split("Button")[0];
                    buttonClicked(action);
                }
            });

            button.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setBounds(xCoord, yCoord, scaledWidth, scaledHeight);

            this.add(button);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void buttonClicked(String action) {
        switch (action) {
            case "play" -> startGame();
            case "options" -> startOptions();
            case "quit" -> System.exit(0);
            default -> quitGame();
        }
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
