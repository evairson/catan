package model.IA;

import model.Game;

public class ThreadBot extends Thread {
    private Game game;

    public ThreadBot(Game game) {
        this.game = game;
    }

    public void run() {
        try {
            Thread.sleep(3000);
            if (game.isStart() || game.isBackwards()) {
                game.placeRoadAndColonyBot(false);
                Thread.sleep(3000);
                game.placeRoadAndColonyBot(true);
                Thread.sleep(3000);
                game.endTurn();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
