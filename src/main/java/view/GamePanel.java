package view;

import controler.KeyBoardInputs;
import controler.MouseInputs;
import model.App;
import others.Constants;

import javax.swing.*;


import java.awt.*;

public class GamePanel extends JPanel {
    private App game;

    private MouseInputs mouseInputs;

    public GamePanel(App game) {
        this.game = game;
        mouseInputs = new MouseInputs(game);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyBoardInputs());
        setPanelSize();
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        setOpaque(false);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }
}

