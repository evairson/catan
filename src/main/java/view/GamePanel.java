package view;

import controler.KeyBoardInputs;
import controler.MouseInputs;
import model.App;
import others.Constants;
import view.utilities.Resolution;

import javax.swing.*;


import java.awt.*;

public class GamePanel extends JPanel {
    private App game;

    private MouseInputs mouseInputs;
    private Image backgroundImage;


    public GamePanel(App game) {
        this.game = game;

        mouseInputs = new MouseInputs(game);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new KeyBoardInputs());
        setPanelSize();
        setOpaque(true);
        int[] coords = Resolution.calculateResolution(100, 50);
        int xCoord = coords[0];
        int yCoord = coords[1];
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
        loadBackgroundImage("src/main/resources/background.png");
    }

    private void loadBackgroundImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        int width = this.getWidth();
        int height = this.getHeight();
        backgroundImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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
        g.drawImage(backgroundImage, 0, 0, this);
        revalidate();
        game.render(g);
    }


}

