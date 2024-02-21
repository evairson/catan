package start;

import model.App;
import network.PlayerClient;
import network.Server;

import java.net.InetAddress;
import java.util.Scanner;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Quel est votre nom ?");
        String name = sc.nextLine();
        System.out.println("1 : créer serveur 2 : se connecter à un existant");
        if (Integer.parseInt(sc.nextLine()) == 1) {
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
        } else {
            System.out.println("Entrez l'adresse IP du serveur :");
            try {
                InetAddress address = InetAddress.getByName(sc.nextLine());
                PlayerClient player = new PlayerClient(name, address, 8000);
                System.out.println("yeaaah2");
                SwingUtilities.invokeLater(() -> {
                    System.out.println("launching game");
                    App game = new App(player);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
