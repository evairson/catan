package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.*;

import java.io.IOException;

import model.Game;
import view.gamepanels.ResourcesPanel;
import view.gamepanels.ShopPanel;
import view.gamepanels.TradePanel;
import view.utilities.Animation;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class ActionPlayerPanel extends JPanel {
    private ButtonImage endTurn;
    private ButtonImage tradeButton;
    private ButtonImage city;
    private ButtonImage colony;
    private ButtonImage road;

    private ButtonImage plus;

    private ButtonImage card;

    private JLabel namePlayer;
    private Game game;
    private ResourcesPanel resourcesPanel;
    private ShopPanel shopPanel;
    private TradePanel tradePanel;
    private Animation animate = new Animation();


    public ActionPlayerPanel(Game game) {
        this.game = game;
        setLayout(null);
        setOpaque(true);
        try {
            createNamePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeTradePanel();
        initializeResourcesPanel();
        initializeShopPanel();
        createButton();
    }

    private void initializeShopPanel() {
        int xCoord = Resolution.calculateResolution(1250, 20)[0];
        int yCoord = Resolution.calculateResolution(1250, 20)[1];
        MouseAdapter animMouse = new MouseAdapter() {
            private final int length = (int) (200 / Resolution.divider());
            @Override
            public void mouseEntered(MouseEvent e) {
                animate.jPanelXLeft(xCoord, xCoord - length, 2, 1, shopPanel);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                animate.jPanelXRight(xCoord - length, xCoord, 2, 1, shopPanel);
            }
        };
        shopPanel = new ShopPanel(animMouse);
        shopPanel.setVisible(true);
        shopPanel.setBounds(xCoord, yCoord, (int) (150 / Resolution.divider()),
                (int) (710 / Resolution.divider()));
        shopPanel.addMouseListener(animMouse);
        add(shopPanel);
    }
    private void initializeResourcesPanel() {
        int xCoord = Resolution.calculateResolution(200, 650)[0];
        int yCoord = Resolution.calculateResolution(200, 650)[1];
        MouseAdapter animMouse = new MouseAdapter() {
            private final int length = (int) (200 / Resolution.divider());
            @Override
            public void mouseEntered(MouseEvent e) {
                animate.jPanelYUp(yCoord, yCoord - length, 2, 1, resourcesPanel);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                animate.jPanelYDown(yCoord - length, yCoord, 2, 1, resourcesPanel);
            }
        };
        resourcesPanel = new ResourcesPanel(animMouse);
        resourcesPanel.setVisible(true);
        resourcesPanel.setBounds(xCoord, yCoord, (int) (975 / Resolution.divider()),
                (int) (210 / Resolution.divider()));
        resourcesPanel.addMouseListener(animMouse);
        add(resourcesPanel);
    }

    private void initializeTradePanel() {
        int xCoord = Resolution.calculateResolution(50, 560)[0];
        int yCoord = Resolution.calculateResolution(50, 560)[1];

        tradePanel = new TradePanel(this::trade);
        tradePanel.setVisible(true);
        tradePanel.setBounds(xCoord, yCoord, (int) (185 / Resolution.divider()),
                (int) (185 / Resolution.divider()));
        add(tradePanel);
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn, null);

        card = new ButtonImage(basePath + "card.png", basePath + "card.png",
        770, 560, 3, null, null);

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
