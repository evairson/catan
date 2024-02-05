package src.main.java.start;

import model.Game;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("launching game");
            Game game = new Game();
        });
    }
}
