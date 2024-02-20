package start;

import model.App;
import network.Server;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1 : créer serveur 2 : se connecter à un existant");
        if (Integer.parseInt(sc.nextLine()) == 1) {
            SwingUtilities.invokeLater(() -> {
                Server server = new Server();
            });
            try {
                InetAddress address = InetAddress.getByName("localhost");
                System.out.println(address);
                Socket socket = new Socket(address, 12345);
                System.out.println("yeaaah");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Entrez l'adresse IP du serveur :");
            try {
                Socket socket = new Socket(sc.nextLine(), 12345);
                System.out.println("yeaaah2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*SwingUtilities.invokeLater(() -> {
            System.out.println("launching game");
            App game = new App();
        });*/
    }
}
