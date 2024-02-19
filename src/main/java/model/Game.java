package model;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import model.cards.CardStack;
import model.geometry.Layout;
import model.geometry.Point;
import others.Constants;
import others.ListPlayers;

public class Game implements StateMethods {
    private static GameBoard board;
    private ListPlayers players; // ListPlayers extends ArrayList
    private CardStack stack;
    private Thief thief;

    Game() {
        Player player1 = new Player(Player.Color.RED, "Player1");
        Player player2 = new Player(Player.Color.YELLOW, "Player2");
        Player player3 = new Player(Player.Color.BLUE, "Player3");
        Player player4 = new Player(Player.Color.GREEN, "Player4");
        players = new ListPlayers(0, player1, player2, player3, player4);

        Point point1 = new Point(400, 400);
        Point point2 = new Point(50, 50);
        Layout layout = new Layout(Constants.OrientationConstants.POINTY, point1, point2);
        thief = new Thief();
        board = new GameBoard(layout, thief);

        stack = new CardStack();
    }

    public CardStack getStack() {
        return stack;
    }

    public Thief getThief() {
        return thief;
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

    public static GameBoard getBoard() {
        return board;
    }

    public static void setBoard(GameBoard board) {
        Game.board = board;
    }

    public void setThiefMode(boolean b) {
        board.setThiefMode(b);
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
        board.mouseCliqued(e);
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
