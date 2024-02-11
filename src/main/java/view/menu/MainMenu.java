package view.menu;

import others.Constants;
import view.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        int width = Constants.Game.WIDTH;
        int height = Constants.Game.HEIGHT;
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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

    private void createButton(String imagePath, int xCoordBaseWidth, int yCoordBaseHeight, int dividerBase) {
        try {

            // Résolution cible
            int width = Constants.Game.WIDTH;
            int height = Constants.Game.HEIGHT;

            // Calcul facteurs d'échelle
            double scaleFactorX = (double) width / Constants.Game.BASE_WIDTH;
            double scaleFactorY = (double) height / Constants.Game.BASE_HEIGHT;

            // Coords pour résolution cible
            int xCoord = (int) (xCoordBaseWidth * scaleFactorX);
            int yCoord = (int) (yCoordBaseHeight * scaleFactorY);

            // New Divider pour résolution cible
            double targetDiagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
            double diagonalRatio = targetDiagonal / Constants.Game.BASE_DIAGONAL;
            double divider = 2 / diagonalRatio; // 2 étant le diviseur pour les images pour la taille 1280x720


            Image image = ImageIO.read(new File(imagePath));
            int scaledWidth = (int) (image.getWidth(null) / divider);
            int scaledHeight = (int) (image.getHeight(null) / divider);
            Image buttonImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);


            Image imageHover = ImageIO.read(new File("src/main/resources/playButton1.png"));
            Image buttonHover = imageHover.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
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

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setIcon(new ImageIcon(buttonHover));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setIcon(new ImageIcon(buttonImage));
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
            case "play" : startGame(); break;
            case "options" : startOptions(); break;
            case "quit" : System.exit(0); break;
            default : quitGame(); break;
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
