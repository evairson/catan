package model;

import others.Constants;
import view.GamePanel;
import view.GameWindow;

import java.awt.*;

public class Game implements Runnable{
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;

    

    public Game(){
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);

        
        gamePanel.requestFocus();
    
        startGameLoop();
    }


    public GamePanel getGamePanel(){
        return gamePanel;
    }


    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){
    }

    public void render(Graphics g){
    }

    @Override
    public void run() {

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true){
            double timePerFrame = 1000000000.0 / Constants.Game.FPS_SET;
            double timePerUpdate = 1000000000.0 / Constants.Game.UPS_SET;
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate; 
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                deltaF--;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS :" + frames + " | Ups " + updates);
                updates = 0;
                frames = 0;
            }

        }
    }

    
}

