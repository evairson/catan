package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Game;
import view.utilities.ButtonImage;
import view.utilities.Resolution;

public class ActionPlayerPanel extends JPanel {
    private JButton endTurn;
    private JLabel namePlayer;
    private Game game;


    public ActionPlayerPanel(Game game) {
        this.game = game;
        setLayout(null);
        setOpaque(true);
        namePlayer = new JLabel(game.getCurrentPlayer().getName());
        double x = Resolution.calculateResolution(620, 550)[0];
        double y = Resolution.calculateResolution(620, 550)[1];
        namePlayer.setBounds((int) x, (int) y, 400, 20);
        add(namePlayer);
        createButton();
    }

    private void createButton() {
        String basePath = "src/main/resources/";
        endTurn = new ButtonImage(basePath + "quitButton.png", basePath + "quitButtonHover.png",
                558, 600, this::changeTurn);
        add(endTurn);
    }

    private void changeTurn() {
        game.endTurn();
        update();
    }

    private void update() {
        namePlayer.setText(game.getCurrentPlayer().getName());
        repaint();
    }
}
