package view;

import controler.KeyBoardInputs;
import controler.MouseInputs;
import model.Game;
import others.Constants;

import javax.swing.*;


import java.awt.*;

public class GamePanel extends JPanel {
    private Game game;

    private MouseInputs mouseInputs;


    public GamePanel(Game game) {
        this.game = game;

        mouseInputs = new MouseInputs(game);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

        addKeyListener(new KeyBoardInputs());

        setPanelSize();
        setOpaque(true);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
    }

    private void updateGame() {
        game.update();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        revalidate();
        game.render(g);
    }
}

