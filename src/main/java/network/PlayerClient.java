package network;

import java.net.Socket;

import model.App;
import model.Game;
import model.Player;
import network.NetworkObject.TypeObject;

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
                            switch (networkObject.getMessage()) {
                                case "startGame" :
                                    app.getMainMenu().startapp((Game) (networkObject.getObject()));
                                    break;
                                case "dices" :
                                    int[] tab = (int[]) networkObject.getObject();
                                    App.getActionPlayerPanel().getRollingDice().networkThrowDices(tab);
                                    break;
                                case "trade" :
                                    TradeObject tradeObject = (TradeObject) networkObject.getObject();
                                    if (tradeObject.getIdPlayer() == id) {
                                        System.out.println("c'est moi");
                                        App.getActionPlayerPanel().showTradePanel(tradeObject);
                                    }
                                    break;
                                case "tradeAccept" :
                                    int idTrader = (int) networkObject.getObject();
                                    if (idTrader == id) {
                                        System.out.println("c'est moi");
                                        App.getActionPlayerPanel().getTradePanel().acceptAction(false);
                                        App.getActionPlayerPanel().update();
                                    }
                                    break;
                                default:
                                    System.out.println("Pas de message correspondant");
                                    break;
                            }
                            break;
                        case Message:
                            message(networkObject);
                            break;
                        case Board:
                            board(networkObject);
                            break;
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
                break;
            default:
                throw new NetworkObjectException();
        }
    }

    public void board(NetworkObject networkObject) throws NetworkObjectException {
        try {
            switch (networkObject.getMessage()) {
                case "buildCity":
                    System.out.println("try to construct a city Network");
                    int idCity = (int) networkObject.getObject();
                    app.getGame().buildCity(idCity);
                    break;
                case "buildColony":
                    System.out.println("try to construct a colony Network");
                    int idColony = (int) networkObject.getObject();
                    app.getGame().buildColony(idColony);
                    break;
                case "buildRoad":
                    System.out.println("try to construct a road Network");
                    int idRoad = (int) networkObject.getObject();
                    app.getGame().buildRoad(idRoad);
                    break;
                default:
                    break;
            }
        } catch (ConstructBuildingException e) {
            ConstructBuildingException.messageError();
        }

    }
}
