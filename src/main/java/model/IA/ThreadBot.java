package model.IA;

import exceptionclass.ConstructBuildingException;
import model.App;
import model.Game;
import model.tiles.TileEdge;
import model.tiles.TileVertex;

public class ThreadBot extends Thread {
    private Game game;
    private Bot bot;

    public ThreadBot(Game game, Bot bot) {
        this.game = game;
        this.bot = bot;
    }

    public void run() {
        try {
            Thread.sleep(3000);
            if (game.isStart() || game.isBackwards()) {
                TileVertex vertex = Bot.getBetterVertex(game);
                try {
                    game.buildColony(vertex.getId());
                } catch (ConstructBuildingException e) {
                    System.out.println("erreur lors du placement de la colony");
                }
                Thread.sleep(3000);
                TileEdge edge = Bot.getRoadNext(game, vertex);
                try {
                    if (edge != null) {
                        game.buildRoad(edge.getId());
                    }
                } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }
                Thread.sleep(3000);
                game.endTurn();
            } else {
                /* FIXME : Ceci crée un nouveau thread dans roll ce qui peut causer des problèmes
                    si endTurn s'execute avant la fin du thread */
                App.getActionPlayerPanel().getRollingDice().roll();
                Thread.sleep(5000);
                if (game.canBuildRoad()) {
                    System.out.println("ok je peux");
                    bot.buildBestRoad(game);
                    Thread.sleep(2000);
                }
                if (game.canBuildColony()) {
                    TileVertex vertex = Bot.getBetterVertex(game);
                    try {
                        game.buildColony(vertex.getId());
                    } catch (ConstructBuildingException e) {
                        System.out.println("erreur lors du placement de la colony");
                    }
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
