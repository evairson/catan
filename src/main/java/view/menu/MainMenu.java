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
    private JButton playButton;
    private JButton optionsButton;
    private JButton quitButton;
    private Image backgroundImage;

    private int scaleHeight;

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
        try {
            Image image = ImageIO.read(new File("src/main/resources/playButton.png"));
            int scaledWidth = image.getWidth(null) / 2;
            int scaledHeight = image.getHeight(null) / 2;
            Image playImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            playButton = new JButton(new ImageIcon(playImage)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(playImage, 0, 0, this.getWidth(), this.getHeight(), null);
                    g2d.dispose();
                    super.paintComponent(g);
                }

                @Override
                public boolean contains(int x, int y) {
                    Area area = new Area(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));
                    return area.contains(x, y);
                }
            };

            playButton.setPreferredSize(new Dimension(playImage.getWidth(null), playImage.getHeight(null)));
            playButton.setBorderPainted(false);
            playButton.setFocusPainted(false);
            playButton.setContentAreaFilled(false);

            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startGame();
                }
            });

            playButton.setBounds(500, 100, playImage.getWidth(null), playImage.getHeight(null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

         try {
             Image image = ImageIO.read(new File("src/main/resources/optionsButton.png"));
             Image optionsImage = image.getScaledInstance(image.getWidth(null) / 2, image.getHeight(null) / 2, Image.SCALE_SMOOTH);

             optionsButton = new JButton(new ImageIcon(optionsImage)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(optionsImage, 0, 0, this.getWidth(), this.getHeight(), null);
                    g2d.dispose();
                    super.paintComponent(g);
                }

                @Override
                public boolean contains(int x, int y) {
                    Area area = new Area(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));
                    return area.contains(x, y);
                }
            };

            optionsButton.setPreferredSize(new Dimension(optionsImage.getWidth(null), optionsImage.getHeight(null)));
            optionsButton.setBorderPainted(false);
            optionsButton.setFocusPainted(false);
            optionsButton.setContentAreaFilled(false);

            optionsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startOptions();
                }
            });

            optionsButton.setBounds(550, 330, optionsImage.getWidth(null), optionsImage.getHeight(null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

         try {
             Image image = ImageIO.read(new File("src/main/resources/quitButton.png"));
             Image quitImage = image.getScaledInstance(image.getWidth(null) / 2, image.getHeight(null) / 2, Image.SCALE_SMOOTH);

             quitButton = new JButton(new ImageIcon(quitImage)) {
                 @Override
                 protected void paintComponent(Graphics g) {
                     Graphics2D g2d = (Graphics2D) g.create();
                     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                     g2d.drawImage(quitImage, 0, 0, this.getWidth(), this.getHeight(), null);
                     g2d.dispose();
                     super.paintComponent(g);
                 }

                 @Override
                 public boolean contains(int x, int y) {
                     Area area = new Area(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));
                     return area.contains(x, y);
                 }
             };

             quitButton.setPreferredSize(new Dimension(quitImage.getWidth(null), quitImage.getHeight(null)));
             quitButton.setBorderPainted(false);
             quitButton.setFocusPainted(false);
             quitButton.setContentAreaFilled(false);

             quitButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     quitGame();
                 }
             });

             quitButton.setBounds(558, 450, quitImage.getWidth(null), quitImage.getHeight(null));
         } catch (IOException ex) {
             ex.printStackTrace();
         }

        add(playButton);
        add(optionsButton);
        add(quitButton);
    }
    
    public void startGame(){
        System.out.println("Lancement du jeu...");
    }
    
    public void startOptions(){
        System.out.println("Options du jeu...");
    }

    public void quitGame() { System.exit(0); }
}
