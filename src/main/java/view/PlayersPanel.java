package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Game;
import model.Player;
import model.geometry.Point;
import view.utilities.Resolution;

public class PlayersPanel extends JPanel {

//    private HashMap<Player, JLabel> labels;
    private HashMap<Player, PlayerInfo> playerInfos;
    private Game game;

    public PlayersPanel(Game game) {
        this.game = game;
        playerInfos = new HashMap<>();
        setLayout(null);
        int x = Resolution.calculateResolution(30, 10)[0];
        int y = Resolution.calculateResolution(30, 10)[1];
        setBounds(x, y, (int) (2000 / Resolution.divider()), (int) (200 / Resolution.divider()));
        initializePlayers();
        setOpaque(false);
    }

    private void initializePlayers() {
        int playerIndex = 0;
        for (Player player : game.getPlayers()) {
            int baseX = Resolution.calculateResolution(300 * playerIndex, 20)[0];
            int baseY = Resolution.calculateResolution(0, 45)[1];
            // Position de base pour les éléments sous le nom du joueur

            // Création et positionnement du label du joueur
            JLabel playerLabel = createPlayerLabel(player, playerIndex);
            add(playerLabel);
//            playerLabel.setBackground(Color.RED);
//            playerLabel.setOpaque(true);

            // Calcul de la position X pour les éléments suivants, en se basant sur la fin du nameLabel
            int offsetX = playerLabel.getBounds().x + playerLabel.getBounds().width - 40;
            // 10 pixels à droite de la fin du nameLabel

            // Utilisation de la même hauteur (Y) que nameLabel pour aligner verticalement les icônes et label
            int newBaseY = playerLabel.getBounds().y;

            // Création et ajout de l'icône de ressource et du label de compte
            JLabel resourceIconLabel = createIconLabel(offsetX, newBaseY,
                    "src/main/resources/resources/resourceLabel.png");
            JLabel resourceCountLabel = createCountLabel(offsetX + 25, newBaseY);
            // 25 pixels à droite de l'icône de ressource pour le label de compte
            // Création et ajout de l'icône de carte et du
            // label de compte, à droite des icônes et labels de resource
            JLabel cardIconLabel = createIconLabel(offsetX,
                    newBaseY + 25, "src/main/resources/card.png");
            // 60 pixels à droite pour séparer les groupes de ressources et de cartes
            JLabel cardCountLabel = createCountLabel(offsetX + 25,
                    newBaseY + 25);
            // 25 pixels à droite de l'icône de carte pour le label de compte

            JLabel vpIconLabel = createIconLabel(offsetX, newBaseY,
                    "src/main/resources/vplabel.png");
            JLabel vpCountLabel = createCountLabel(offsetX + 25, newBaseY);
            // ff
            add(vpIconLabel);
            add(vpCountLabel);
//            add(resourceIconLabel);
//            add(resourceCountLabel);
            add(cardIconLabel);
            add(cardCountLabel);

//            resourceIconLabel.setBackground(Color.BLUE);
//            resourceCountLabel.setBackground(Color.BLUE);
//            cardIconLabel.setBackground(Color.BLUE);
//            cardCountLabel.setBackground(Color.BLUE);
//            resourceCountLabel.setOpaque(true);
//            resourceIconLabel.setOpaque(true);
//            cardCountLabel.setOpaque(true);
//            cardIconLabel.setOpaque(true);

            // Stockage des informations du joueur
            playerInfos.put(player, new PlayerInfo(playerLabel, resourceIconLabel,
                    resourceCountLabel, cardIconLabel, cardCountLabel, vpCountLabel));
            playerIndex++;
        }
    }

    private JLabel createPlayerLabel(Player player, int index) {
        JLabel label;
        try {
            String src = "src/main/resources/pion/pion";
            String imagePath = src + player.getColorString() + ".png";
            Image origiImg = ImageIO.read(new File(imagePath));
            int scale = (int) (40 / Resolution.divider());
            Image buttonImage = origiImg.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
            String text = player.getName().toUpperCase();
            Boolean isCurrentplayer = player == game.getCurrentPlayer();
            String textUnderligne = isCurrentplayer ? "<html><u> " + text + "</u></html>" : " " + text;
            label = new JLabel(textUnderligne, new ImageIcon(buttonImage), JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(new Font("SansSerif", Font.BOLD, scale));
            double x1 = Resolution.calculateResolution(index * 200, 20)[0];
            double y1 = Resolution.calculateResolution(index * 200, 20)[1];
            label.setBounds((int) x1, (int) y1, (int) (scale * 10), (int) (scale * 1.2));
            add(label);
            return label;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JLabel createIconLabel(int x, int y, String iconPath) {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File(iconPath)));
            JLabel label = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(20,
                    20, Image.SCALE_SMOOTH)));
            label.setBounds(x, y, 20, 20);
            return label;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JLabel createCountLabel(int x, int y) {
        JLabel label = new JLabel("0");
        label.setBounds(x, y, 50, 20); // Largeur pour accueillir des nombres
        return label;
    }

    public void update(Game game) {
        for (Entry<Player, PlayerInfo> entry : playerInfos.entrySet()) {
            Player player = entry.getKey();
            PlayerInfo info = entry.getValue();

            // Mise à jour des textes sous les labels du joueur
            info.resourceCountLabel.setText(String.valueOf(player.getResourcesSum()));
            info.cardCountLabel.setText(String.valueOf(player.getCardsDev().size()));
            info.vpCountLabel.setText(String.valueOf(player.getPoints()));

            // Marquer le joueur courant
            boolean isCurrentPlayer = player == game.getCurrentPlayer();
            info.nameLabel.setText(isCurrentPlayer ? "<html><u>"
                    + player.getName().toUpperCase() + "</u></html>" : player.getName().toUpperCase());
        }
        repaint();
    }


    public Point getPlayerLabelPosition(Player player) {
        PlayerInfo playerInfo = playerInfos.get(player);
        if (playerInfo != null) {
            int x = playerInfo.nameLabel.getX() + playerInfo.nameLabel.getWidth() / 2;
            int y = playerInfo.nameLabel.getY() + playerInfo.nameLabel.getHeight() / 2;
            return new Point(x, y);
        }
        return null; // ou une position par défaut si le joueur n'est pas trouvé
    }

    private class PlayerInfo {
        private JLabel nameLabel;
        private JLabel resourceIconLabel;
        private JLabel resourceCountLabel;
        private JLabel cardIconLabel;
        private JLabel cardCountLabel;
        private JLabel vpCountLabel;

        PlayerInfo(JLabel nameLabel, JLabel resourceIconLabel,
                   JLabel resourceCountLabel, JLabel cardIconLabel, JLabel cardCountLabel,
                   JLabel vpCountLabel) {
            this.nameLabel = nameLabel;
            this.resourceIconLabel = resourceIconLabel;
            this.resourceCountLabel = resourceCountLabel;
            this.cardIconLabel = cardIconLabel;
            this.cardCountLabel = cardCountLabel;
            this.vpCountLabel = vpCountLabel;
        }

        public void updateName(String name) {
            nameLabel.setText(name);
        }
    }

}
