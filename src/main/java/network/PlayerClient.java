package network;

import java.net.Socket;

import model.App;
import model.Player;

import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerClient extends Player {

    private App app;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public PlayerClient(InetAddress serverAddress, int serverPort) throws IOException {
        super(Color.BLUE, "player 1");
        this.socket = new Socket(serverAddress, serverPort);
        // Écoute des messages entrants dans un thread séparé
        executor.submit(() -> {
            try {
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                Object object;
                while ((object = in.readObject()) != null) {
                    if (((NetworkObjet) object).getMessage().equals("start")) {
                        app.getMainMenu().startapp();
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
}
