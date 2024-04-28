package network;

import java.net.Socket;

import model.App;
import model.Game;
import model.Player;
import network.NetworkObject.TypeObject;

import javax.swing.*;
import java.awt.*;

import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import exceptionclass.ConstructBuildingException;
import exceptionclass.NetworkObjectException;

public class PlayerClient extends Player {
    private static App app;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public PlayerClient(String s, InetAddress serverAddress, int serverPort) throws IOException {
        super(Color.BLUE, s);
        this.socket = new Socket(serverAddress, serverPort);
        // Écoute des messages entrants dans un thread séparé
        executor.submit(() -> {
            try {
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                NetworkObject namePlayer = new NetworkObject(TypeObject.Message, name, id, null);
                out.writeObject(namePlayer);
                out.flush();
                Object object;
                while ((object = in.readObject()) != null) {
                    NetworkObject networkObject = ((NetworkObject) object);
                    switch (networkObject.getType()) {
                        case Game:
                            game(networkObject);
                            break;
                        case Message:
                            message(networkObject);
                            break;
                        case Board:
                            board(networkObject);
                            break;
                        case ChatMessage:
                            app.addMessage(networkObject.getMessage());
                        default:
                            System.out.println("Pas de message correspondant");
                            break;
                    }
                }
            } catch (NetworkObjectException e) {
                NetworkObjectException.messageError();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Fermer proprement les ressources
    public void close() throws IOException {
        in.close();
        socket.close();
        executor.shutdown();
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setApp(App app) {
        PlayerClient.app = app;
    }

    public int getId() {
        return id;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public boolean isMyTurn(Game game) {
        return id == game.getCurrentPlayer().getId();
    }

    public void game(NetworkObject networkObject) {
        switch (networkObject.getMessage()) {
            case "startGame" :
                app.getMainMenu().startapp((Game) (networkObject.getObject()));
                break;
            case "dices" :
                int[] tab = (int[]) networkObject.getObject();
                App.getActionPlayerPanel().getRollingDice().networkThrowDices(tab);
                if (networkObject.getId() == id) {
                    App.getActionPlayerPanel().updateShopPanel();
                }
                break;
            case "trade" :
                TradeObject tradeObject = (TradeObject) networkObject.getObject();
                if (tradeObject.getIdPlayer() == id) {
                    App.getActionPlayerPanel().showTradePanel(tradeObject);
                }
                break;
            case "tradeAccept" :
                int idTrader = (int) networkObject.getObject();
                if (idTrader == id) {
                    App.getActionPlayerPanel().getTradePanel().acceptAction(false);
                    App.getActionPlayerPanel().update();
                }
                break;
            case "changeThief" :
                app.getBoard().changehighlitedTile((int) networkObject.getObject());
                app.getBoard().changeThief();
                break;
            default:
                System.out.println("Pas de message correspondant");
                break;
        }
    }

    public void message(NetworkObject networkObjet) throws NetworkObjectException {
        switch (networkObjet.getMessage()) {
            case "ID":
                id = networkObjet.getId();
                System.out.println("Je suis le joueur numéro : " + id);
                break;
            case "NamePlayers":
                HashSet<Player> hashSet = (HashSet<Player>) networkObjet.getObject();
                app.startGame(hashSet);
                break;
            case "changeTurn":
                app.getGame().endTurn();
                app.addMessageColor("C'est au tour de ", java.awt.Color.BLACK);
                app.addMessageColor(app.getGame().getCurrentPlayer().getName() + "\n",
                    app.getGame().getCurrentPlayer().getColorAwt());
                break;
            case "DrawCard":
                if (id != networkObjet.getId()) {
                    App.getActionPlayerPanel().drawCardServer();
                }
                app.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                    app.getGame().getCurrentPlayer().getColorAwt());
                app.addMessageColor(" vient d'acheter une carte de developpement \n", java.awt.Color.RED);
                break;
            default:
                throw new NetworkObjectException();
        }
    }

    public void board(NetworkObject networkObject) throws NetworkObjectException {
        try {
            switch (networkObject.getMessage()) {
                case "buildCity":
                    int idCity = (int) networkObject.getObject();
                    app.getGame().buildCity(idCity);
                    if (networkObject.getId() == id) {
                        App.getActionPlayerPanel().updateShopPanel();
                    }
                    app.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                        app.getGame().getCurrentPlayer().getColorAwt());
                    app.addMessageColor(" vient de placer une ville \n", java.awt.Color.BLACK);
                    break;
                case "buildColony":
                    int idColony = (int) networkObject.getObject();
                    app.getGame().buildColony(idColony);
                    if (networkObject.getId() == id) {
                        App.getActionPlayerPanel().updateShopPanel();
                    }
                    app.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                        app.getGame().getCurrentPlayer().getColorAwt());
                    app.addMessageColor(" vient de placer une colonie \n", java.awt.Color.BLACK);
                    break;
                case "buildRoad":
                    int idRoad = (int) networkObject.getObject();
                    app.getGame().buildRoad(idRoad);
                    if (networkObject.getId() == id) {
                        App.getActionPlayerPanel().updateShopPanel();
                    }
                    app.addMessageColor(app.getGame().getCurrentPlayer().getName(),
                        app.getGame().getCurrentPlayer().getColorAwt());
                    app.addMessageColor(" vient de placer une route \n", java.awt.Color.BLACK);
                    break;
                default:
                    break;
            }
        } catch (ConstructBuildingException e) {
            ConstructBuildingException.messageError();
        }

    }
}
