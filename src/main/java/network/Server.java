package network;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        int port = 12345; // Choisissez un port non utilisé sur votre machine
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Le serveur est en attente de connexion sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion avec : " + clientSocket.getInetAddress());

                // Créer un nouveau thread pour chaque client pour traiter sa connexion
                new ClientHandler(clientSocket).start();
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

