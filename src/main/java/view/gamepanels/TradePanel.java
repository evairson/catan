package view.gamepanels;

import model.Player;
import network.NetworkObject;
import network.NetworkObject.TypeObject;
import network.PlayerClient;
import network.TradeObject;
import others.Constants;
import others.ListPlayers;
import start.Main;
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
import java.util.HashMap;
import java.util.Map;

public class TradePanel extends JPanel {
    private Image backgroundImage;
    private ButtonImage[] playerOneButtons = new ButtonImage[5];
    private ButtonImage[] playerTwoButtons = new ButtonImage[5];
    private JLabel[] playerOneLabels = new JLabel[5];
    private JLabel[] playerTwoLabels = new JLabel[5];

    private final String[] resourceNames = {"clay", "ore", "wheat", "wood", "wool"};
    private final int[] buttonYPositions = {378, 312, 446, 245, 515};
    private ListPlayers listPlayers;
    private Player selectedPlayer;
    private JLabel selectedPlayerLabel;
    private JLabel selectedPlayerLabelIcon;
    private ButtonImage proposeButton;
    private ButtonImage acceptButton;
    private ButtonImage declineButton;
    private Image playerIcon;
    private ButtonImage bankTradeButton;
    private Map<String, TileType> resourceNameToTileType = new HashMap<>();
    private HashMap<TileType, Integer> resourcesRequested = new HashMap<>();
    private HashMap<TileType, Integer> resourcesOffered = new HashMap<>();
    private ResourcesPanel resourcesPanel;
    private boolean isBank = false;

    public TradePanel(ListPlayers listPlayers, ResourcesPanel resourcesPanel) {
        this.listPlayers = listPlayers;
        this.resourcesPanel = resourcesPanel;
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
    }

    public TradePanel(TradeObject tradeObject) {
        this.resourcesOffered = tradeObject.getResourcesOffered();
        this.resourcesRequested = tradeObject.getResourcesRequested();
        //initializeSelectedPlayerLabel();
        initializeResourceNameMap();
        setLayout(null);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        createResourceButtons(false);
        createTradeButtons();
        //createPlayersButtons();
        //initializeSelectedPlayerImage();
        updateProposeButtonState();
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
        }
    }
    private JLabel createCounterLabel(int x, int y, int height) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setForeground(Color.RED);
        label.setBounds(coords[0], coords[1], 20, height);
        int scale = (int) (32 / Resolution.divider());
        label.setFont(new Font("SansSerif", Font.BOLD, scale));
        add(label);
        return label;
    }

    // -------- Fonctions configurant les boutons ressources et leurs labels respectifs -------- //

    private void configurePlayerOneButton(ButtonImage button, JLabel counterLabel,
                                          TileType resourceType) {
        Player currentPlayer = listPlayers.getCurrentPlayer();
        button.addActionListener(e -> {
            int currentValue = Integer.parseInt(counterLabel.getText());
            int playerResourceAmount = currentPlayer.getResources().getOrDefault(resourceType, 0);
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
        if (Main.hasServer()) {
            declineButton.setEnabled(false);
            proposeButton.setEnabled(false);
            acceptButton.setEnabled(false);
            bankTradeButton.setEnabled(false);
        } else {
            acceptButton.setEnabled(canFulfillRequest);
            declineButton.setEnabled(true);
        }
    }
    private void bankTradeAction() {
        isBank = true;
        updateAcceptButtonState();
        selectedPlayerLabelIcon = null;
        initializeSelectedPlayerImage();
        selectedPlayerLabel.setText("<html><div style='text-align: center;'>"
                + "ÉCHANGE AVEC<br/> La Banque</div></html>");
    }
    private void notifyOfferToPlayer(Player player) {
        // TODO : Implémentez la notification pour le joueur sélectionné.
        // Cela peut être un changement de couleur, un message pop-up, etc.

        if (listPlayers.getCurrentPlayer() instanceof PlayerClient) {
            try {
                PlayerClient playerClient = (PlayerClient) player;
                int id = playerClient.getId();
                TradeObject tradeObject = new TradeObject(id, player.getId(), resourcesOffered,
                    resourcesRequested);
                NetworkObject object = new NetworkObject(TypeObject.Game, "trade", id, tradeObject);
                playerClient.getOut().writeUnshared(object);
                playerClient.getOut().flush();
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            System.out.println("Problème de downCast");
        }
    }
    private void acceptAction(boolean isBank) {
        performTrade(isBank);
        closeTradePanel();
    }
    private void declineAction() {
        declineButton.setEnabled(false);
        acceptButton.setEnabled(false);
        toggleTradeInterface(true);
    }
    private void performTrade(boolean isBank) {
        HashMap<TileType, Integer> currentPlayerResources = listPlayers.getCurrentPlayer().getResources();

        if (isBank) {
            // Transaction avec la banque
            gatherResourcesOffered();
            gatherResourcesRequested();

            for (Map.Entry<TileType, Integer> entry : resourcesOffered.entrySet()) {
                TileType resource = entry.getKey();
                Integer amount = entry.getValue();

                // Retirer les ressources offertes du joueur courant
                currentPlayerResources.put(resource, currentPlayerResources.getOrDefault(resource,
                        0) - amount);
            }

            for (Map.Entry<TileType, Integer> entry : resourcesRequested.entrySet()) {
                TileType resource = entry.getKey();
                Integer amount = entry.getValue();

                // Ajouter la ressource demandée au joueur courant
                currentPlayerResources.put(resource, currentPlayerResources.getOrDefault(resource,
                        0) + amount);
            }
        } else {
            // Transaction avec un autre joueur
            HashMap<TileType, Integer> selectedPlayerResources = selectedPlayer.getResources();

            for (Map.Entry<TileType, Integer> entry : resourcesOffered.entrySet()) {
                TileType resource = entry.getKey();
                Integer amount = entry.getValue();

                currentPlayerResources.put(resource, currentPlayerResources.getOrDefault(resource,
                        0) - amount);
                selectedPlayerResources.put(resource, selectedPlayerResources.getOrDefault(resource,
                        0) + amount);
            }

            for (Map.Entry<TileType, Integer> entry : resourcesRequested.entrySet()) {
                TileType resource = entry.getKey();
                Integer amount = entry.getValue();

                currentPlayerResources.put(resource, currentPlayerResources.getOrDefault(resource,
                        0) + amount);
                selectedPlayerResources.put(resource, selectedPlayerResources.getOrDefault(resource,
                        0) - amount);
            }
        }

        // Effacer les ressources demandées et offertes après la transaction
        resourcesRequested.clear();
        resourcesOffered.clear();

        // Mettre à jour l'affichage des ressources pour les joueurs impliqués
        resourcesPanel.updateResourceLabels(listPlayers.getCurrentPlayer());
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
        proposeButton.setEnabled(isPlayerSelected && hasValidResourceValues);
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

    private boolean isValidBankTrade() {
        gatherResourcesOffered();
        gatherResourcesRequested();
        int totalMultiplesOfFour = 0;
        for (Map.Entry<TileType, Integer> entry : resourcesOffered.entrySet()) {
            if (entry.getValue() % 4 != 0) {
                // Si l'une des ressources offertes n'est pas un multiple de 4, l'échange n'est pas valide.
                return false;
            }
            totalMultiplesOfFour += entry.getValue() / 4;
        }

        // Calcul du nombre total de ressources demandées.
        int totalRequestedResources = getTotalSelectedResources(playerTwoLabels);

        // Et il doit y avoir exactement k types de ressources demandées pour k multiples de 4 offerts.
        return totalMultiplesOfFour == totalRequestedResources;
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

    // -------- Fonctions d'affichage du panel et de son background -------- //

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(this.getWidth(),
                this.getHeight(), Image.SCALE_SMOOTH);
        System.out.println("derchos" + Constants.Game.WIDTH + " ; " + Constants.Game.HEIGHT);
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
