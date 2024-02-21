package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class Server {
    private static final int PORT = 8000;
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();

    public Server() throws Exception {
        System.out.println("Le serveur est démarré sur le port " + PORT);
        ServerSocket listener = new ServerSocket(PORT);

        try {
            while (true) {
                new ClientHandler(listener.accept()).start();
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

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Créer le flux d'entrée pour lire les messages du client
                in = new ObjectInputStream(socket.getInputStream());
                // Créer le flux de sortie pour envoyer des messages au client
                out = new ObjectOutputStream(socket.getOutputStream());

                // Ajouter le flux de sortie à l'ensemble des écrivains
                synchronized (writers) {
                    writers.add(out);
                }

                // Lire les messages du client et les redistribuer
                Object input;
                while ((input = in.readObject()) != null) {
                    // Redistribuer le message à tous les clients
                    synchronized (writers) {
                        for (ObjectOutputStream writer : writers) {
                            writer.writeObject(input);
                            writer.flush();
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
