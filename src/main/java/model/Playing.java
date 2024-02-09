package model;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import model.geometry.Layout;
import model.geometry.Point;
import others.Constants;

public class Playing {
    private static GameBoard board;

    Playing() {
        Point point1 = new Point(400, 400);
        Point point2 = new Point(50, 50);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        board = new GameBoard(layout);

    }

    public void draw(Graphics g) {
        board.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        board.mouseMoved(e);
    }
}
