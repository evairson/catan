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
import model.Player;
import view.gamepanels.*;
import model.cards.DevelopmentCard;
import model.cards.KnightCard;
import model.cards.ProgessCard;
import others.Constants;
import view.utilities.Animation;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class ActionPlayerPanel extends JPanel {
    private ButtonImage endTurn;
    private JLabel namePlayer;
    private App app;
    private Game game;
    private ResourcesPanel resourcesPanel;
    private ShopPanel shopPanel;
    private TradeButtonPanel tradeButtonPanel;
    private TradePanel tradePanel;
    private DeckPanel deckPanel;
    private Animation animate = new Animation();
    private JPanel cardsPanel;
    private JPanel cardPanel;
    private JPanel playersPanel;
    private RollingDice dice;

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
        initializeRollingDicePanel();
        initializeTradeButtonPanel();
        initializeTradePanel();
        initializeResourcesPanel();
        initializeShopPanel(game);
        initializeDeckPanel();
        createButton();

    }

    private void initializeRollingDicePanel() {
        int xCoord = Resolution.calculateResolution(1108, 440)[0];
        int yCoord = Resolution.calculateResolution(1108, 440)[1];

        dice = new RollingDice(game);
        dice.setBounds(xCoord, yCoord, (int) (205 / Resolution.divider()),
                (int) (150 / Resolution.divider()));
        add(dice);
        dice.setOpaque(false);
    }

    private void initializeDeckPanel() {
        int xCoord = Resolution.calculateResolution(750, 620)[0];
        int yCoord = Resolution.calculateResolution(750, 620)[1];

        deckPanel = new DeckPanel(this::addCardsPanel);
        MouseAdapter animMouse = new MouseAdapter() {
            private final int length = (int) (200 / Resolution.divider());

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!deckPanel.isMouseInside()) {
                    animate.jPanelYUp(yCoord, yCoord - length, 2, 1, deckPanel);
                    deckPanel.setMouseInside(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Point mousePos = SwingUtilities.convertPoint(e.getComponent(),
                            e.getPoint(), deckPanel);
                    if (!deckPanel.contains(mousePos)) {
                        animate.jPanelYDown(yCoord - length, yCoord, 2, 1, deckPanel);
                        deckPanel.setMouseInside(false);
                    }
                });
            }
        };
        deckPanel.setOpaque(false);
        deckPanel.setAnimMouse(animMouse);
        deckPanel.setVisible(true);
        deckPanel.setBounds(xCoord, yCoord, (int) (195 / Resolution.divider()),
                (int) (500 / Resolution.divider()));
        deckPanel.addMouseListener(animMouse);
        add(deckPanel);
    }

    private void initializeShopPanel(Game game) {
        int xCoord = Resolution.calculateResolution(1220, 20)[0];
        int yCoord = Resolution.calculateResolution(1220, 20)[1];
        shopPanel = new ShopPanel(this::drawCard, game);
        MouseAdapter animMouse = new MouseAdapter() {
            private final int length = (int) (200 / Resolution.divider());

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!shopPanel.isMouseInside()) {
                    animate.jPanelXLeft(xCoord, xCoord - length, 2, 1, shopPanel);
                    shopPanel.setMouseInside(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Point mousePos = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), shopPanel);
                    if (!shopPanel.contains(mousePos)) {
                        animate.jPanelXRight(xCoord - length, xCoord, 2, 1, shopPanel);
                        shopPanel.setMouseInside(false);
                    }
                });
            }
        };
        shopPanel.setOpaque(false);
        shopPanel.setAnimMouse(animMouse);
        shopPanel.setVisible(true);
        shopPanel.setBounds(xCoord, yCoord, (int) (400 / Resolution.divider()),
                (int) (840 / Resolution.divider()));
        shopPanel.addMouseListener(animMouse);
        add(shopPanel);
    }

    private void initializeTradePanel() {
        int xCoord = Resolution.calculateResolution(480, 120)[0];
        int yCoord = Resolution.calculateResolution(480, 120)[1];
        tradePanel = new TradePanel(xCoord, yCoord, (int) (800 / Resolution.divider()),
                (int) (600 / Resolution.divider()));
        tradePanel.setOpaque(true);
        tradePanel.setBounds(xCoord, yCoord, (int) (800 / Resolution.divider()),
                (int) (600 / Resolution.divider()));
        tradePanel.setVisible(false);
        add(tradePanel);
    }

    private void initializeResourcesPanel() {
        int xCoord = Resolution.calculateResolution(180, 620)[0];
        int yCoord = Resolution.calculateResolution(180, 620)[1];
        resourcesPanel = new ResourcesPanel();
        createPlayerPanel();
        MouseAdapter animMouse = new MouseAdapter() {
            private final int length = (int) (200 / Resolution.divider());

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!resourcesPanel.isMouseInside()) {
                    animate.jPanelYUp(yCoord, yCoord - length, 2, 1, resourcesPanel);
                    resourcesPanel.setMouseInside(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Point mousePos = SwingUtilities.convertPoint(e.getComponent(),
                            e.getPoint(), resourcesPanel);
                    if (!resourcesPanel.contains(mousePos)) {
                        animate.jPanelYDown(yCoord - length, yCoord, 2, 1, resourcesPanel);
                        resourcesPanel.setMouseInside(false);
                    }
                });
            }
        };
        resourcesPanel.setOpaque(false);
        resourcesPanel.setAnimMouse(animMouse);
        resourcesPanel.setVisible(true);
        resourcesPanel.setBounds(xCoord, yCoord, (int) (1040 / Resolution.divider()),
                (int) (500 / Resolution.divider()));
        resourcesPanel.addMouseListener(animMouse);
        add(resourcesPanel);
    }

    private void initializeTradeButtonPanel() {
        int xCoord = Resolution.calculateResolution(50, 560)[0];
        int yCoord = Resolution.calculateResolution(50, 560)[1];
        tradeButtonPanel = new TradeButtonPanel(this::trade);
        tradeButtonPanel.setVisible(true);
        tradeButtonPanel.setBounds(xCoord, yCoord, (int) (185 / Resolution.divider()),
                (int) (185 / Resolution.divider()));
        add(tradeButtonPanel);
        tradeButtonPanel.setOpaque(false);
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn, null);
        add(endTurn);
    }

    private void changeTurn() {
        game.endTurn();
        update();
    }

    private void trade() {
        tradePanel.setVisible(true);
        int length = (int) (200 / Resolution.divider());
        int yCoord = resourcesPanel.getY();
        animate.jPanelYUp(yCoord, yCoord - length, 2, 1, resourcesPanel);
        resourcesPanel.setLabelTradeVisible(true);
        length = (int) (400 / Resolution.divider());
        int xCoord = tradeButtonPanel.getX();
        animate.jPanelXLeft(xCoord, xCoord - length, 2, 1, tradeButtonPanel);
    }

    private void addCardsPanel() {
        if (cardsPanel != null) {
            remove(cardsPanel);
        }
        cardsPanel = new JPanel();
        cardsPanel.setLayout(null);
        cardsPanel.setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        String basePath = "src/main/resources/";
        for (int i = 0; i < game.getCurrentPlayer().getCardsDev().size(); i++) {
            String card = cardImageUrl(game.getCurrentPlayer().getCardsDev().get(i));
            ButtonImage b = new ButtonImage(basePath + card, basePath + card,
                    300 + i * 100, 250, 1.5, this::useCard, null);
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
        add(cardsPanel, 0);
        repaint();
        revalidate();
    }

    private void useCard() {
        // TODO :
    }

    private String cardImageUrl(DevelopmentCard card) {
        if (card instanceof KnightCard) {
            return "cards/knight.png";
        } else if (card instanceof ProgessCard) {
            return "cards/progress.png";
        } else {
            return "cards/point.png";
        }
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
        int last = game.getCurrentPlayer().getCardsDev().size();
        String card = cardImageUrl(game.getCurrentPlayer().getCardsDev().get(last - 1));
        ButtonImage b = new ButtonImage(basePath + card, basePath + card,
                600, 250, 1.5, this::useCard, null);
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
        add(cardPanel, 0);
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
                Boolean player = game.getPlayers().get(i) == game.getCurrentPlayer();
                String textUnderligne = player ? "<html><u> " + text + "</u></html>" : " " + text;
                panel = new JLabel(textUnderligne, new ImageIcon(buttonImage), JLabel.CENTER);
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

    public void update() {
        Player currentPlayer = game.getCurrentPlayer();
        dice.newPlayer(currentPlayer);
        resourcesPanel.updateResourceLabels(currentPlayer);

        namePlayer.setText(" " + game.getCurrentPlayer().getName().toUpperCase());
        for (int i = 0; i < game.getPlayers().size(); i++) {
            String text = game.getPlayers().get(i).getName().toUpperCase();
            Boolean player = game.getPlayers().get(i) == game.getCurrentPlayer();
            String textUnderligne = player ? "<html><u>" + text + "</u></html>" : " " + text;
            ((JLabel) playersPanel.getComponents()[i]).setText(textUnderligne);
        }
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

}
