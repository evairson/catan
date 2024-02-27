package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.lang.*;
import java.util.ArrayList;
import java.io.IOException;

import model.App;
import model.Game;
import model.Player;
import view.gamepanels.DeckPanel;
import view.gamepanels.ResourcesPanel;
import view.gamepanels.ShopPanel;
import view.gamepanels.TradePanel;
import model.cards.DevelopmentCard;
import model.cards.KnightCard;
import model.cards.Monopoly;
import model.cards.ProgressCard;
import others.Constants;
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
    private App app;
    private Game game;
    private ResourcesPanel resourcesPanel;
    private ShopPanel shopPanel;
    private TradePanel tradePanel;
    private DeckPanel deckPanel;
    private Animation animate = new Animation();

    private JPanel cardsPanel;
    private JPanel cardPanel;
    private JPanel playersPanel;
    private RollingDice dice;

    private boolean cardPlayed = false;

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
        initializeTradePanel();
        initializeResourcesPanel();
        initializeShopPanel(game);
        initializeDeckPanel();
        createButton();

    }

    public RollingDice getRollingDice() {
        return dice;
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

    private void initializeTradePanel() {
        int xCoord = Resolution.calculateResolution(50, 560)[0];
        int yCoord = Resolution.calculateResolution(50, 560)[1];
        tradePanel = new TradePanel(this::trade);
        tradePanel.setVisible(true);
        tradePanel.setBounds(xCoord, yCoord, (int) (185 / Resolution.divider()),
                (int) (185 / Resolution.divider()));
        add(tradePanel);
        tradePanel.setOpaque(false);
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn, null);
        tradeButton = new ButtonImage(basePath + "tradeButton.png", basePath + "tradeButton.png",
                50, 560, 5, this::trade, null);

        card = new ButtonImage(basePath + "card.png", basePath + "card.png",
                770, 560, 3, null, null);

        //
        // city = new ButtonImage(basePath + "building/city.png", basePath +
        // "building/city.png",
        // 1150, 20, 2, cityRunnable, null);
        //
        //
        // colony = new ButtonImage(basePath + "building/colony.png", basePath +
        // "building/colony.png",
        // 1150, 130, 2, colonyRunnable, null);
        //
        //
        // road = new ButtonImage(basePath + "building/road.png", basePath +
        // "building/road.png",
        // 1150, 220, 2, roadRunnable, null);

        /*
         * plus = new ButtonImage(basePath + "plus.png", basePath + "plus.png",
         * 1160, 310, 8, null, null);
         */

        // add(city);
        // add(colony);
        // add(road);

        // add(plus);

        add(tradeButton);
        add(endTurn);
        // add(card);
    }

    private void changeTurn() {
        game.endTurn();
        cardPlayed = false;
        update();
    }

    private void trade() {
        Container contentPane = this.app.getGameWindow().getContentPane();
        CardLayout layout = this.app.getGameWindow().getLayout();
        app.createTradePanel();
        layout.show(contentPane, "tradePanel");
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
            DevelopmentCard card = game.getCurrentPlayer().getCardsDev().get(i);
            String stringCard = cardImageUrl(card);
            System.out.println(card.getClass().getSimpleName());
            ButtonImage b = new ButtonImage(basePath + stringCard, basePath + stringCard,
                    300 + i * 100, 250, 1.5,
                switch (card.getClass().getSimpleName()) {
                    case "KnightCard": yield (this::useKnight);
                    case "ProgressCard": yield (this::useMonopoly);
                    default: yield null;
                }, null);
            cardsPanel.add(b);
        }
        cardsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeCardsPanel();
            }
        });
        cardsPanel.setOpaque(false);
        add(cardsPanel, 0);
        repaint();
        revalidate();
    }

    public void removeCardsPanel() {
        this.remove(cardsPanel);
        cardsPanel = null;
        repaint();
        revalidate();
    }

    private void useKnight() {
        removeCardsPanel();
        cardPlayed = true;
        ArrayList<DevelopmentCard> cards = game.getCurrentPlayer().getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof KnightCard) {
                cards.remove(i);
                break;
            }
        }
        game.getBoard().setThiefMode(true);
        System.out.println("knigh");
    }

    private void useMonopoly() {
        removeCardsPanel();
        cardPlayed = true;
        ArrayList<DevelopmentCard> cards = game.getCurrentPlayer().getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof Monopoly) {
                cards.remove(i);
                break;
            }
        }
        game.getBoard().setWaitingChoice(true);
        System.out.println("progress");
    }

    private void useCard() {
        //
    }

    private String cardImageUrl(DevelopmentCard card) {
        if (card instanceof KnightCard) {
            return "cards/knight.png";
        } else if (card instanceof ProgressCard) {
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
        ArrayList<DevelopmentCard> devCards = game.getCurrentPlayer().getCardsDev();
        int last = devCards.size();
        String card = cardImageUrl(devCards.get(last - 1));
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
        //dice.newPlayer(currentPlayer);
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
