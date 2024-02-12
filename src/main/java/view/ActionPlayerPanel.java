package view;

import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.*;

import java.io.IOException;

import model.Game;
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
    private Game game;


    public ActionPlayerPanel(Game game) {
        this.game = game;
        setLayout(null);
        setOpaque(true);
        try {
            createNamePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createButton();
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn);
        tradeButton = new ButtonImage(basePath + "tradeButton.png", basePath + "tradeButton.png",
                50, 560, 5, this::trade);

        wood = new ButtonImage(basePath + "resources/wood.png", basePath + "resources/wood.png",
                200, 550, 2, null);
        ore = new ButtonImage(basePath + "resources/ore.png", basePath + "resources/ore.png",
                300, 550, 2, null);
        clay = new ButtonImage(basePath + "resources/clay.png", basePath + "resources/clay.png",
                400, 550, 2, null);
        wheat = new ButtonImage(basePath + "resources/wheat.png", basePath + "resources/wheat.png",
                500, 550, 2, null);
        wool = new ButtonImage(basePath + "resources/wool.png", basePath + "resources/wool.png",
             600, 550, 2, null);

        card = new ButtonImage(basePath + "card.png", basePath + "card.png",
        770, 560, 3, null);


        city = new ButtonImage(basePath + "building/city.png", basePath + "building/city.png",
        1150, 20, 2, null);
        colony = new ButtonImage(basePath + "building/colony.png", basePath + "building/colony.png",
        1150, 130, 2, null);
        road = new ButtonImage(basePath + "building/road.png", basePath + "building/road.png",
        1150, 220, 2, null);

        plus = new ButtonImage(basePath + "plus.png", basePath + "plus.png",
        1160, 310, 8, null);

        add(wood);
        add(wool);
        add(ore);
        add(clay);
        add(wheat);

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
        repaint();
    }

    private void trade() {
        // A remplir
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
}
