package view.gamepanels;

import model.Player;
import others.Constants;
import others.ListPlayers;
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
    private Image playerIcon;
    private Map<String, TileType> resourceNameToTileType = new HashMap<>();
    private HashMap<TileType, Integer> resourcesRequested = new HashMap<>();

    public TradePanel(ListPlayers listPlayers) {
        this.listPlayers = listPlayers;
        initializeUI();
        initializeResourceNameMap();
        setLayout(null);

        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        createResourceButtons();
        createTradeButtons();
        createPlayersButtons();
        initializeSelectedPlayerImage();
        updateProposeButtonState();
    }

    // -------- Fonctions affichant les boutons ressources et leurs labels respectifs -------- //

    private void createResourceButtons() {
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
            configurePlayerTwoButton(playerTwoButtons[i], playerTwoLabels[i], type);
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
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                    updateProposeButtonState();
                }
            }
        });
    }
    private void configurePlayerTwoButton(ButtonImage button, JLabel counterLabel,
                                          TileType resourceType) {
        button.addActionListener(e -> {
            int currentValue = Integer.parseInt(counterLabel.getText());
            currentValue++;
            counterLabel.setText(String.valueOf(currentValue));
            updateProposeButtonState();
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                    updateProposeButtonState();
                }
            }
        });
    }

    // -------- Fonctions affichant les boutons d'échanges (Propose, Accept, Decline) -------- //

        // TODO : simplifier cette merde !
    private void createTradeButtons() {
        proposeButton = new ButtonImage("src/main/resources/proposeButton.png",
                "src/main/resources/proposeButton.png", 605,
                629, 0.69, this::proposeAction, null);
        add(proposeButton);
        createTradeButton("src/main/resources/acceptButton.png", 320, 629);
        createTradeButton("src/main/resources/refuseButton.png", 830, 629);
    }
    private ButtonImage createTradeButton(String imagePath, int x, int y) {
        ButtonImage button = new ButtonImage(imagePath, imagePath, x, y, 0.69, this::closeTradePanel, null);
        add(button);
        return button;
    }

    // -------- Fonctions traitant les mécanismes des boutons d'échange -------- //

    private void proposeAction() {
        resourcesRequested.clear();
        for (int i = 0; i < playerTwoLabels.length; i++) {
            TileType resourceType = resourceNameToTileType.get(resourceNames[i]);
            int amount = Integer.parseInt(playerTwoLabels[i].getText());
            if (amount > 0) {
                resourcesRequested.put(resourceType, amount);
            }
        }
        displayResourcesRequested(resourcesRequested);
    }
    private void updateProposeButtonState() {
        boolean isPlayerSelected = selectedPlayer != null;
        boolean hasValidResourceValuesP1 = false;
        boolean hasValidResourceValuesP2 = false;
        for (JLabel label : playerOneLabels) {
            if (Integer.parseInt(label.getText()) > 0) {
                hasValidResourceValuesP1 = true;
                break;
            }
        }
        for (JLabel label : playerTwoLabels) {
            if (Integer.parseInt(label.getText()) > 0) {
                hasValidResourceValuesP2 = true;
                break;
            }
        }
        boolean hasValidResourceValues = hasValidResourceValuesP1 && hasValidResourceValuesP2;
        proposeButton.setEnabled(isPlayerSelected && hasValidResourceValues);
    }

    // -------- Fonctions affichant et traitant les boutons et leur mécanisme pour la séléction des joueurs -------- //

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
        ButtonImage button = createButtonImage(pionPathImg, x, y - (int) (20 / Resolution.divider()));
        button.setAction(() -> actionPlayerButton(player));
        JLabel playerName = createPlayerNameLabel(player.getName().toUpperCase(), x,
                y + (int) (30 / Resolution.divider()));
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
                (int) (170 / Resolution.divider()), (int) (35 / Resolution.divider()));
        return playerName;
    }

    private void actionPlayerButton(Player player) {
        selectedPlayer = player;
        selectedPlayerLabel.setText("<html><div style='text-align: center;'>"
                + "ÉCHANGE AVEC<br/>" + player.getName().toUpperCase() + "</div></html>");
        updateSelectedPlayerImage("src/main/resources/pion/pion"
                + player.getColorString() + ".png");
        updateProposeButtonState();
    }

    // -------- Fonctions affichant et update le panel du joueur sélectionné -------- //

    private void initializeUI() {
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

    public void displayResourcesRequested(Map<TileType, Integer> resourcesRequested) {
        if (resourcesRequested.isEmpty()) {
            System.out.println("Aucune ressource demandée.");
            return;
        }

        for (Map.Entry<TileType, Integer> entry : resourcesRequested.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
