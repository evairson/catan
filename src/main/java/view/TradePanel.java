package view;

import model.Game;
import model.Player;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class TradePanel extends JPanel {

    public TradePanel(Game game, GameWindow gameWindow) {
        // Définir le gestionnaire de disposition pour le JPanel principal
        setLayout(new GridBagLayout());

        ArrayList<Player> otherPlayers = (ArrayList<Player>) game.getPlayers().clone();
        otherPlayers.remove(game.getCurrentPlayer());

        Player tradingPlayer = game.getCurrentPlayer();
        final Player[] playerReceiving = {null};

        // Créer un JPanel pour les boutons radio
        JPanel radioPanel = new JPanel(new GridLayout(1, 3));

        JRadioButton player1 = new JRadioButton(otherPlayers.get(0).getName());
        player1.addActionListener(actionEvent -> playerReceiving[0] = otherPlayers.get(0));

        JRadioButton player2 = new JRadioButton(otherPlayers.get(1).getName());
        player2.addActionListener(actionEvent -> playerReceiving[0] = otherPlayers.get(1));

        JRadioButton player3 = new JRadioButton(otherPlayers.get(2).getName());
        player3.addActionListener(actionEvent -> playerReceiving[0] = otherPlayers.get(2));

        ButtonGroup group = new ButtonGroup();
        group.add(player1);
        group.add(player2);
        group.add(player3);
        radioPanel.add(player1);
        radioPanel.add(player2);
        radioPanel.add(player3);

        // Créer un JPanel pour les éléments donnés
        JPanel givePanel = new JPanel(new GridLayout(6, 2));
        JLabel giveLabel = new JLabel("You give", SwingConstants.CENTER);
        HashMap<TileType, Integer> tradingPResources = tradingPlayer.getResources();
        JLabel resLabel1 = new JLabel("Clay (you have " + tradingPResources.get(TileType.CLAY) + ") :");
        JLabel resLabel2 = new JLabel("Ore (you have " + tradingPResources.get(TileType.ORE) + ") :");
        JLabel resLabel3 = new JLabel("Wheat (you have " + tradingPResources.get(TileType.WHEAT) + ") :");
        JLabel resLabel4 = new JLabel("Wood (you have " + tradingPResources.get(TileType.WOOD) + ") :");
        JLabel resLabel5 = new JLabel("Wool (you have " + tradingPResources.get(TileType.WOOL) + ") :");
        JTextField resource1 = new ResourceField(tradingPlayer.getResources().get(TileType.CLAY));
        JTextField resource2 = new ResourceField(tradingPlayer.getResources().get(TileType.ORE));
        JTextField resource3 = new ResourceField(tradingPlayer.getResources().get(TileType.WHEAT));
        JTextField resource4 = new ResourceField(tradingPlayer.getResources().get(TileType.WOOD));
        JTextField resource5 = new ResourceField(tradingPlayer.getResources().get(TileType.WOOL));
        givePanel.add(giveLabel);
        givePanel.add(new JLabel()); // Emplacement vide pour centrer "You give"
        givePanel.add(resLabel1);
        givePanel.add(resource1);
        givePanel.add(resLabel2);
        givePanel.add(resource2);
        givePanel.add(resLabel3);
        givePanel.add(resource3);
        givePanel.add(resLabel4);
        givePanel.add(resource4);
        givePanel.add(resLabel5);
        givePanel.add(resource5);

        // Créer un JPanel pour les éléments reçus
        JPanel receivePanel = new JPanel(new GridLayout(6, 2));
        JLabel receiveLabel = new JLabel("You receive", SwingConstants.CENTER);
        JLabel resourceLabel6 = new JLabel("Clay :");
        JLabel resourceLabel7 = new JLabel("Ore :");
        JLabel resourceLabel8 = new JLabel("Wheat :");
        JLabel resourceLabel9 = new JLabel("Wood :");
        JLabel resourceLabel10 = new JLabel("Wool :");
        JTextField resource6 = new ResourceField();
        JTextField resource7 = new ResourceField();
        JTextField resource8 = new ResourceField();
        JTextField resource9 = new ResourceField();
        JTextField resource10 = new ResourceField();
        receivePanel.add(receiveLabel);
        receivePanel.add(new JLabel()); // Emplacement vide pour centrer "You receive"
        receivePanel.add(resourceLabel6);
        receivePanel.add(resource6);
        receivePanel.add(resourceLabel7);
        receivePanel.add(resource7);
        receivePanel.add(resourceLabel8);
        receivePanel.add(resource8);
        receivePanel.add(resourceLabel9);
        receivePanel.add(resource9);
        receivePanel.add(resourceLabel10);
        receivePanel.add(resource10);

        // Créer un JPanel pour les boutons
        JPanel buttonPanel = new JPanel();

        //Stocke les variables pour jump entre JPanel
        Container contentPane = gameWindow.getContentPane();
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();

        JButton validerButton = new JButton("Valider");
        validerButton.addActionListener(actionEvent -> {
            //Si le trade ne peut pas être effectué car on a mal rempli les champs
            if (!checkValidTextFields(givePanel)
                    || !checkValidTextFields(receivePanel)
                    || !checkValidRadioButtons(radioPanel)) {
                return;
            }

            //Initialise les tableaux qui contiennent les valeurs pour le trade
            int[] toGive = {Integer.parseInt(resource1.getText()),
                    Integer.parseInt(resource2.getText()),
                    Integer.parseInt(resource3.getText()),
                    Integer.parseInt(resource4.getText()),
                    Integer.parseInt(resource5.getText())};

            int[] toReceive = {Integer.parseInt(resource6.getText()),
                    Integer.parseInt(resource7.getText()),
                    Integer.parseInt(resource8.getText()),
                    Integer.parseInt(resource9.getText()),
                    Integer.parseInt(resource10.getText())};

            AcceptTradePanel acceptTradePanel = new AcceptTradePanel(tradingPlayer, playerReceiving[0],
                    toGive, toReceive, contentPane, cardLayout);
            contentPane.add(acceptTradePanel, "acceptTradePanel");
            cardLayout.show(contentPane, "acceptTradePanel");
        });

        JButton retourButton = new JButton("Retour");
        retourButton.addActionListener(actionEvent -> {
            cardLayout.show(contentPane, "actionPlayerPanel");
            contentPane.remove(TradePanel.super.getParent());
        });

        buttonPanel.add(validerButton);
        buttonPanel.add(retourButton);

        // Ajouter les composants avec GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        add(radioPanel, gbc);

        gbc.gridy++;
        add(givePanel, gbc);

        gbc.gridy++;
        add(receivePanel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    public static void main(String[] args) {
        // Créer une nouvelle JFrame
        JFrame frame = new JFrame();

        // Définir la taille de la fenêtre
        frame.setSize(1920, 1080);

        // Définir la couleur de fond de la JFrame en blanc
        frame.getContentPane().setBackground(java.awt.Color.WHITE);

        // Permettre à la fenêtre de se fermer lorsqu'on clique sur le bouton de fermeture
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Rendre la fenêtre visible
        frame.setVisible(true);

    }

    /**
     * Checks if every JTextField in the window contains a value.
     * @param container The JPanel we are checking in
     * @return true if the condition before is verified, false if not
     */
    public static boolean checkValidTextFields(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (textField.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Checks if one JRadioButton is selected.
     * @param container The JPanel we are checking in
     * @return true if the condition before is verified, false if not
     */
    public static boolean checkValidRadioButtons(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) component;
                if (radioButton.isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private class AcceptTradePanel extends JPanel {
        AcceptTradePanel(Player tradingPlayer, Player receivingPlayer,
                         int[] give, int[] receive, Container contentPane, CardLayout cardLayout) {

            // Définir le gestionnaire de disposition pour le JPanel principal
            setLayout(new GridBagLayout());

            // Créer un JPanel pour le message d'offre de transaction
            JPanel offerMessagePanel = new JPanel();
            JLabel offerMessageLabel = new JLabel(receivingPlayer.getName() + ", you have a trade offer",
                    SwingConstants.CENTER);
            offerMessagePanel.add(offerMessageLabel);

            // Créer un JPanel pour les ressources du joueur qui propose le trade (tradingPlayer)
            JPanel playerGivesPanel = new JPanel(new GridLayout(7, 1));
            JLabel playerGivesLabel = new JLabel(tradingPlayer.getName() + " gives", SwingConstants.CENTER);
            JLabel resourceLabel1 = new JLabel("Clay : " + give[0]);
            JLabel resourceLabel2 = new JLabel("Ore : " + give[1]);
            JLabel resourceLabel3 = new JLabel("Wheat : " + give[2]);
            JLabel resourceLabel4 = new JLabel("Wood : " + give[3]);
            JLabel resourceLabel5 = new JLabel("Wool : " + give[4]);
            playerGivesPanel.add(playerGivesLabel);
            playerGivesPanel.add(resourceLabel1);
            playerGivesPanel.add(resourceLabel2);
            playerGivesPanel.add(resourceLabel3);
            playerGivesPanel.add(resourceLabel4);
            playerGivesPanel.add(resourceLabel5);

            // Créer un JPanel pour les ressources du joueur qui reçoit le trade (receivingPlayer)
            JPanel youGivePanel = new JPanel(new GridLayout(7, 1));
            JLabel youGiveLabel = new JLabel("You give", SwingConstants.CENTER);
            JLabel resourceLabel6 = new JLabel("Clay (you have "
                    + receivingPlayer.getResources().get(TileType.CLAY) + ") : " + receive[0]);
            JLabel resourceLabel7 = new JLabel("Ore (you have "
                    + receivingPlayer.getResources().get(TileType.ORE) + ") : " + receive[1]);
            JLabel resourceLabel8 = new JLabel("Wheat (you have "
                    + receivingPlayer.getResources().get(TileType.WHEAT) + ") : " + receive[2]);
            JLabel resourceLabel9 = new JLabel("Wood (you have "
                    + receivingPlayer.getResources().get(TileType.WOOD) + ") : " + receive[3]);
            JLabel resourceLabel10 = new JLabel("Wool (you have "
                    + receivingPlayer.getResources().get(TileType.WOOL) + ") : " + receive[4]);
            youGivePanel.add(youGiveLabel);
            youGivePanel.add(resourceLabel6);
            youGivePanel.add(resourceLabel7);
            youGivePanel.add(resourceLabel8);
            youGivePanel.add(resourceLabel9);
            youGivePanel.add(resourceLabel10);

            // Créer un JPanel pour les boutons
            JPanel buttonPanel = new JPanel();
            JButton acceptButton = new JButton("Accept");
            acceptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    //If we don't have enough resources to match the trade offer
                    if (!receivingPlayer.hasEnough(receive)) {
                        return;
                    }
                    tradingPlayer.removeResourceAmount(give);
                    receivingPlayer.addResourceAmount(give);

                    receivingPlayer.removeResourceAmount(receive);
                    tradingPlayer.addResourceAmount(receive);

                    cardLayout.show(contentPane, "actionPlayerPanel");
                    contentPane.remove(AcceptTradePanel.super.getParent());
                    contentPane.remove(TradePanel.super.getParent());
                }
            });

            JButton denyButton = new JButton("Deny");
            denyButton.addActionListener(actionEvent -> {
                cardLayout.show(contentPane, "actionPlayerPanel");
                contentPane.remove(AcceptTradePanel.super.getParent());
                contentPane.remove(TradePanel.super.getParent());
            });


            buttonPanel.add(acceptButton);
            buttonPanel.add(denyButton);

            // Ajouter les composants avec GridBagLayout
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;
            add(offerMessagePanel, gbc);

            gbc.gridy++;
            add(playerGivesPanel, gbc);

            gbc.gridy++;
            add(youGivePanel, gbc);

            gbc.gridy++;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            add(buttonPanel, gbc);
        }
    }

    private static class ResourceField extends JTextField {

        ResourceField(int limit) {
            setDocument(new LimitedNumericDocument(limit));
        }
        ResourceField() {
            setDocument(new LimitedNumericDocument(Integer.MAX_VALUE));
        }

        class LimitedNumericDocument extends PlainDocument {
            private int limit;

            LimitedNumericDocument(int limit) {
                super();
                this.limit = limit;
            }

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }

                // Concatenate the current text with the new string
                String currentText = getText(0, getLength());
                String newText = currentText.substring(0, offs) + str + currentText.substring(offs);

                // Try to parse the new text as an integer
                try {
                    int newValue = Integer.parseInt(newText);
                    if (newValue > limit || newValue < 0) {
                        // If the new value exceeds the limit, do not insert the string
                        return;
                    }
                } catch (NumberFormatException e) {
                    // If parsing fails, the inserted string contains non-numeric characters, do not insert
                    return;
                }

                // If the new value is within the limit, insert the string
                super.insertString(offs, str, a);
            }
        }
    }
}
