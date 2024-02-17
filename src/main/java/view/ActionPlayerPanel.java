package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.*;

import java.io.IOException;

import model.App;
import model.Game;
import others.Constants;
import view.utilities.Animation;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class ActionPlayerPanel extends JPanel {
    private ButtonImage endTurn;
    private ButtonImage tradeButton;

    private ButtonImage wood;
    private ButtonImage ore;
    private ButtonImage clay;
    private ButtonImage wheat;
    private ButtonImage wool;

    private ButtonImage city;
    private ButtonImage colony;
    private ButtonImage road;

    private ButtonImage plus;

    private ButtonImage card;

    private JLabel namePlayer;
    private App app;
    private Game game;
    private ResourcesPanel resourcesPanel;

    private JPanel cardsPanel;
    private JPanel cardPanel;
    private JPanel playersPanel;


    public ActionPlayerPanel(App app) {
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        this.app = app;
        game = app.getGame();


        setLayout(null);
        setOpaque(true);
        try {
            createNamePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createPlayerPanel();

        Animation animate = new Animation();
        int xCoord = Resolution.calculateResolution(200, 650)[0];
        int yCoord = Resolution.calculateResolution(200, 650)[1];
        MouseAdapter animMouse = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animate.jPanelYUp(yCoord, yCoord - 150, 2, 1, resourcesPanel);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                animate.jPanelYDown(yCoord - 150, yCoord, 2, 1, resourcesPanel);
            }
        };
        resourcesPanel = new ResourcesPanel(animMouse);
        resourcesPanel.setVisible(true);
        resourcesPanel.setBounds(xCoord, yCoord, (int) (975 / Resolution.divider()),
                (int) (210 / Resolution.divider()));
        resourcesPanel.addMouseListener(animMouse);

        add(resourcesPanel);
        createButton();
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn, null);
        tradeButton = new ButtonImage(basePath + "tradeButton.png", basePath + "tradeButton.png",
                50, 560, 5, this::trade, null);

//        wood = new ButtonImage(basePath + "resources/wood.png", basePath + "resources/wood.png",
//                200, 550, 2, null);
//        ore = new ButtonImage(basePath + "resources/ore.png", basePath + "resources/ore.png",
//                300, 550, 2, null);
//        clay = new ButtonImage(basePath + "resources/clay.png", basePath + "resources/clay.png",
//                400, 550, 2, null);
//        wheat = new ButtonImage(basePath + "resources/wheat.png", basePath + "resources/wheat.png",
//                500, 550, 2, null);
//        wool = new ButtonImage(basePath + "resources/wool.png", basePath + "resources/wool.png",
//             600, 550, 2, null);

        card = new ButtonImage(basePath + "card.png", basePath + "card.png",
        770, 560, 3, this::addcardsPanel, null);


        city = new ButtonImage(basePath + "building/city.png", basePath + "building/city.png",
        1150, 20, 2, null, null);
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colony.png",
        1150, 130, 2, null, null);
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/road.png",
        1150, 220, 2, null, null);

        plus = new ButtonImage(basePath + "card.png", basePath + "card.png",
        1170, 310, 5, this::drawCard, null);

//        add(wood);
//        add(wool);
//        add(ore);
//        add(clay);
//        add(wheat);

        add(city);
        add(colony);
        add(road);

        add(plus);

        add(tradeButton);
        add(endTurn);
        add(card);
    }

    private void changeTurn() {
        game.endTurn();
        update();
    }

    private void update() {
        namePlayer.setText(" " + game.getCurrentPlayer().getName().toUpperCase());
        try {
            String src = "src/main/resources/pion/pion";
            String imagePath = src + game.getCurrentPlayer().getColorString() + ".png";
            Image origiImg = ImageIO.read(new File(imagePath));
            Image buttonImage = origiImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            namePlayer.setIcon(new ImageIcon(buttonImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    private void trade() {
        // TODO : A remplir
    }

    private void addcardsPanel() {
        if (cardsPanel != null) {
            remove(cardsPanel);
        }
        cardsPanel = new JPanel();
        cardsPanel.setLayout(null);
        cardsPanel.setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        String basePath = "src/main/resources/";
        for (int i = 0; i < game.getCurrentPlayer().getCardsDev().size(); i++) {
            ButtonImage b = new ButtonImage(basePath + "card.png", basePath + "card.png",
                300 + i * 100, 310, 2, this::useCard, null);
            cardsPanel.add(b);
        }
        JPanel self = this;
        cardsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                self.remove(cardsPanel);
                cardsPanel = null;
                repaint();
                revalidate();
            }
        });
        cardsPanel.setOpaque(false);
        add(cardsPanel, 10);
        repaint();
        revalidate();

    }

    private void useCard() {
        // TODO :
    }

    private void drawCard() {
        game.getCurrentPlayer().drawCard(game.getStack());
        if (cardPanel != null) {
            remove(cardPanel);
        }
        cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        String basePath = "src/main/resources/";
        ButtonImage b = new ButtonImage(basePath + "card.png", basePath + "card.png",
            600, 310, 2, this::useCard, null);
        cardPanel.add(b);
        JPanel self = this;
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                self.remove(cardPanel);
                cardPanel = null;
                repaint();
                revalidate();
            }
        });
        cardPanel.setOpaque(false);
        add(cardPanel, 10);
        repaint();
        revalidate();
    }

    private void createNamePlayer() throws IOException {
        String src = "src/main/resources/pion/pion";
        String imagePath = src + game.getCurrentPlayer().getColorString() + ".png";
        Image origiImg = ImageIO.read(new File(imagePath));
        int scale = (int) (40 / Resolution.divider());
        Image buttonImage = origiImg.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
        String text = game.getCurrentPlayer().getName().toUpperCase();
        namePlayer = new JLabel(" " + text, new ImageIcon(buttonImage), JLabel.CENTER);
        namePlayer.setVerticalTextPosition(JLabel.CENTER);
        namePlayer.setHorizontalTextPosition(JLabel.RIGHT);

        namePlayer.setFont(new Font("SansSerif", Font.BOLD, scale));
        double x = Resolution.calculateResolution(1000, 570)[0];
        double y = Resolution.calculateResolution(1000, 570)[1];
        namePlayer.setBounds((int) x, (int) y, (int) (scale * 10), (int) (scale * 1.2));
        add(namePlayer);
    }

    private void createPlayerPanel() {
        playersPanel = new JPanel();
        playersPanel.setLayout(null);
        int x = Resolution.calculateResolution(30, 10)[0];
        int y = Resolution.calculateResolution(30, 10)[1];
        playersPanel.setBounds(x, y, (int) (2000 / Resolution.divider()), (int) (100 / Resolution.divider()));
        for (int i = 0; i < game.getPlayers().size(); i++) {
            JLabel panel = new JLabel();
            try {
                String src = "src/main/resources/pion/pion";
                String imagePath = src + game.getPlayers().get(i).getColorString() + ".png";
                Image origiImg = ImageIO.read(new File(imagePath));
                int scale = (int) (40 / Resolution.divider());
                Image buttonImage = origiImg.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
                String text = game.getPlayers().get(i).getName().toUpperCase();
                panel = new JLabel(" " + text, new ImageIcon(buttonImage), JLabel.CENTER);
                panel.setVerticalTextPosition(JLabel.CENTER);
                panel.setHorizontalTextPosition(JLabel.RIGHT);
                panel.setFont(new Font("SansSerif", Font.BOLD, scale));
                double x1 = Resolution.calculateResolution(i * 200, 20)[0];
                double y1 = Resolution.calculateResolution(i * 200, 20)[1];
                panel.setBounds((int) x1, (int) y1, (int) (scale * 10), (int) (scale * 1.2));
            } catch (IOException e) {
                e.printStackTrace();
            }
            playersPanel.add(panel);
        }
        add(playersPanel);
    }
}
