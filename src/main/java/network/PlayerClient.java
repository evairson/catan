package network;

import java.net.Socket;

import model.App;
import model.Game;
import model.Player;
import model.tiles.TileEdge;
import model.tiles.TileVertex;
import network.NetworkObject.TypeObject;

import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                    NetworkObject networkObjet = ((NetworkObject) object);
                    switch (networkObjet.getType()) {
                        case Game:
                            if (networkObjet.getMessage().equals("startGame")) {
                                app.getMainMenu().startapp((Game) (networkObjet.getObject()));
                            }
                            break;
                        case Message:
                            if (networkObjet.getMessage().equals("ID")) {
                                id = networkObjet.getId();
                                System.out.println("Je suis le joueur numéro : " + id);
                            }
                            if (networkObjet.getMessage().equals("NamePlayers")) {
                                HashSet<Player> hashSet = (HashSet<Player>) networkObjet.getObject();
                                app.startGame(hashSet);
                            }
                            if (networkObjet.getMessage().equals("changeTurn")) {
                                app.getGame().endTurn();
                            }
                            break;
                        case Board:
                            if (networkObjet.getMessage().equals("buildCity")) {
                                System.out.println("derchos");
                                TileVertex cVertex = (TileVertex) networkObjet.getObject();
                                app.getGame().buildCity(cVertex);
                            }
                            if (networkObjet.getMessage().equals("buildColony")) {
                                System.out.println("derchos");
                                TileVertex cVertex = (TileVertex) networkObjet.getObject();
                                app.getGame().buildColony(cVertex);
                            }
                            if (networkObjet.getMessage().equals("buildRoad")) {
                                System.out.println("derchos");
                                TileEdge cEdge = (TileEdge) networkObjet.getObject();
                                app.getGame().buildRoad(cEdge);
                            }
                        case ChatMessage:
                            app.addMessage(networkObjet.getMessage());
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // N'oubliez pas de fermer proprement vos ressources
    public void close() throws IOException {
        in.close();
        socket.close();
        executor.shutdown();
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setApp(App app) {
        this.app = app;
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
}
