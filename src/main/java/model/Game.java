package model;

import others.Constants;
import view.GamePanel;
import view.GameState;
import view.GameWindow;

import java.awt.*;

import model.geometry.Layout;

public class Game implements Runnable {
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    public static GameBoard board;

    public Game() {
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);

        Layout layout = new Layout(Constants.OrientationConstants.POINTY, new Point(400, 400), new Point(50, 50));
        board = new GameBoard(layout);


        gamePanel.requestFocus();

        startGameLoop();
    }


    public final GamePanel getGamePanel() {
        return gamePanel;
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
    }

    public void render(Graphics g) {
        switch(GameState.state){
            case Board: board.draw(g);
        }
    }

    @Override
    public final void run() {

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            double timePerFrame = Constants.Number.DOUBLE_BILLION / Constants.Game.FPS_SET;
            double timePerUpdate = Constants.Number.DOUBLE_BILLION / Constants.Game.UPS_SET;
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= Constants.Number.SECOND) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS :" + frames + " | Ups " + updates);
                updates = 0;
                frames = 0;
            }

        }
    }


}

