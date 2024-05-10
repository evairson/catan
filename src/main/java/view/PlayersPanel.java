package view;

import java.awt.Font;
import java.awt.Image;
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
import view.utilities.Resolution;

public class PlayersPanel extends JPanel {

    private HashMap<Player, JLabel> labels;

    public PlayersPanel(Game game) {
        labels = new HashMap<>();
        setLayout(null);
        int x = Resolution.calculateResolution(30, 10)[0];
        int y = Resolution.calculateResolution(30, 10)[1];
        setBounds(x, y, (int) (2000 / Resolution.divider()), (int) (100 / Resolution.divider()));
        for (int i = 0; i < game.getPlayers().size(); i++) {
            JLabel label;
            try {
                String src = "src/main/resources/pion/pion";
                String imagePath = src + game.getPlayers().get(i).getColorString() + ".png";
                Image origiImg = ImageIO.read(new File(imagePath));
                int scale = (int) (40 / Resolution.divider());
                Image buttonImage = origiImg.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
                String text = game.getPlayers().get(i).getName().toUpperCase();
                Boolean player = game.getPlayers().get(i) == game.getCurrentPlayer();
                String textUnderligne = player ? "<html><u> " + text + "</u></html>" : " " + text;
                label = new JLabel(textUnderligne, new ImageIcon(buttonImage), JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.CENTER);
                label.setHorizontalTextPosition(JLabel.RIGHT);
                label.setFont(new Font("SansSerif", Font.BOLD, scale));
                double x1 = Resolution.calculateResolution(i * 200, 20)[0];
                double y1 = Resolution.calculateResolution(i * 200, 20)[1];
                label.setBounds((int) x1, (int) y1, (int) (scale * 10), (int) (scale * 1.2));
                labels.put(game.getPlayers().get(i), label);
                add(label);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setOpaque(false);
    }

    public void update(Game game) {
        for (Entry<Player, JLabel> label : labels.entrySet()) {
            String text = label.getKey().getName().toUpperCase();
            Boolean player = label.getKey() == game.getCurrentPlayer();
            String textUnderligne = player ? "<html><u>" + text + "</u></html>" : " " + text;
            label.getValue().setText(textUnderligne);
        }
        repaint();
    }
}
