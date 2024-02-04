package view.menu;

import model.Game;
import view.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private Game game;
    private JButton playButton;
    private JButton optionsButton;
    private Image backgroundImage;

    public MainMenu(Game game) {
        setLayout(null); // Disposer les boutons verticalement
        loadBackgroundImage("src/resources/mainMenu.png");
//        initializeButtons();
    }

    private void loadBackgroundImage(String path){
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(1920,1080, Image.SCALE_SMOOTH);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

//    private void initializeButtons() {
//        ImageIcon playIcon = new ImageIcon("src/resources/button.png");
//        Image img = playIcon.getImage();
//        Image newimg = img.getScaledInstance(305, 320,  java.awt.Image.SCALE_SMOOTH);
//        playIcon = new ImageIcon(newimg);
//
//        // Modifier la forme du bouton pour être un cercle
//        playButton = new JButton(playIcon) {
//            @Override
//            protected void paintComponent(Graphics g) {
//                if (getModel().isArmed()) {
//                    g.setColor(Color.lightGray);
//                } else {
//                    g.setColor(getBackground());
//                }
//                g.fillOval(0, 0, getSize().width-1, getSize().height-1);
//                super.paintComponent(g);
//            }
//
//            @Override
//            public boolean contains(int x, int y) {
//                Shape shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
//                return shape.contains(x, y);
//            }
//        };
//        playButton.setBounds(250, 150, 140, 140);
//        playButton.setContentAreaFilled(false);
//        playButton.setBorderPainted(false);
//        playButton.setFocusPainted(false);
//        playButton.setOpaque(false);
//        playButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                startGame();
//            }
//        });
//
//        try {
//            Image[] images = new Image[3];
//            images[0] = ImageIO.read(new File("src/resources/EasyButton.png"));
//            images[1] = ImageIO.read(new File("src/resources/NormalButton.png"));
//            images[2] = ImageIO.read(new File("src/resources/HardButton.png"));
//
//            for (int i = 0; i < images.length; i++) {
//                images[i] = images[i].getScaledInstance(
//                        images[i].getWidth(null) / 2, images[i].getHeight(null) / 2,
//                        Image.SCALE_SMOOTH
//                );
//            }
//            difficultyButton = new JButton(new ImageIcon(images[0])) {
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
//            difficultyButton.setPreferredSize(new Dimension(images[0].getWidth(null), images[0].getHeight(null)));
//            difficultyButton.setBorderPainted(false);
//            difficultyButton.setFocusPainted(false);
//            difficultyButton.setContentAreaFilled(false);
//
//            difficultyButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    changeDifficulty(images);
//                }
//            });
//
//            difficultyButton.setBounds(160, 300, images[0].getWidth(null), images[0].getHeight(null));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        try {
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
//            modeButton = new JButton(new ImageIcon(images[0])) {
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
//            modeButton.setPreferredSize(new Dimension(images[0].getWidth(null), images[0].getHeight(null)));
//            modeButton.setBorderPainted(false);
//            modeButton.setFocusPainted(false);
//            modeButton.setContentAreaFilled(false);
//
//            modeButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    changeMode(images);
//                }
//            });
//
//            modeButton.setBounds(320, 330, images[0].getWidth(null), images[0].getHeight(null));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        add(playButton);
//        add(difficultyButton);
//        add(modeButton);
//    }
}
