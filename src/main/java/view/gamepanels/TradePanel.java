package view.gamepanels;

import model.App;
import model.Player;
import model.bots.Bot;
import network.NetworkObject;
import network.NetworkObject.TypeObject;
import network.PlayerClient;
import network.TradeObject;
import model.buildings.Colony;
import model.buildings.Harbor;
import model.buildings.SpecializedHarbor;
import model.tiles.TileVertex;
import others.Constants;
import others.ListPlayers;
import start.Main;
import view.ActionPlayerPanel;
import view.GameWindow;
import view.TileType;
import view.utilities.ButtonImage;
import view.utilities.ImgService;
import view.utilities.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Random;

public class TradePanel extends JPanel {
    private ActionPlayerPanel actionPlayerPanel;
    private Image backgroundImage;
    private ButtonImage[] playerOneButtons = new ButtonImage[5];
    private ButtonImage[] playerTwoButtons = new ButtonImage[5];
    private JLabel[] playerOneLabels = new JLabel[5];
    private JLabel[] playerTwoLabels = new JLabel[5];

    private final String[] resourceNames = {"clay", "ore", "wheat", "wood", "wool"};
    private final int[] buttonYPositions = {378, 312, 446, 245, 515};
    private ListPlayers listPlayers;
    private Player selectedPlayer;
    private Player player;
    private JLabel selectedPlayerLabel;
    private JLabel selectedPlayerLabelIcon;
    private ButtonImage proposeButton;
    private ButtonImage acceptButton;
    private ButtonImage declineButton;
    private Image playerIcon;
    private ButtonImage bankTradeButton;
    private Map<String, TileType> resourceNameToTileType = new HashMap<>();
    private HashMap<TileType, Integer> resourcesRequested;
    private HashMap<TileType, Integer> resourcesOffered;
    private ResourcesPanel resourcesPanel;

    private boolean isOfferedDouble;
    private boolean isRequestedDouble;
    private boolean isBank = false;
    private boolean trader = false;

    private static boolean tradeAlea;

    public static void setTradeAlea(boolean b) {
        tradeAlea = b;
    }

    public TradePanel(ListPlayers listPlayers, ResourcesPanel resourcesPanel,
                      ActionPlayerPanel actionPlayerPanel) {
        this.actionPlayerPanel = actionPlayerPanel;
        resourcesOffered = new HashMap<>();
        resourcesRequested = new HashMap<>();
        this.listPlayers = listPlayers;
        this.resourcesPanel = resourcesPanel;
        isOfferedDouble = false;
        isRequestedDouble = false;
        initializeSelectedPlayerLabel();
        initializeResourceNameMap();
        setLayout(null);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        createResourceButtons(false);
        createTradeButtons();
        createPlayersButtons();
        initializeSelectedPlayerImage();
        updateProposeButtonState();
        initializeReturnButton();
    }

    public TradePanel(TradeObject tradeObject, ListPlayers listPlayers,
        ResourcesPanel resourcesPanel, Player player, ActionPlayerPanel actionPlayerPanel) {
        this.actionPlayerPanel = actionPlayerPanel;
        trader = true;
        this.player = player;
        this.listPlayers = listPlayers;
        this.resourcesPanel = resourcesPanel;
        this.resourcesOffered = TradeObject.toTileType(tradeObject.getResourcesRequested());
        this.resourcesRequested = TradeObject.toTileType(tradeObject.getResourcesOffered());
        this.isOfferedDouble = tradeObject.isOfferedDouble();
        this.isRequestedDouble = tradeObject.isrequestedDouble();
        initializeSelectedPlayerLabel();
        initializeResourceNameMap();
        setLayout(null);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        createResourceButtons(false);

        createTradeButtons();
        //createPlayersButtons();
        initializeSelectedPlayerImage();
        System.out.println(playerOneLabels);
        updateProposeButtonState();
        actionPlayerButton(listPlayers.getCurrentPlayer());
        bankTradeButton.setEnabled(false);
        acceptButton.setEnabled(canSelectedPlayerFulfillRequest());
        declineButton.setEnabled(true);
        repaint();
    }

    // -------- Fonctions affichant les boutons ressources et leurs labels respectifs -------- //

    private void createResourceButtons(boolean isBank) {
        String basePath = "src/main/resources/resources/";
        for (int i = 0; i < resourceNames.length; i++) {
            playerOneButtons[i] = createButtonImage(basePath + resourceNames[i] + ".png",
                    357, buttonYPositions[i]);
            playerOneLabels[i] = createCounterLabel(400, buttonYPositions[i],
                    playerOneButtons[i].getHeight());

            playerTwoButtons[i] = createButtonImage(basePath + resourceNames[i] + ".png",
                    888, buttonYPositions[i]);
            playerTwoLabels[i] = createCounterLabel(865, buttonYPositions[i],
                    playerTwoButtons[i].getHeight());
            TileType type = resourceNameToTileType.get(resourceNames[i]);
            configurePlayerOneButton(playerOneButtons[i], playerOneLabels[i], type);
            configurePlayerTwoButton(playerTwoButtons[i], playerTwoLabels[i]);
            if (trader) {
                updateCounterLabel(i);
            } else {
                resourcesOffered.put(resourceNameToTileType.get(resourceNames[i]), 0);
                resourcesRequested.put(resourceNameToTileType.get(resourceNames[i]), 0);
            }
        }
    }
    private JLabel createCounterLabel(int x, int y, int height) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label;
        label = new JLabel("0", SwingConstants.CENTER);
        label.setForeground(Color.RED);
        label.setBounds(coords[0], coords[1], 20, height);
        int scale = (int) (32 / Resolution.divider());
        label.setFont(new Font("SansSerif", Font.BOLD, scale));
        add(label);
        return label;
    }

    private void updateCounterLabel(int i) {
        TileType resourceType = resourceNameToTileType.get(resourceNames[i]);
        playerOneLabels[i].setText(String.valueOf(resourcesRequested.get(resourceType)));
        playerTwoLabels[i].setText(String.valueOf(resourcesOffered.get(resourceType)));
    }

    // -------- Fonctions configurant les boutons ressources et leurs labels respectifs -------- //

    private void configurePlayerOneButton(ButtonImage button, JLabel counterLabel,
                                          TileType resourceType) {
        Player currentPlayer = listPlayers.getCurrentPlayer();
        button.addActionListener(e -> {
            int currentValue = Integer.parseInt(counterLabel.getText());
            int playerResourceAmount;
            if (trader) {
                playerResourceAmount = player.getResources().getOrDefault(resourceType, 0);
            } else {
                playerResourceAmount = currentPlayer.getResources().getOrDefault(resourceType, 0);
            }
            if (currentValue < playerResourceAmount) {
                currentValue++;
                counterLabel.setText(String.valueOf(currentValue));
                updateProposeButtonState();
                updateAcceptButtonState();
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                    updateProposeButtonState();
                    updateAcceptButtonState();
                }
            }
        });
    }
    private void configurePlayerTwoButton(ButtonImage button, JLabel counterLabel) {
        button.addActionListener(e -> {
            int currentValue = Integer.parseInt(counterLabel.getText());
            currentValue++;
            counterLabel.setText(String.valueOf(currentValue));
            updateProposeButtonState();
            updateAcceptButtonState();
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                    updateProposeButtonState();
                    updateAcceptButtonState();
                }
            }
        });
    }
    private void gatherResourcesOffered() {
        resourcesOffered.clear();
        for (int i = 0; i < playerOneLabels.length; i++) {
            TileType resourceType = resourceNameToTileType.get(resourceNames[i]);
            int amount = Integer.parseInt(playerOneLabels[i].getText());
            if (amount > 0) {
                resourcesOffered.put(resourceType, amount);
            } else {
                resourcesOffered.put(resourceType, 0);
            }
        }
    }
    private void gatherResourcesRequested() {
        resourcesRequested.clear();
        for (int i = 0; i < playerTwoLabels.length; i++) {
            TileType resourceType = resourceNameToTileType.get(resourceNames[i]);
            int amount = Integer.parseInt(playerTwoLabels[i].getText());
            if (amount > 0) {
                resourcesRequested.put(resourceType, amount);
            } else {
                resourcesRequested.put(resourceType, 0);
            }
        }
    }

    // -------- Fonctions affichant les boutons d'échanges (Propose, Accept, Decline) -------- //

        // TODO : simplifier cette merde !
    private void createTradeButtons() {
        proposeButton = new ButtonImage("src/main/resources/proposeButton.png",
                "src/main/resources/proposeButton.png", 605,
                629, 0.69, this::proposeAction, null);
        acceptButton = new ButtonImage("src/main/resources/acceptButton.png",
                "src/main/resources/acceptButton.png", 320,
                629, 0.69, () -> acceptAction(isBank), null);
        declineButton = new ButtonImage("src/main/resources/refuseButton.png",
                "src/main/resources/refuseButton.png", 830,
                629, 0.69, this::declineAction, null);
        bankTradeButton = new ButtonImage("src/main/resources/bankButton.png",
                "src/main/resources/bankButton.png", 711,
                618, 0.45, this::bankTradeAction, null);
        add(bankTradeButton);
        add(proposeButton);
        add(acceptButton);
        add(declineButton);
        declineButton.setEnabled(false);
        acceptButton.setEnabled(false);
    }

    // -------- Fonctions traitant les mécanismes des boutons d'échange -------- //
    private boolean canSelectedPlayerFulfillRequest() {
        if (trader) {
            HashMap<TileType, Integer> selectedPlayerResources = player.getResources();
            for (Map.Entry<TileType, Integer> entry : resourcesRequested.entrySet()) {
                TileType requestedResource = entry.getKey();
                Integer requestedAmount = entry.getValue();
                if (selectedPlayerResources.getOrDefault(requestedResource, 0) < requestedAmount) {
                    System.out.println(requestedResource);
                    System.out.println(selectedPlayerResources.get(requestedResource));
                    return false;
                }
            }
            return true;
        }
        if (selectedPlayer == null) {
            return false;
        }
        HashMap<TileType, Integer> selectedPlayerResources = selectedPlayer.getResources();
        for (Map.Entry<TileType, Integer> entry : resourcesRequested.entrySet()) {
            TileType requestedResource = entry.getKey();
            Integer requestedAmount = entry.getValue();

            if (selectedPlayerResources.getOrDefault(requestedResource, 0) < requestedAmount) {
                return false;
            }
        }

        return true;
    }
    private void proposeAction() {
        gatherResourcesRequested();
        displayResources(resourcesRequested);

        gatherResourcesOffered();
        displayResources(resourcesOffered);

        boolean canFulfillRequest = canSelectedPlayerFulfillRequest();
        toggleTradeInterface(false);
        notifyOfferToPlayer(selectedPlayer);

        if (Main.hasServer() && !trader) {
            declineButton.setEnabled(false);
            proposeButton.setEnabled(false);
            acceptButton.setEnabled(false);
            bankTradeButton.setEnabled(false);
        } else {
            if (canFulfillRequest) {
                if (((Bot) selectedPlayer).acceptTrade(resourcesRequested, resourcesOffered)) {
                    performTrade(false);
                    actionPlayerPanel.getApp().addMessageColor("Votre trade a été accepté \n",
                            java.awt.Color.GREEN);
                }
            } else {
                actionPlayerPanel.getApp().addMessageColor("Votre trade a été refusé \n",
                        java.awt.Color.ORANGE);
            }
            closeTradePanel();
            //acceptButton.setEnabled(canFulfillRequest);
            //declineButton.setEnabled(true);
        }
    }
    private void bankTradeAction() {
        isBank = true;
        if (tradeAlea) {
            Random random = new Random();
            isRequestedDouble = random.nextBoolean();
        }
        updateAcceptButtonState();
        selectedPlayerLabelIcon.setVisible(false);
        selectedPlayerLabelIcon = null;
        initializeSelectedPlayerImage();
        selectedPlayerLabel.setText("<html><div style='text-align: center;'>"
                + "ÉCHANGE AVEC<br/> La Banque</div></html>");
    }
    public void notifyOfferToPlayer(Player player) {
        // TODO : Implémentez la notification pour le joueur sélectionné.
        // Cela peut être un changement de couleur, un message pop-up, etc.

        if (listPlayers.getCurrentPlayer() instanceof PlayerClient) {
            try {
                PlayerClient playerClient = (PlayerClient) listPlayers.getCurrentPlayer();
                int id = playerClient.getId();
                if (tradeAlea) {
                    Random random = new Random();
                    isOfferedDouble = random.nextBoolean();
                    isRequestedDouble = random.nextBoolean();
                }
                HashMap<String, Integer> offered = TradeObject.toString(resourcesOffered);
                HashMap<String, Integer> requested = TradeObject.toString(resourcesRequested);
                TradeObject tradeObject = new TradeObject(id, player.getId(), offered,
                    requested, isOfferedDouble, isRequestedDouble);
                NetworkObject object = new NetworkObject(TypeObject.Game, "trade", id, tradeObject);
                playerClient.getOut().writeUnshared(object);
                playerClient.getOut().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Problème de downCast");
        }
    }
    public void acceptAction(boolean isBank) {
        performTrade(isBank);
        closeTradePanel();
    }
    private void declineAction() {
        sendTradeExit(false);
        closeTradePanel();
    }

    /**
     * Send to the network the result of the trade.
     * @param b true if the trade succeded, false if not
     */
    private void sendTradeExit(boolean b) {
        if (Main.hasServer()) {
            try {
                PlayerClient playerClient = (PlayerClient) player;
                int id = playerClient.getId();
                String exit = b ? "Accept" : "Refuse";
                NetworkObject object = new NetworkObject(TypeObject.Game, "trade" + exit, id,
                        listPlayers.getCurrentPlayer().getId());
                playerClient.getOut().writeUnshared(object);
                playerClient.getOut().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void performTrade(boolean isBank) {
        if (isBank) {
            // Mise à jour des ressources pour une transaction avec la banque
            updateResources(listPlayers.getCurrentPlayer().getResources(), resourcesOffered, true, false);
            updateResources(listPlayers.getCurrentPlayer().getResources(), resourcesRequested,
                    false, isRequestedDouble);
        } else {
            // Transaction avec un autre joueur
            HashMap<TileType, Integer> selectedPlayerResources;
            if (trader) {
                selectedPlayerResources = player.getResources();
            } else {
                selectedPlayerResources = selectedPlayer.getResources();
            }
            // Mise à jour des ressources pour une transaction entre deux joueurs
            updateResources(listPlayers.getCurrentPlayer().getResources(), resourcesOffered, true, false);
            updateResources(selectedPlayerResources, resourcesOffered, false, isOfferedDouble);

            updateResources(listPlayers.getCurrentPlayer().getResources(), resourcesRequested,
                    false, isRequestedDouble);
            updateResources(selectedPlayerResources, resourcesRequested, true, false);
        }
        // Effacer les ressources demandées et offertes après la transaction
        resourcesRequested.clear();
        resourcesOffered.clear();

        App.getActionPlayerPanel().update();

        sendTradeExit(true);
    }

    /**
     * Met à jour les ressources d'un joueur en fonction des ressources offertes ou demandées.
     * @param playerResources Les ressources actuelles du joueur.
     * @param changes Les changements à appliquer (offertes/demandées).
     * @param subtract Si vrai, les ressources seront soustraites (offertes),
     *                 sinon elles seront ajoutées (demandées).
     * @param isDouble Est ce que les ressources doivent être doublés.
     */
    private void updateResources(HashMap<TileType, Integer> playerResources,
                                 HashMap<TileType, Integer> changes, boolean subtract, boolean isDouble) {

        changes.forEach((resource, amount) -> {
            int currentAmount = playerResources.getOrDefault(resource, 0);
            if (tradeAlea && !subtract) {
//                System.out.println("Mode trade event");
                if (isDouble) {
                    playerResources.put(resource, currentAmount + 2 * amount);
                }
            } else {
                playerResources.put(resource, subtract ? currentAmount - amount : currentAmount + amount);
            }
        });
    }

    private void toggleTradeInterface(boolean enable) {
        for (ButtonImage button : playerOneButtons) {
            button.setEnabled(enable);
        }
        for (ButtonImage button : playerTwoButtons) {
            button.setEnabled(enable);
        }
        proposeButton.setEnabled(enable);
    }
    private void updateProposeButtonState() {
        boolean isPlayerSelected = selectedPlayer != null;
        boolean hasValidResourceValues = checkIfAnyResourcesSelected(playerOneLabels)
                && checkIfAnyResourcesSelected(playerTwoLabels);
        proposeButton.setEnabled(isPlayerSelected && hasValidResourceValues && !trader);
    }
    private int getTotalSelectedResources(JLabel[] resourceLabels) {
        int total = 0;
        for (JLabel label : resourceLabels) {
            int value = Integer.parseInt(label.getText());
            total += value;
        }
        return total;
    }
    private boolean checkIfAnyResourcesSelected(JLabel[] labels) {
        for (JLabel label : labels) {
            if (Integer.parseInt(label.getText()) > 0) {
                return true;
            }
        }
        return false;
    }
    private void updateAcceptButtonState() {
        if (isBank) {
            acceptButton.setEnabled(isValidBankTrade());
        }
    }
    private List<Harbor> getCurrentPlayerHarbors() {
        List<Harbor> accessiblesHarbors = new ArrayList<>();
        Player currentPlayer = listPlayers.getCurrentPlayer();
        ArrayList<Colony> colonies = currentPlayer.getColony();

        for (Colony colony : colonies) {
            TileVertex colonyVertex = colony.getVertex();
            Harbor connectedHarbor = colonyVertex.getHarbor();
            if (connectedHarbor != null) {
                accessiblesHarbors.add(connectedHarbor);
            }
        }
        return accessiblesHarbors;
    }

    private boolean isValidBankTrade() {
        List<Harbor> currentPlayerPorts = getCurrentPlayerHarbors();
        // Définir le taux général à 4 par défaut
        int generalTradeRate = 4;

        // Vérifier la présence d'un port classique
        boolean hasGeneralPort = currentPlayerPorts.stream().anyMatch(port ->
                !(port instanceof SpecializedHarbor));
        if (hasGeneralPort) {
            generalTradeRate = 3;
        }

        gatherResourcesOffered();
        gatherResourcesRequested();
        int totalExchanges = 0;

        for (Map.Entry<TileType, Integer> entry : resourcesOffered.entrySet()) {
            TileType offeredResource = entry.getKey();
            Integer offeredAmount = entry.getValue();
            int requiredTradeRate = generalTradeRate; // Taux général par défaut.

            // Vérifier si un port spécialisé ajuste le taux pour cette ressource.
            for (Harbor harbor : currentPlayerPorts) {
                if (harbor instanceof SpecializedHarbor
                        && ((SpecializedHarbor) harbor).getResourceType() == offeredResource
                        && !actionPlayerPanel.isHarboursDisabled()) {
                    requiredTradeRate = 2; // Taux spécialisé.
                    break;
                }
            }

            if (offeredAmount % requiredTradeRate != 0) {
                return false; // Si les ressources offertes ne correspondent pas au taux d'échange requis.
            }
            totalExchanges += offeredAmount / requiredTradeRate;
        }

        int totalRequestedResources = getTotalSelectedResources(playerTwoLabels);
        return totalExchanges == totalRequestedResources;
    }

    // -------- Fonctions affichant et traitant les boutons
    // et leur mécanisme pour la séléction des joueurs -------- //

    private void createPlayersButtons() {
        int spacing = 100; // Espacement entre chaque bouton de joueur
        int initialX = 520;
        int y = 550;
        for (Player player : listPlayers) {
            if (player != listPlayers.getCurrentPlayer()) {
                addPlayerButton(player, initialX, y);
                initialX += spacing; // Décale le prochain bouton vers la droite
            }
        }
    }
    private void addPlayerButton(Player player, int x, int y) {
        String pionPathImg = "src/main/resources/pion/pion" + player.getColorString() + ".png";
        ButtonImage button = createButtonImage(pionPathImg, x, y - 15);
        button.setAction(() -> actionPlayerButton(player));
        JLabel playerName = createPlayerNameLabel(player.getName().toUpperCase(), x,
                y + 26);
        add(playerName);
    }
    private JLabel createPlayerNameLabel(String name, int x, int y) {
        JLabel playerName = new JLabel(name, SwingConstants.CENTER);
        playerName.setFont(new Font("SansSerif", Font.BOLD, (int) (30 / Resolution.divider())));
        playerName.setBackground(Color.decode("#F3DCB7"));
        playerName.setOpaque(true);
        int[] coords = Resolution.calculateResolution(x, y);
        int newX = coords[0];
        int newY = coords[1];
        playerName.setBounds(newX - (int) (50 / Resolution.divider()), newY,
                (int) (170 / Resolution.divider()), (int) (30 / Resolution.divider()));
        return playerName;
    }
    private void actionPlayerButton(Player player) {
        selectedPlayer = player;
        isBank = false;
        selectedPlayerLabel.setText("<html><div style='text-align: center;'>"
                + "ÉCHANGE AVEC<br/>" + player.getName().toUpperCase() + "</div></html>");
        updateSelectedPlayerImage("src/main/resources/pion/pion"
                + player.getColorString() + ".png");
        updateProposeButtonState();
    }

    // -------- Fonctions affichant et update le panel du joueur sélectionné -------- //

    private void initializeSelectedPlayerLabel() {
        selectedPlayerLabel = new JLabel("<html><div style='text-align: center;'>"
                + "AUCUN JOUEUR<br/>SÉLECTIONNÉ</div></html>");
        selectedPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        int[] coords = Resolution.calculateResolution(565, 225);
        selectedPlayerLabel.setBounds(coords[0], coords[1], (int) (325 / Resolution.divider()),
                (int) (100 / Resolution.divider()));
        selectedPlayerLabel.setFont(new Font("SansSerif", Font.BOLD, (int) (40 / Resolution.divider())));
        add(selectedPlayerLabel);
    }
    private void initializeSelectedPlayerImage() {
        selectedPlayerLabelIcon = new JLabel();
        int[] coords1 = Resolution.calculateResolution(630, 280);
        int xCoord = coords1[0];
        int yCoord = coords1[1];
        selectedPlayerLabelIcon.setBounds(xCoord, yCoord, (int) (75 / Resolution.divider()),
                (int) (80 / Resolution.divider()));
        add(selectedPlayerLabelIcon);
    }


    private void updateSelectedPlayerImage(String imagePath) {
        try {
            playerIcon = ImgService.loadImage(imagePath, 5.0);
            selectedPlayerLabelIcon.setIcon(new ImageIcon(playerIcon));
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
            selectedPlayerLabelIcon.setIcon(null);
        }
    }

    // -------- Fonctions utilitaires pour tout le monde -------- //

    private ButtonImage createButtonImage(String imagePath, int x, int y) {
        ButtonImage button = new ButtonImage(imagePath, imagePath, x, y, 5, () -> { }, null);
        add(button);
        return button;
    }
    private void initializeResourceNameMap() {
        resourceNameToTileType.put("clay", TileType.CLAY);
        resourceNameToTileType.put("ore", TileType.ORE);
        resourceNameToTileType.put("wheat", TileType.WHEAT);
        resourceNameToTileType.put("wood", TileType.WOOD);
        resourceNameToTileType.put("wool", TileType.WOOL);
    }

    // -------- Fonctions utilitaires qui servent au panel seulement -------- //

    public GameWindow getParentFrame() {
        return (GameWindow) SwingUtilities.getWindowAncestor(this);
    }

    public void closeTradePanel() {
        GameWindow parentFrame = getParentFrame();
        if (parentFrame != null) {
            this.setVisible(false);
            parentFrame.getActionPlayer().setComponentsEnabled(true);
            parentFrame.getActionPlayer().setVisible(true);
        }
    }

    private void initializeReturnButton() {
        ButtonImage returnButton = new ButtonImage("src/main/resources/backGame.png",
                "src/main/resources/backGame.png", 100, 620, 1.2, this::closeTradePanel, null);
        add(returnButton);
    }

    // -------- TradePanel pour les bots --------- //

    public TradePanel(ListPlayers listPlayers, HashMap<TileType, Integer> resourcesOffered,
        HashMap<TileType, Integer> resourcesRequested, Player selectedPlayer, App app) {

        this.resourcesOffered = resourcesOffered;
        this.resourcesRequested = resourcesRequested;
        this.listPlayers = listPlayers;
        this.selectedPlayer = selectedPlayer;
        boolean canFulfillRequest = canSelectedPlayerFulfillRequest();
        if (canFulfillRequest) {
            if (((Bot) selectedPlayer).acceptTrade(resourcesRequested, resourcesOffered)) {
                performTrade(false);
                if (!App.getBotSoloMode()) {
                    actionPlayerPanel.getApp().addMessageColor("trade avec :"
                                    + selectedPlayer.getName() + " accepté \n",
                            java.awt.Color.GREEN);
                } else {
                    app.addMessageColor("trade avec :" + selectedPlayer.getName() + " accepté \n",
                            java.awt.Color.GREEN);
                }

            }
        } else {
            if (!App.getBotSoloMode()) {
                actionPlayerPanel.getApp().addMessageColor("trade avec :"
                                + selectedPlayer.getName() + " refusé \n",
                        Color.ORANGE);
            } else {
                app.addMessageColor("trade avec :" + selectedPlayer.getName() + " refusé \n",
                        Color.ORANGE);
            }
        }
    }

    // -------- Fonctions d'affichage du panel et de son background -------- //

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(this.getWidth(),
                this.getHeight(), Image.SCALE_SMOOTH);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

    // -------- Fonctions de tests -------- //

    public void displayResources(Map<TileType, Integer> resourcesList) {
        if (resourcesList.isEmpty()) {
            System.out.println("Aucune ressource demandée ou offerte.");
            return;
        }

        for (Map.Entry<TileType, Integer> entry : resourcesList.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
