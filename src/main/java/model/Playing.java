package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.geometry.Layout;
import model.geometry.Point;
import others.Constants;

public class Playing implements StateMethods {
    private static GameBoard board;
    private Game game;

    Playing() {
        Point point1 = new Point(400, 400);
        Point point2 = new Point(70, 70);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        board = new GameBoard(layout);

    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void draw(Graphics g) {
        board.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        board.mouseMoved(e);
    }

    @Override
    public void update() {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked");
        if (board.isPlacingCity()) {
            game.buildCity();
            System.out.println("Building city");
        } else if (board.isPlacingColony()) {
            game.buildColony();
            System.out.println("Building colony");
        } else if (board.isPlacingRoad()) {
            game.buildRoad();
            System.out.println("Building road");
        }

        game.getCurrentPlayer().printResources();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
