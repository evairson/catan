package view.gamepanels;

import model.Player;
import others.Constants;
import others.ListPlayers;
import view.GameWindow;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TradePanel extends JPanel {
    private Image backgroundImage;
    private ButtonImage[] playerOneButtons = new ButtonImage[5];
    private ButtonImage[] playerTwoButtons = new ButtonImage[5];
    private JLabel[] playerOneLabels = new JLabel[5];
    private JLabel[] playerTwoLabels = new JLabel[5];

    private final String[] resourceNames = {"clay", "ore", "wheat", "wood", "wool"};
    private final int[] buttonYPositions = {378, 310, 446, 245, 515};
    private ListPlayers listPlayers;
    private Player selectedPlayer;
    private JLabel selectedPlayerLabel;
    public TradePanel(ListPlayers listPlayers) {
        this.listPlayers = listPlayers;
        initializeUI();

        setLayout(null);
        loadBackgroundImage("src/main/resources/tradePanel.png");
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        createResourceButtons();
        createTradeButtons();
    }

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

    private void initializeUI() {

        // Définir la position du label du joueur sélectionné
        selectedPlayerLabel = new JLabel("Aucun joueur sélectionné");
        selectedPlayerLabel.setBounds(10, 10, 300, 30); // Exemple de positionnement
        add(selectedPlayerLabel);

        int baseX = 10; // Position de départ pour le premier bouton
        int baseY = 50; // Position Y pour tous les boutons
        int buttonWidth = 100; // Largeur des boutons
        int buttonHeight = 30; // Hauteur des boutons
        int gap = 110; // Espace entre les boutons

        // Créer et ajouter les boutons pour chaque joueur (sauf le joueur courant)
        for (Player player : listPlayers) {
            if (player != listPlayers.getCurrentPlayer()) {
                JButton playerButton = new JButton(player.getName());
                playerButton.setBounds(baseX, baseY, buttonWidth, buttonHeight);
                playerButton.addActionListener(e -> {
                    selectedPlayer = player;
                    selectedPlayerLabel.setText("Échange avec : " + selectedPlayer.getName());
                });
                add(playerButton);
                baseX += gap; // Déplacer la position X pour le prochain bouton
            }
        }
    }


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

            configureButton(playerOneButtons[i], playerOneLabels[i]);
            configureButton(playerTwoButtons[i], playerTwoLabels[i]);
        }
    }
    private ButtonImage createButtonImage(String imagePath, int x, int y) {
        ButtonImage button = new ButtonImage(imagePath, imagePath, x, y, 5, () -> { }, null);
        add(button);
        return button;
    }
    private void createTradeButtons() {
        createTradeButton("src/main/resources/proposeButton.png", 605, 629);
        createTradeButton("src/main/resources/acceptButton.png", 320, 629);
        createTradeButton("src/main/resources/refuseButton.png", 830, 629);
    }
    private ButtonImage createTradeButton(String imagePath, int x, int y) {
        ButtonImage button = new ButtonImage(imagePath, imagePath, x, y, 0.69, this::closeTradePanel, null);
        add(button);
        return button;
    }
    private void configureButton(ButtonImage button, JLabel counterLabel) {
        button.addActionListener(e -> {
            int value = Integer.parseInt(counterLabel.getText()) + 1;
            counterLabel.setText(String.valueOf(value));
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int value = Integer.parseInt(counterLabel.getText()) - 1;
                    counterLabel.setText(String.valueOf(Math.max(0, value))); // Évite les valeurs négatives
                }
            }
        });
    }
    private JLabel createCounterLabel(int x, int y, int height) {
        int[] coords = Resolution.calculateResolution(x, y);
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setForeground(Color.RED);
        label.setBounds(coords[0], coords[1], 20, height);
        Font labelFont = label.getFont();
        label.setFont(labelFont.deriveFont(18.0f));
        add(label);
        return label;
    }
    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        backgroundImage = icon.getImage().getScaledInstance(Constants.Game.WIDTH,
                Constants.Game.HEIGHT, Image.SCALE_SMOOTH);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
