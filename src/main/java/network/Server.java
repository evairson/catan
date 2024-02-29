package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import model.Player;
import network.NetworkObject.TypeObject;

public class Server {
    private static final int PORT = 8000;
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static HashSet<Player> players = new HashSet<>();

    public Server() throws Exception {
        System.out.println("Le serveur est démarré sur le port " + PORT);
        ServerSocket listener = new ServerSocket(PORT);
        int id = 0;
        try {
            while (true) {
                new ClientHandler(listener.accept(), id).start();
                id++;
                System.out.println("Nouveau joueur");
            }
        } finally {
            listener.close();
        }
    }

    // Thread pour gérer chaque client
    private static class ClientHandler extends Thread {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private int id;

        ClientHandler(Socket socket, int id) {
            this.id = id;
            this.socket = socket;
        }

        public void run() {
            try {
                // Créer le flux d'entrée pour lire les messages du client
                in = new ObjectInputStream(socket.getInputStream());
                // Créer le flux de sortie pour envoyer des messages au client
                out = new ObjectOutputStream(socket.getOutputStream());

                NetworkObject idPlayer = new NetworkObject(TypeObject.Message, "ID", id, null);

                out.writeUnshared(idPlayer);
                out.flush();

                NetworkObject name = (NetworkObject) in.readObject();
                Player player = new Player(Player.Color.RED, name.getMessage(), id);
                players.add(player);
                System.out.println(name.getMessage() + " est bien connecté(e) au serveur.");
                // Ajouter le flux de sortie à l'ensemble des écrivains
                synchronized (writers) {
                    writers.add(out);
                }

                // Lire les messages du client et les redistribuer
                Object input;
                while ((input = in.readObject()) != null) {
                    // Redistribuer le message à tous les clients
                    if (((NetworkObject) input).getMessage().equals("tryStartGame")) {
                        System.out.println("J'ai recu");
                        input = new NetworkObject(TypeObject.Message, "NamePlayers", id, players);
                        out.writeUnshared(input);
                        out.flush();
                    } else {
                        synchronized (writers) {
                            for (ObjectOutputStream writer : writers) {
                                writer.writeUnshared(input);
                                writer.flush();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                }
                synchronized (writers) {
                    writers.remove(out);
                }
            }
        }
    }
}
