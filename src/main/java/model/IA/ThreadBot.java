package model.IA;

import model.App;
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
            } else {
                /* FIXME : Ceci crée un nouveau thread dans roll ce qui peut causer des problèmes
                    si endTurn s'execute avant la fin du thread */
                App.getActionPlayerPanel().getRollingDice().roll();
                Thread.sleep(5000);
                int nbAlea = (int) (Math.random() * 3);
                switch (nbAlea) {
                    case 0:
                        if (game.canBuildRoad()) {
                            game.placeRoadAndColonyBot(true);
                        }
                        break;
                    case 1:
                        if (game.canBuildColony()) {
                            game.placeRoadAndColonyBot(false);
                        }
                        break;
                    //case 3:
                        //if (game.canBuildCity()) {
                            //game.placeCityBot();
                        //}
                    default:
                        break;
                }
                Thread.sleep(3000);
                game.endTurn();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
