package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.cards.CardStack;
import model.geometry.Layout;
import model.geometry.Point;
import others.Constants;
import others.ListPlayers;

import java.io.Serializable;
import java.util.HashSet;

public class Game implements StateMethods, Serializable {
    private GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;

    Game(HashSet<String> names) {
        HashSet<Player> playersSet = new HashSet<>();
        for (String name : names) {
            Player player = new Player(Player.Color.RED, name);
            playersSet.add(player);
        }
        players = new ListPlayers(0, playersSet);

        Point point1 = new Point(400, 400);
        Point point2 = new Point(50, 50);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        board = new GameBoard(layout);

        stack = new CardStack();
    }

    public CardStack getStack() {
        return stack;
    }

    public void endTurn() {
        players.next();
    }

    public Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    public ListPlayers getPlayers() {
        return players;
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
