package view.menu;

import model.Game;
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
    private Image backgroundImage;

    public MainMenu(GameWindow game) {
        this.game = game;
        setLayout(null); // Disposer les boutons verticalement
        loadBackgroundImage("src/main/resources/mainMenu.png");
        initializeButtons();
    }

    private void loadBackgroundImage(String path){
        System.out.println(path);
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(1920,1080, Image.SCALE_SMOOTH);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

     private void initializeButtons() {
        try {
            Image image = ImageIO.read(new File("src/main/resources/playButton.png"));
            Image playImage = image.getScaledInstance(image.getWidth(null) / 3, image.getHeight(null) / 3, Image.SCALE_SMOOTH);
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

            playButton.setBounds(500, 300, playImage.getWidth(null), playImage.getHeight(null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//         try {
//            Image[] images = new Image[2];
//            images[0] = ImageIO.read(new File("src/resources/modeNormalButton.png"));
//            images[1] = ImageIO.read(new File("src/resources/modeMarathonButton.png"));
//
//            for (int i = 0; i < images.length; i++) {
//                images[i] = images[i].getScaledInstance(
//                        images[i].getWidth(null) / 3, images[i].getHeight(null) / 3,
//                        Image.SCALE_SMOOTH
//                );
//            }
//            optionsButton = new JButton(new ImageIcon(images[0])) {
//                @Override
//                protected void paintComponent(Graphics g) {
//                    Graphics2D g2d = (Graphics2D) g.create();
//                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                    g2d.drawImage(images[0], 0, 0, this.getWidth(), this.getHeight(), null);
//                    g2d.dispose();
//                    super.paintComponent(g);
//                }
//
//                @Override
//                public boolean contains(int x, int y) {
//                    Area area = new Area(new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight()));
//                    return area.contains(x, y);
//                }
//            };
//
//            optionsButton.setPreferredSize(new Dimension(images[0].getWidth(null), images[0].getHeight(null)));
//            optionsButton.setBorderPainted(false);
//            optionsButton.setFocusPainted(false);
//            optionsButton.setContentAreaFilled(false);
//
//            optionsButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    startOptions();
//                }
//            });
//
//            optionsButton.setBounds(320, 330, images[0].getWidth(null), images[0].getHeight(null));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        add(playButton);
//        add(optionsButton);
    }
    
    public void startGame(){
        System.out.println("Lancement du jeu...");
    }
    
    public void startOptions(){
        System.out.println("Options du jeu...");
    }
}
