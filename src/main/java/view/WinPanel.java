package view;

import model.App;

import javax.swing.*;
import java.awt.*;

public class WinPanel extends JPanel {
    private App app;
    public WinPanel(App app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel winnerLabel = new JLabel("JoueurX a gagné");
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(winnerLabel);

        add(Box.createRigidArea(new Dimension(0, 10))); // Ajoute un espace vertical

        addLabel("Your points : " + app.getGame().getCurrentPlayer());
        addLabel("Colony Number");
        addLabel("City Number");
        addLabel("Route number");
        addLabel("Your position in game");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton returnButton = new JButton("Return to main menu");
        returnButton.addActionListener(e -> {
            Container contentPane = this.app.getGameWindow().getContentPane();
            CardLayout layout = this.app.getGameWindow().getLayout();
            app.createNewGame();
            layout.show(contentPane,"mainMenu");
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(returnButton);
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
    }
}
