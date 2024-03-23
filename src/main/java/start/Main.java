package start;

import model.App;
import model.Player;
import model.Player.Color;
import network.PlayerClient;
import network.Server;

import java.net.InetAddress;
import java.util.Scanner;


import javax.swing.*;

public class Main {

    private static boolean server = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Quel est votre nom ?");
        String name = sc.nextLine();
        System.out.println("1 : créer serveur 2 : se connecter à un existant 3 : Jouer en local");
        int nextLine = Integer.parseInt(sc.nextLine());
        if (nextLine == 1) {
            new Thread(() -> {
                try {
                    Server server = new Server();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }).start();
            try {
                InetAddress address = InetAddress.getByName("localhost");
                System.out.println(address);
                PlayerClient player = new PlayerClient(name, address, 8000);
                System.out.println("yeaaah");
                SwingUtilities.invokeLater(() -> {
                    System.out.println("launching game");
                    App game = new App(player);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (nextLine == 2) {
            System.out.println("Entrez l'adresse IP du serveur :");
            try {
                InetAddress address = InetAddress.getByName(sc.nextLine());
                PlayerClient player = new PlayerClient(name, address, 8000);
                SwingUtilities.invokeLater(() -> {
                    System.out.println("launching game");
                    App game = new App(player);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            server = false;
            SwingUtilities.invokeLater(() -> {
                System.out.println("launching game");
                Player player = new Player(Color.BLUE, name, 0);
                App game = new App(player);
            });
        }
    }

    public static boolean hasServer() {
        return server;
    }
}
