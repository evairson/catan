package start;

import model.App;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("launching game");
            App game = new App();
        });
    }
}
