package model.bots;

import exceptionclass.ConstructBuildingException;
import model.App;
import model.Game;
import model.tiles.TileEdge;
import model.tiles.TileVertex;

public class ThreadBot extends Thread {
    private Game game;
    private Bot bot;
    private boolean otherAction = false;

    public ThreadBot(Game game, Bot bot) {
        this.game = game;
        this.bot = bot;
    }

    public void run() {
        try {
            if (!App.getBotSoloMode()) {
                Thread.sleep(3000);
            }
            if (game.isStart() || game.isBackwards()) {
                TileVertex vertex = bot.getBetterVertex(game);
                try {
                    game.buildColony(vertex.getId());
                } catch (ConstructBuildingException e) {
                }
                if (!App.getBotSoloMode()) {
                    Thread.sleep(3000);
                }
                TileEdge edge = Bot.getRoadNext(game, vertex);
                try {
                    if (edge != null) {
                        game.buildRoad(edge.getId());
                    }
                } catch (ConstructBuildingException e) {
                    ConstructBuildingException.messageError();
                }
                if (!App.getBotSoloMode()) {
                    Thread.sleep(3000);
                }
                game.endTurn();
            } else {

                App.getActionPlayerPanel().getRollingDice().roll();
                if (!App.getBotSoloMode()) {
                    Thread.sleep(5000);
                } else {
                    Thread.sleep(200);
                }
                if (game.canBuildCity()) {
                    bot.buildCity(game);
                    if (!App.getBotSoloMode()) {
                        Thread.sleep(2000);
                    }
                }
                if (bot.needToWaitForColonies(game)) {
                    bot.askForTradeColonies(game);
                    if (game.canBuildColony()) {
                        TileVertex vertex = bot.getBetterVertex(game);
                        if (vertex != null) {
                            try {
                                game.buildColony(vertex.getId());
                            } catch (ConstructBuildingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            otherAction = true;
                        }
                    }
                }
                if (!bot.needToWaitForColonies(game) || otherAction) {
                    if (game.canBuildColony()) {
                        TileVertex vertex = bot.getBetterVertex(game);
                        if (vertex != null) {
                            try {
                                game.buildColony(vertex.getId());
                            } catch (ConstructBuildingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (game.canBuildRoad()) {
                        if ((bot.getNumberRoads(game)
                            < 2 * bot.getNumberColonies(game) + 1)) {
                            bot.buildBestRoad(game);
                            if (!App.getBotSoloMode()) {
                                Thread.sleep(2000);
                            }
                        }
                    } else {
                        bot.askForTradeRoads(game);
                        if (game.canBuildRoad()) {
                            if ((bot.getNumberRoads(game)
                                < 2 * bot.getNumberColonies(game) + 1)) {
                                bot.buildBestRoad(game);
                                if (!App.getBotSoloMode()) {
                                    Thread.sleep(2000);
                                }
                            }
                        }
                    }
                }
                if (game.canDraw()) {
                    bot.drawCard(game.getStack());
                }
                if (!App.getBotSoloMode()) {
                    Thread.sleep(3000);
                }
                otherAction = false;
                game.endTurn();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
