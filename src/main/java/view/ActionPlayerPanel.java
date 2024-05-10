package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.io.IOException;

import model.App;
import model.Game;
import model.Player;
import others.ListPlayers;
import view.gamepanels.*;
import model.cards.DevelopmentCard;
import model.cards.KnightCard;
import model.cards.Monopoly;
import model.cards.RoadBuilding;
import model.cards.YearOfPlenty;
import network.NetworkObject;
import network.PlayerClient;
import network.NetworkObject.TypeObject;
import network.TradeObject;
import others.Constants;
import start.Main;
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
    private JPanel chat;
    private LogPanel logChat;
    private PlayersPanel playersPanel;
    private RollingDice dice;
    private boolean harboursDisabled = false;
    private boolean firstUpdate = true;

    public ActionPlayerPanel(App app) {
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        this.app = app;
        game = app.getGame();
        setLayout(null);
        try {
            createNamePlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOpaque(false);
        initializeRollingDicePanel();
        initializeTradeButtonPanel();
        initializeResourcesPanel();
        initializeShopPanel(game);
        initializeDeckPanel();
        initializeChat();
        initializeLogChat();
        //createPlayerPanel();
        createEndTurnButton();
        setVisible(true);
    }

    public TradePanel getTradePanel() {
        return tradePanel;
    }

    public RollingDice getRollingDice() {
        return dice;
    }

    public ResourcesPanel getResourcesPanel() {
        return resourcesPanel;
    }

    public JPanel getChat() {
        return chat;
    }

    private void initializeRollingDicePanel() {
        int xCoord = Resolution.calculateResolution(1108, 440)[0];
        int yCoord = Resolution.calculateResolution(1108, 440)[1];

        dice = new RollingDice(game, app.hasD20());
        dice.setBounds(xCoord, yCoord, (int) (205 / Resolution.divider()),
                (int) (300 / Resolution.divider()));
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
        showTradePanel(null);
    }

    private void initializeChat() {
        int xCoord = Resolution.calculateResolution(650, 200)[0];
        int yCoord = Resolution.calculateResolution(750, 200)[1];

        chat = new ChatPanel(this);
        chat.setVisible(true);
        chat.setBounds(xCoord, yCoord, (int) (400 / Resolution.divider()),
                (int) (400 / Resolution.divider()));
        add(chat);
    }

    private void initializeLogChat() {
        int xCoord = Resolution.calculateResolution(850, 200)[0];
        int yCoord = Resolution.calculateResolution(750, 200)[1];

        logChat = new LogPanel(this);
        logChat.setVisible(true);
        logChat.setBounds(xCoord, yCoord, (int) (400 / Resolution.divider()),
                (int) (400 / Resolution.divider()));
        add(logChat);
    }

    private JFrame getMainFrame() {
        Container current = this;
        while (current.getParent() != null) {
            current = current.getParent();
            if (current instanceof JFrame) {
                return (JFrame) current;
            }
        }
        return null; // Si le JFrame n'est pas trouvé (ce qui ne devrait pas arriver)
    }

    public void showTradePanel(TradeObject tradeObject) {
        JFrame mainFrame = getMainFrame();
        JLayeredPane layeredPane = mainFrame.getLayeredPane();
        ListPlayers listPlayers = game.getPlayers();
        if (tradeObject == null) {
            tradePanel = new TradePanel(listPlayers, resourcesPanel, this);
        } else {
            tradePanel = new TradePanel(tradeObject, listPlayers, resourcesPanel,
                    game.getPlayerClient(), this);
        }
        layeredPane.add(tradePanel, JLayeredPane.MODAL_LAYER);
        tradePanel.setVisible(true);
        setComponentsEnabled(false);
        repaint();
    }

    public void setComponentsEnabled(boolean enabled) {
        for (Component comp : this.getComponents()) {
            if (!(comp.equals(endTurn) || comp.equals(tradeButtonPanel))) {
                comp.setEnabled(enabled);
            }
        }
    }

    private void initializeResourcesPanel() {
        int xCoord = Resolution.calculateResolution(180, 620)[0];
        int yCoord = Resolution.calculateResolution(180, 620)[1];
        resourcesPanel = new ResourcesPanel(game);
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
        tradeButtonPanel = new TradeButtonPanel(this::initializeTradePanel);
        tradeButtonPanel.setVisible(true);
        tradeButtonPanel.setBounds(xCoord, yCoord, (int) (185 / Resolution.divider()),
                (int) (185 / Resolution.divider()));
        add(tradeButtonPanel);
        tradeButtonPanel.setOpaque(false);
    }

    private void createEndTurnButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "endTurn.png", basePath + "endTurn.png",
                960, 600, 1.5, this::changeTurn, null);
        add(endTurn);
    }

    private void changeTurn() {
        game.serverEndTurn();
        game.checkIfTradeEventActive();
        update();
    }

    private void addCardsPanel() {
        if (game.getBlankTurn()) {
            return;
        }
        if (cardsPanel != null) {
            remove(cardsPanel);
        }
        Player player;
        if (Main.hasServer()) {
            player = game.getPlayerClient();
        } else {
            player = game.getCurrentPlayer();
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
                    300 + i * 100, 250, 1.5, () -> useCard(card.getClass().getSimpleName()), null);
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
        revalidate();
        repaint();
    }

    public void removeCardsPanel() {
        this.remove(cardsPanel);
        cardsPanel = null;
        repaint();
    }

    private void useKnight() {
        removeCardsPanel();
        Player p = game.getCurrentPlayer();
        ArrayList<DevelopmentCard> cards = p.getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof KnightCard) {
                cards.remove(i);
                p.incrementKnights();
                break;
            }
        }
        game.setThiefMode(true);
    }

    private void useMonopoly() {
        removeCardsPanel();
        ArrayList<DevelopmentCard> cards = game.getCurrentPlayer().getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof Monopoly) {
                cards.remove(i);
                break;
            }
        }
        game.setMonoWaiting(true);
    }

    private void useRoadBuilding() {
        removeCardsPanel();
        ArrayList<DevelopmentCard> cards = game.getCurrentPlayer().getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof RoadBuilding) {
                cards.remove(i);
                break;
            }
        }
        game.getCurrentPlayer().setFreeRoad(2);
    }

    private void useYearOfPlenty() {
        removeCardsPanel();
        ArrayList<DevelopmentCard> cards = game.getCurrentPlayer().getCardsDev();
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof YearOfPlenty) {
                cards.remove(i);
                break;
            }
        }
        game.setYearOfPlenty(2);
    }

    private void useCard(String s) {
        switch (s) {
            case "KnightCard": useKnight(); break;
            case "Monopoly": useMonopoly(); break;
            case "YearOfPlenty": useYearOfPlenty(); break;
            case "RoadBuilding": useRoadBuilding();
            default:
        }
    }

    private String cardImageUrl(DevelopmentCard card) {
        if (card instanceof KnightCard) {
            return "cards/knight.png";
        } else if (card instanceof Monopoly) {
            return "cards/monopoly.png";
        } else if (card instanceof RoadBuilding) {
            return "cards/roadbuilding.png";
        } else if (card instanceof YearOfPlenty) {
            return "cards/yearofplenty.png";
        } else {
            return "cards/point.png";
        }
    }

    public void drawCardServer() {
        game.getCurrentPlayer().drawCard(game.getStack());
    }

    private void drawCard() {
        if (!game.canDraw()) {
            return;
        }
        game.getCurrentPlayer().drawCard(game.getStack());
        if (Main.hasServer() && game.getCurrentPlayer() instanceof PlayerClient) {
            try {
                PlayerClient player = game.getPlayerClient();
                NetworkObject gameObject;
                gameObject = new NetworkObject(TypeObject.Message, "DrawCard", player.getId(), null);
                player.getOut().writeUnshared(gameObject);
                player.getOut().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                600, 250, 1.5, () -> { }, null);
        cardPanel.add(b);
        JPanel self = this;
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override

            public void mouseClicked(MouseEvent e) {
                self.remove(cardPanel);
                cardPanel = null;
                revalidate();
                repaint();
            }
        });
        cardPanel.setOpaque(false);
        add(cardPanel, 0);
        updateShopPanel();
        revalidate();
        repaint();
    }

    /**
     * Function in charge of creating the little display of the player's name
     * and color icon just above the "Fin de Tour" button.
     * @throws IOException if perhaps the image getting fails
     */
    private void createNamePlayer() throws IOException {
        Player player = getPlayerFromGame();
        String src = "src/main/resources/pion/pion";
        String imagePath = src + player.getColorString() + ".png";
        Image origiImg = ImageIO.read(new File(imagePath));
        int scale = (int) (40 / Resolution.divider());
        Image buttonImage = origiImg.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
        String text = player.getName().toUpperCase();
        namePlayer = new JLabel(" " + text, new ImageIcon(buttonImage), JLabel.CENTER);
        namePlayer.setVerticalTextPosition(JLabel.CENTER);
        namePlayer.setHorizontalTextPosition(JLabel.RIGHT);

        namePlayer.setFont(new Font("SansSerif", Font.BOLD, scale));
        double x = Resolution.calculateResolution(1000, 570)[0];
        double y = Resolution.calculateResolution(1000, 570)[1];
        namePlayer.setBounds((int) x, (int) y, (int) (scale * 10), (int) (scale * 1.2));
        add(namePlayer);
    }

    public void createPlayerPanel() {
        playersPanel = new PlayersPanel(game);
        add(playersPanel);
    }

    public void update() {
        Player currentPlayer = game.getCurrentPlayer();

        if (!Main.hasServer()) {
            resourcesPanel.updateResourceLabels(currentPlayer);
            namePlayer.setText(" " + game.getCurrentPlayer().getName().toUpperCase());
        } else {
            resourcesPanel.updateResourceLabels(game.getPlayerClient());
        }
        if (firstUpdate) {
            try {
                Player player = getPlayerFromGame();
                String src = "src/main/resources/pion/pion";
                String imagePath = src + player.getColorString() + ".png";
                Image origiImg = ImageIO.read(new File(imagePath));
                Image buttonImage = origiImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                namePlayer.setIcon(new ImageIcon(buttonImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Main.hasServer()) {
                firstUpdate = false;
            }
        }

        updateTurn();

        if (playersPanel != null) {
            playersPanel.update(game);
        }
        revalidate();
        repaint();
    }

    /**
     * Gets the player according to the game mode (network or local).
     * @return The Player object representing the player in front of the screen
     */
    public Player getPlayerFromGame() {
        Player player;
        if (Main.hasServer()) {
            player = game.getPlayerClient();
        } else {
            player = game.getCurrentPlayer();
        }
        return player;
    }

    public void updateShopPanel() {
        shopPanel.updateEnablePanel(game);
    }

    public void updateTurn() {
        if (Main.hasServer()) {
            if (game.isMyTurn()) {
                updateShopPanel();
                tradeButtonPanel.getButton().setEnabled(!game.isInBeginningPhase()
                        && game.getCurrentPlayer().hasThrowDices());
                if (game.canPass()) {
                    endTurn.setEnabled(true);
                } else {
                    endTurn.setEnabled(false);
                }
            } else {
                dice.setButtonIsOn(false);
                shopPanel.setEnabledPanel(false);
                endTurn.setEnabled(false);
                tradeButtonPanel.getButton().setEnabled(false);
            }
        }
    }

    public App getApp() {
        return app;
    }

    public LogPanel getLogChat() {
        return (LogPanel) logChat;
    }

    public boolean isHarboursDisabled() {
        return harboursDisabled;
    }

    public void setHarboursDisabled(boolean harboursDisabled) {
        this.harboursDisabled = harboursDisabled;
    }
}
