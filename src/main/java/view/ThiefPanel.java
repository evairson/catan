package view;

import javax.swing.*;
import java.awt.*;

import model.Game;
import model.Thief;
import model.geometry.Layout;
import others.Constants;
import model.geometry.Point;

public class ThiefPanel extends JPanel {
    private Thief thief;
    private Game game;
    private Layout layout;

    public ThiefPanel(Game game) {
        this.game = game;
        this.thief = game.getThief();
        layout = Game.getBoard().getLayout();

        setOpaque(false);
        setBounds(0, 0, Constants.Game.WIDTH, Constants.Game.HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Point p = layout.cubeToPixel(layout, thief.getTile().getCoordinates());
        g2d.setColor(Color.white);
        g2d.fillOval((int) p.getX() - 10, (int) p.getY() - 10, 20, 20);
        revalidate();
    }
}
